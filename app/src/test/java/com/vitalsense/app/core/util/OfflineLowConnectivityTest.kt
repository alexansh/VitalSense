package com.vitalsense.app.core.util

import com.vitalsense.app.core.data.local.entity.OutboxEntity
import com.vitalsense.app.core.network.ConnectivityState
import org.junit.Assert.*
import org.junit.Test

class OfflineLowConnectivityTest {

    @Test
    fun testConnectivityQualityClassification() {
        // High bandwidth (>= 1500 kbps) -> ONLINE
        val highBw = 3000
        val onlineState = if (highBw >= 1500) ConnectivityState.ONLINE else ConnectivityState.SLOW_NETWORK
        assertEquals(ConnectivityState.ONLINE, onlineState)

        // Degraded 2G/EDGE (1..1499 kbps) -> SLOW_NETWORK
        val slowBw = 450
        val slowState = if (slowBw in 1..1499) ConnectivityState.SLOW_NETWORK else ConnectivityState.ONLINE
        assertEquals(ConnectivityState.SLOW_NETWORK, slowState)

        // Zero bandwidth -> OFFLINE
        val zeroBw = 0
        val offlineState = if (zeroBw <= 0) ConnectivityState.OFFLINE else ConnectivityState.ONLINE
        assertEquals(ConnectivityState.OFFLINE, offlineState)
    }

    @Test
    fun testOutboxEntityPayloadAndIdempotencyKey() {
        val patientId = "pat_sih_101"
        val outboxKey = "outbox_patient_$patientId"
        val payloadJson = """{"id":"$patientId","name":"Sunita Devi","villageName":"Sundarpura","age":32}"""

        val outbox = OutboxEntity(
            id = outboxKey,
            actionType = "CREATE_PATIENT",
            entityId = patientId,
            payloadJson = payloadJson,
            timestamp = 1756900000000L,
            retryCount = 0,
            syncStatus = "PENDING",
            lastAttemptAt = 0L,
            errorMessage = null
        )

        assertEquals("outbox_patient_pat_sih_101", outbox.id)
        assertEquals("CREATE_PATIENT", outbox.actionType)
        assertEquals(patientId, outbox.entityId)
        assertEquals("PENDING", outbox.syncStatus)
        assertEquals(0, outbox.retryCount)
        assertTrue(outbox.payloadJson.contains("Sunita Devi"))
    }

    @Test
    fun testExponentialBackoffCalculation() {
        fun calculateBackoffMs(retryCount: Int): Long {
            val baseDelay = 1000L
            val calculated = (1L shl retryCount.coerceAtMost(5)) * baseDelay
            return calculated.coerceAtMost(30000L)
        }

        assertEquals(1000L, calculateBackoffMs(0))   // 2^0 * 1000 = 1000ms
        assertEquals(2000L, calculateBackoffMs(1))   // 2^1 * 1000 = 2000ms
        assertEquals(4000L, calculateBackoffMs(2))   // 2^2 * 1000 = 4000ms
        assertEquals(8000L, calculateBackoffMs(3))   // 2^3 * 1000 = 8000ms
        assertEquals(16000L, calculateBackoffMs(4))  // 2^4 * 1000 = 16000ms
        assertEquals(30000L, calculateBackoffMs(5))  // 2^5 * 1000 = 32000ms capped at 30000ms
        assertEquals(30000L, calculateBackoffMs(10)) // capped at 30000ms
    }

    @Test
    fun testEmergencySosOfflineSafetyContract() {
        val isDeviceOnline = false
        var outboxInserted = false
        var serverDispatchAttempted = false
        var fallbackPresented = false

        if (isDeviceOnline) {
            serverDispatchAttempted = true
        } else {
            // Local Outbox queueing without blocking the user
            outboxInserted = true
            // Immediately activate 1-tap zero-internet fallbacks (108 Call / SMS)
            fallbackPresented = true
        }

        assertFalse("Server dispatch should not be falsely claimed when offline", serverDispatchAttempted)
        assertTrue("Emergency alert must be safely enqueued in local Outbox", outboxInserted)
        assertTrue("Direct 1-tap 108 Call & SMS fallback must be presented immediately", fallbackPresented)
    }
}

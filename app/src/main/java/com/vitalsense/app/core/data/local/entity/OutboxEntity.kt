package com.vitalsense.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Durable outbox record for storing pending mutations when the device is offline or on weak connectivity.
 * Flushed automatically by WorkManager & NetworkMonitor upon network reconnection (SIH26133).
 */
@Entity(tableName = "outbox_records")
data class OutboxEntity(
    @PrimaryKey val id: String,
    val actionType: String,
    val entityId: String,
    val payloadJson: String,
    val timestamp: Long = System.currentTimeMillis(),
    val retryCount: Int = 0,
    val syncStatus: String = "PENDING", // PENDING, SYNCING, SYNCED, FAILED
    val lastAttemptAt: Long = 0L,
    val errorMessage: String? = null
)

package com.vitalsense.app.core.sync

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages scheduling of background synchronization for the offline-first outbox pattern (SIH26133).
 */
@Singleton
class SyncManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    init {
        // Observe sync work state
        try {
            workManager.getWorkInfosForUniqueWorkLiveData("VitalSense_Immediate_Outbox_Sync")
                .observeForever { workInfos ->
                    val running = workInfos?.any { it.state == WorkInfo.State.RUNNING } ?: false
                    _isSyncing.value = running
                }
        } catch (_: Exception) {}
    }

    /**
     * Trigger an immediate synchronization pass as soon as network is connected.
     */
    fun triggerImmediateSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10,
                TimeUnit.SECONDS
            )
            .build()

        workManager.enqueueUniqueWork(
            "VitalSense_Immediate_Outbox_Sync",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    /**
     * Schedule periodic background sync every 15 minutes.
     */
    fun schedulePeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicSyncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "VitalSense_Periodic_Outbox_Sync",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncRequest
        )
    }
}

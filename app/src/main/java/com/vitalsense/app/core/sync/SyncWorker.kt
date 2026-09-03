package com.vitalsense.app.core.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vitalsense.app.core.data.local.VitalSenseDatabase
import com.vitalsense.app.core.data.model.SyncState
import com.vitalsense.app.core.data.remote.FirestoreDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val database: VitalSenseDatabase,
    private val firestoreDataSource: FirestoreDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val dao = database.vitalSenseDao()
            Log.d("SyncWorker", "Starting offline sync pass...")

            // 1. Sync Patients
            val pendingPatients = dao.getPendingPatients()
            for (entity in pendingPatients) {
                try {
                    // Reconstruct domain model to push
                    val model = com.vitalsense.app.core.data.model.Patient(
                        id = entity.id, name = entity.name, age = entity.age, gender = entity.gender,
                        phone = entity.phone, villageId = entity.villageId, villageName = entity.villageName,
                        ashaWorkerId = entity.ashaWorkerId, ashaWorkerName = entity.ashaWorkerName,
                        currentRiskLevel = entity.currentRiskLevel, lastCondition = entity.lastCondition,
                        lastVisitDate = entity.lastVisitDate, nextAppointmentDate = entity.nextAppointmentDate,
                        emergencyContact = entity.emergencyContact, profilePhotoUrl = entity.profilePhotoUrl
                    )
                    firestoreDataSource.uploadPatient(model)
                    dao.insertPatient(entity.copy(syncState = SyncState.SYNCED))
                } catch (e: Exception) {
                    Log.e("SyncWorker", "Failed to sync patient ${entity.id}", e)
                }
            }

            // 2. Sync Condition Records
            val pendingConditions = dao.getPendingConditionRecords()
            for (entity in pendingConditions) {
                try {
                    val model = com.vitalsense.app.core.data.model.ConditionRecord(
                        id = entity.id, patientId = entity.patientId, patientName = entity.patientName,
                        villageId = entity.villageId, villageName = entity.villageName,
                        category = entity.category, severity = entity.severity,
                        requestedDoctorType = entity.requestedDoctorType, notes = entity.notes,
                        timestamp = entity.timestamp, ashaProxyLogged = entity.ashaProxyLogged,
                        status = entity.status, assignedDoctorId = entity.assignedDoctorId,
                        assignedDoctorName = entity.assignedDoctorName, doctorResponse = entity.doctorResponse,
                        doctorResponseTimestamp = entity.doctorResponseTimestamp, doctorResponseDoctorName = entity.doctorResponseDoctorName,
                        privateDoctorNotes = entity.privateDoctorNotes, referredByDoctorId = entity.referredByDoctorId,
                        referredByDoctorName = entity.referredByDoctorName, referralNotes = entity.referralNotes,
                        syncState = SyncState.SYNCED, serverVersion = entity.serverVersion
                    )
                    firestoreDataSource.uploadConditionRecord(model)
                    dao.insertConditionRecord(entity.copy(syncState = SyncState.SYNCED))
                } catch (e: Exception) {
                    Log.e("SyncWorker", "Failed to sync condition ${entity.id}", e)
                }
            }
            
            // Note: In a complete implementation we would do this for Prescriptions, Appointments, and Notices as well.
            // But we have enough here to prove out the Worker logic.

            Log.d("SyncWorker", "Offline sync pass completed successfully.")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Sync pass failed", e)
            Result.retry()
        }
    }
}

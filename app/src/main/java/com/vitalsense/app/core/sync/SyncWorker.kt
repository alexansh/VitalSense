package com.vitalsense.app.core.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.vitalsense.app.core.data.local.VitalSenseDatabase
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.data.remote.FirestoreDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Background WorkManager worker that flushes durable outbox records to Cloud Firestore.
 * Implements the offline-first outbox pattern defined for SIH26133 low-connectivity environments.
 */
class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val TAG = "VitalSenseSyncWorker"
    private val gson = Gson()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🔄 VitalSense SyncWorker started. Flushing pending offline outbox records...")
            val database = VitalSenseDatabase.getDatabase(applicationContext)
            val dao = database.vitalSenseDao()
            val firestore = FirebaseFirestore.getInstance()
            val firestoreDataSource = FirestoreDataSource(firestore)
            val pendingRecords = dao.getPendingOutboxRecords()
            Log.d(TAG, "Found ${pendingRecords.size} pending outbox items to synchronize.")

            var failureCount = 0

            for (record in pendingRecords) {
                try {
                    dao.updateOutboxStatus(
                        id = record.id,
                        status = "SYNCING",
                        attemptAt = System.currentTimeMillis(),
                        error = null
                    )

                    when (record.actionType) {
                        "CONDITION_RECORD" -> {
                            val condition = gson.fromJson(record.payloadJson, ConditionRecord::class.java)
                            firestoreDataSource.uploadConditionRecord(condition)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed condition record: ${record.entityId}")
                        }
                        "PRESCRIPTION" -> {
                            val rx = gson.fromJson(record.payloadJson, Prescription::class.java)
                            firestoreDataSource.uploadPrescription(rx)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed prescription: ${record.entityId}")
                        }
                        "APPOINTMENT" -> {
                            val appt = gson.fromJson(record.payloadJson, Appointment::class.java)
                            firestoreDataSource.uploadAppointment(appt)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed appointment: ${record.entityId}")
                        }
                        "BROADCAST_NOTICE", "SOS_ALERT" -> {
                            val notice = gson.fromJson(record.payloadJson, BroadcastNotice::class.java)
                            firestoreDataSource.uploadNotice(notice)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed broadcast notice: ${record.entityId}")
                        }
                        "PATIENT" -> {
                            val patient = gson.fromJson(record.payloadJson, Patient::class.java)
                            firestoreDataSource.uploadPatient(patient)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed patient record: ${record.entityId}")
                        }
                        "CREATE_REFERRAL", "UPDATE_REFERRAL" -> {
                            val referral = gson.fromJson(record.payloadJson, Referral::class.java)
                            firestoreDataSource.uploadReferral(referral)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed referral: ${record.entityId}")
                        }
                        "QUEUE_ENTRY" -> {
                            val entry = gson.fromJson(record.payloadJson, QueueEntry::class.java)
                            if (entry.provisionalToken) {
                                val authoritative = firestoreDataSource.assignAuthoritativeTokenAndSave(entry)
                                dao.upsertQueueEntry(
                                    com.vitalsense.app.core.data.local.entity.QueueEntryEntity(
                                        id = authoritative.id,
                                        doctorId = authoritative.doctorId,
                                        doctorName = authoritative.doctorName,
                                        dateFormatted = authoritative.dateFormatted,
                                        tokenNumber = authoritative.tokenNumber,
                                        provisionalToken = false,
                                        appointmentId = authoritative.appointmentId,
                                        patientId = authoritative.patientId,
                                        patientName = authoritative.patientName,
                                        source = authoritative.source,
                                        status = authoritative.status,
                                        priorityFlag = authoritative.priorityFlag,
                                        checkedInAt = authoritative.checkedInAt,
                                        calledAt = authoritative.calledAt,
                                        consultationStartedAt = authoritative.consultationStartedAt,
                                        completedAt = authoritative.completedAt,
                                        outcomeNotes = authoritative.outcomeNotes,
                                        isPendingSync = false
                                    )
                                )
                            } else {
                                firestoreDataSource.uploadQueueEntry(entry)
                            }
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed queue entry: ${record.entityId}")
                        }
                        "DOCTOR_DAY_SLOT" -> {
                            val slot = gson.fromJson(record.payloadJson, DoctorDaySlotConfig::class.java)
                            firestoreDataSource.uploadDoctorSlot(slot)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed doctor day slot: ${record.entityId}")
                        }
                        "CREATE_REFERRAL", "UPDATE_REFERRAL", "REFERRAL" -> {
                            val referral = gson.fromJson(record.payloadJson, Referral::class.java)
                            firestoreDataSource.uploadReferral(referral)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed referral: ${record.entityId}")
                        }
                        "DAILY_ROUND" -> {
                            val round = gson.fromJson(record.payloadJson, DailyRound::class.java)
                            firestoreDataSource.uploadDailyRound(round)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed daily round: ${record.entityId}")
                        }
                        "IMMUNIZATION_RECORD" -> {
                            val recordData = gson.fromJson(record.payloadJson, ImmunizationRecord::class.java)
                            firestoreDataSource.uploadImmunizationRecord(recordData)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed immunization record: ${record.entityId}")
                        }
                        "ASHA_MEDICINE" -> {
                            val medicine = gson.fromJson(record.payloadJson, AshaMedicine::class.java)
                            firestoreDataSource.uploadAshaMedicine(medicine)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed asha medicine: ${record.entityId}")
                        }
                        "DISPENSARY_ITEM" -> {
                            val item = gson.fromJson(record.payloadJson, DispensaryItem::class.java)
                            firestoreDataSource.uploadDispensaryItem(item)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed dispensary item: ${record.entityId}")
                        }
                        "LAB_REPORT" -> {
                            val report = gson.fromJson(record.payloadJson, LabReport::class.java)
                            firestoreDataSource.uploadLabReport(report)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed lab report: ${record.entityId}")
                        }
                        "OPD_TOKEN" -> {
                            val token = gson.fromJson(record.payloadJson, OpdToken::class.java)
                            firestoreDataSource.uploadOpdToken(token)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed opd token: ${record.entityId}")
                        }
                        "MEDICAL_CERTIFICATE" -> {
                            val cert = gson.fromJson(record.payloadJson, MedicalCertificate::class.java)
                            firestoreDataSource.uploadMedicalCertificate(cert)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed medical certificate: ${record.entityId}")
                        }
                        "EXTERNAL_REFERRAL" -> {
                            val extRef = gson.fromJson(record.payloadJson, ExternalReferral::class.java)
                            firestoreDataSource.uploadExternalReferral(extRef)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed external referral: ${record.entityId}")
                        }
                        "MEDICAL_HISTORY_ENTRY" -> {
                            val entry = gson.fromJson(record.payloadJson, MedicalHistoryEntry::class.java)
                            firestoreDataSource.uploadMedicalHistory(entry)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed medical history entry: ${record.entityId}")
                        }
                        "CALL_LOG" -> {
                            val callLog = gson.fromJson(record.payloadJson, CallLog::class.java)
                            firestoreDataSource.uploadCallLog(callLog)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed call log: ${record.entityId}")
                        }
                        "AUDIT_LOG" -> {
                            val auditLog = gson.fromJson(record.payloadJson, AuditLog::class.java)
                            firestoreDataSource.uploadAuditLog(auditLog)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed audit log: ${record.entityId}")
                        }
                        "DISEASE_TREND" -> {
                            val trend = gson.fromJson(record.payloadJson, DiseaseTrendRecord::class.java)
                            firestoreDataSource.uploadDiseaseTrend(trend)
                            dao.deleteOutboxRecord(record.id)
                            Log.d(TAG, "✓ Flushed disease trend: ${record.entityId}")
                        }
                        else -> {
                            dao.deleteOutboxRecord(record.id)
                        }
                    }
                } catch (e: Exception) {
                    failureCount++
                    Log.w(TAG, "Sync retry for record ${record.id} (${record.actionType}): ${e.message}")
                    dao.updateOutboxStatus(
                        id = record.id,
                        status = "FAILED",
                        attemptAt = System.currentTimeMillis(),
                        error = e.message ?: "Network error"
                    )
                }
            }

            if (failureCount > 0) {
                Result.retry()
            } else {
                Result.success()
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ SyncWorker execution failure: ${e.message}", e)
            Result.retry()
        }
    }
}

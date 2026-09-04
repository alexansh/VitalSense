package com.vitalsense.app.core.data.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vitalsense.app.core.data.model.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "VitalSenseFirebase"

@Singleton
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Collection references
    private val patientsCollection = firestore.collection("patients")
    private val conditionsCollection = firestore.collection("condition_records")
    private val prescriptionsCollection = firestore.collection("prescriptions")
    private val appointmentsCollection = firestore.collection("appointments")
    private val noticesCollection = firestore.collection("broadcast_notices")
    private val villagesCollection = firestore.collection("villages")
    private val queueEntriesCollection = firestore.collection("queue_entries")
    private val doctorSlotsCollection = firestore.collection("doctor_day_slots")
    private val queueCountersCollection = firestore.collection("queue_counters")
    private val referralsCollection = firestore.collection("referrals")

    init {
        // Ensure an authenticated session for Firestore security rules
        try {
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser == null) {
                auth.signInAnonymously().addOnSuccessListener {
                    Log.d(TAG, "FirebaseAuth: Anonymous sign-in success. UID=${it.user?.uid}")
                }.addOnFailureListener { e ->
                    Log.w(TAG, "FirebaseAuth: Anonymous sign-in failed (Rules in test mode will still work): ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "FirebaseAuth init error: ${e.message}")
        }
    }

    // --- PUSH OPERATIONS (Writes) ---

    suspend fun uploadConditionRecord(record: ConditionRecord) {
        try {
            val data = hashMapOf(
                "id" to record.id,
                "patientId" to record.patientId,
                "patientName" to record.patientName,
                "villageId" to record.villageId,
                "villageName" to record.villageName,
                "category" to record.category.name,
                "severity" to record.severity.name,
                "requestedDoctorType" to record.requestedDoctorType.name,
                "notes" to record.notes,
                "timestamp" to record.timestamp,
                "ashaProxyLogged" to record.ashaProxyLogged,
                "status" to record.status.name,
                "assignedDoctorId" to (record.assignedDoctorId ?: ""),
                "assignedDoctorName" to (record.assignedDoctorName ?: ""),
                "doctorResponse" to (record.doctorResponse ?: ""),
                "doctorResponseTimestamp" to (record.doctorResponseTimestamp ?: 0L),
                "doctorResponseDoctorName" to (record.doctorResponseDoctorName ?: ""),
                "privateDoctorNotes" to (record.privateDoctorNotes ?: ""),
                "referredByDoctorId" to (record.referredByDoctorId ?: ""),
                "referredByDoctorName" to (record.referredByDoctorName ?: ""),
                "referralNotes" to (record.referralNotes ?: ""),
                "isPendingSync" to false
            )
            conditionsCollection.document(record.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded condition_record: ${record.id} (${record.patientName})")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload condition_record: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadPrescription(prescription: Prescription) {
        try {
            val data = hashMapOf(
                "id" to prescription.id,
                "caseId" to (prescription.caseId ?: ""),
                "patientId" to prescription.patientId,
                "patientName" to prescription.patientName,
                "doctorId" to prescription.doctorId,
                "doctorName" to prescription.doctorName,
                "doctorSpecialty" to prescription.doctorSpecialty,
                "timestamp" to prescription.timestamp,
                "dateFormatted" to prescription.dateFormatted,
                "medicines" to prescription.medicines.map { med ->
                    hashMapOf(
                        "name" to med.name,
                        "dosage" to med.dosage,
                        "frequency" to med.frequency,
                        "duration" to med.duration,
                        "quantity" to med.quantity
                    )
                },
                "instructions" to prescription.instructions,
                "isOcrExtracted" to prescription.isOcrExtracted
            )
            prescriptionsCollection.document(prescription.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded prescription: ${prescription.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload prescription: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadAppointment(appointment: Appointment) {
        try {
            val data = hashMapOf(
                "id" to appointment.id,
                "patientId" to appointment.patientId,
                "patientName" to appointment.patientName,
                "doctorId" to appointment.doctorId,
                "doctorName" to appointment.doctorName,
                "doctorSpecialty" to appointment.doctorSpecialty,
                "dateFormatted" to appointment.dateFormatted,
                "timeSlot" to appointment.timeSlot,
                "status" to appointment.status,
                "proposedBy" to appointment.proposedBy.name,
                "outcomeNotes" to (appointment.outcomeNotes ?: "")
            )
            appointmentsCollection.document(appointment.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded appointment: ${appointment.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload appointment: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadPatient(patient: Patient) {
        try {
            val data = hashMapOf(
                "id" to patient.id,
                "name" to patient.name,
                "age" to patient.age,
                "gender" to patient.gender,
                "phone" to patient.phone,
                "villageId" to patient.villageId,
                "villageName" to patient.villageName,
                "ashaWorkerId" to patient.ashaWorkerId,
                "ashaWorkerName" to patient.ashaWorkerName,
                "currentRiskLevel" to patient.currentRiskLevel.name,
                "lastCondition" to patient.lastCondition,
                "lastVisitDate" to patient.lastVisitDate,
                "nextAppointmentDate" to patient.nextAppointmentDate,
                "emergencyContact" to patient.emergencyContact,
                "profilePhotoUrl" to patient.profilePhotoUrl
            )
            patientsCollection.document(patient.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded patient: ${patient.id} (${patient.name})")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload patient: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadNotice(notice: BroadcastNotice) {
        try {
            val data = hashMapOf(
                "id" to notice.id,
                "senderRole" to notice.senderRole.name,
                "senderName" to notice.senderName,
                "targetRole" to notice.targetRole,
                "targetVillage" to notice.targetVillage,
                "title" to notice.title,
                "message" to notice.message,
                "timestamp" to notice.timestamp,
                "isUrgent" to notice.isUrgent
            )
            noticesCollection.document(notice.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded broadcast_notice: ${notice.id} - ${notice.title}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload broadcast_notice: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadMedicalHistory(entry: MedicalHistoryEntry) {
        try {
            val data = hashMapOf(
                "id" to entry.id,
                "patientId" to entry.patientId,
                "type" to entry.type.name,
                "title" to entry.title,
                "details" to entry.details,
                "severity" to (entry.severity?.name ?: ""),
                "doctorId" to entry.doctorId,
                "doctorName" to entry.doctorName,
                "caseId" to (entry.caseId ?: ""),
                "prescriptionId" to (entry.prescriptionId ?: ""),
                "timestamp" to entry.timestamp,
                "dateFormatted" to entry.dateFormatted
            )
            firestore.collection("patientMedicalHistory").document(entry.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded patient record: ${entry.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload patient record: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadReferral(referral: Referral) {
        try {
            val historyMaps = referral.statusHistory.map {
                hashMapOf(
                    "status" to it.status.name,
                    "timestamp" to it.timestamp,
                    "changedByUserId" to it.changedByUserId,
                    "note" to (it.note ?: "")
                )
            }
            
            val data = hashMapOf(
                "id" to referral.id,
                "patientId" to referral.patientId,
                "patientName" to referral.patientName,
                "referringUserId" to referral.referringUserId,
                "referringUserName" to referral.referringUserName,
                "referringUserSpecialty" to referral.referringUserSpecialty,
                "targetDoctorId" to (referral.targetDoctorId ?: ""),
                "targetDoctorName" to (referral.targetDoctorName ?: ""),
                "targetSpecialty" to referral.targetSpecialty,
                "reason" to referral.reason,
                "clinicalQuestion" to referral.clinicalQuestion,
                "urgency" to referral.urgency.name,
                "attachedRecordIds" to referral.attachedRecordIds,
                "status" to referral.status.name,
                "statusHistory" to historyMaps,
                "declineReason" to (referral.declineReason ?: ""),
                "suggestedSpecialtyOrDoctor" to (referral.suggestedSpecialtyOrDoctor ?: ""),
                "infoRequestNote" to (referral.infoRequestNote ?: ""),
                "specialistFindings" to (referral.specialistFindings ?: ""),
                "specialistRecommendations" to (referral.specialistRecommendations ?: ""),
                "specialistFollowUpNeeded" to referral.specialistFollowUpNeeded,
                "createdAt" to referral.createdAt,
                "updatedAt" to referral.updatedAt,
                "respondedAt" to (referral.respondedAt ?: 0L),
                "completedAt" to (referral.completedAt ?: 0L)
            )
            referralsCollection.document(referral.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded referral: ${referral.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload referral: ${e.message}", e)
            throw e
        }
    }

    // --- PULL OPERATIONS (Reads / Listeners) ---

    fun getConditionRecordsStream(): Flow<List<ConditionRecord>> = callbackFlow {
        val listener = conditionsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Condition records stream transient error: ${error.message}")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    try {
                        val statusStr = doc.getString("status") ?: CaseStatus.PENDING_REVIEW.name
                        val status = runCatching { CaseStatus.valueOf(statusStr) }.getOrDefault(CaseStatus.PENDING_REVIEW)
                        ConditionRecord(
                            id = doc.getString("id") ?: doc.id,
                            patientId = doc.getString("patientId") ?: "",
                            patientName = doc.getString("patientName") ?: "",
                            villageId = doc.getString("villageId") ?: "",
                            villageName = doc.getString("villageName") ?: "",
                            category = ConditionCategory.valueOf(doc.getString("category") ?: ConditionCategory.GENERAL_MEDICINE.name),
                            severity = SeverityLevel.valueOf(doc.getString("severity") ?: SeverityLevel.LOW.name),
                            requestedDoctorType = DoctorSpecialty.valueOf(doc.getString("requestedDoctorType") ?: DoctorSpecialty.GENERAL_PHYSICIAN.name),
                            notes = doc.getString("notes") ?: "",
                            timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
                            ashaProxyLogged = doc.getBoolean("ashaProxyLogged") ?: false,
                            status = status,
                            assignedDoctorId = doc.getString("assignedDoctorId")?.takeIf { it.isNotBlank() },
                            assignedDoctorName = doc.getString("assignedDoctorName")?.takeIf { it.isNotBlank() },
                            doctorResponse = doc.getString("doctorResponse")?.takeIf { it.isNotBlank() },
                            doctorResponseTimestamp = doc.getLong("doctorResponseTimestamp")?.takeIf { it > 0 },
                            doctorResponseDoctorName = doc.getString("doctorResponseDoctorName")?.takeIf { it.isNotBlank() },
                            privateDoctorNotes = doc.getString("privateDoctorNotes")?.takeIf { it.isNotBlank() },
                            referredByDoctorId = doc.getString("referredByDoctorId")?.takeIf { it.isNotBlank() },
                            referredByDoctorName = doc.getString("referredByDoctorName")?.takeIf { it.isNotBlank() },
                            referralNotes = doc.getString("referralNotes")?.takeIf { it.isNotBlank() },
                            isPendingSync = false
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                trySend(list)
            }
        }
        awaitClose { listener.remove() }
    }

    fun getBroadcastNoticesStream(): Flow<List<BroadcastNotice>> = callbackFlow {
        val listener = noticesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Broadcast notices stream transient error: ${error.message}")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    try {
                        BroadcastNotice(
                            id = doc.getString("id") ?: doc.id,
                            senderRole = UserRole.valueOf(doc.getString("senderRole") ?: UserRole.ADMIN.name),
                            senderName = doc.getString("senderName") ?: "",
                            targetRole = doc.getString("targetRole") ?: "ALL",
                            targetVillage = doc.getString("targetVillage") ?: "All Villages",
                            title = doc.getString("title") ?: "",
                            message = doc.getString("message") ?: "",
                            timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
                            isUrgent = doc.getBoolean("isUrgent") ?: false
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                trySend(list)
            }
        }
        awaitClose { listener.remove() }
    }

    // --- LIVE QUEUE & APPOINTMENTS ---

    suspend fun assignAuthoritativeTokenAndSave(entry: QueueEntry): QueueEntry {
        val counterDocRef = queueCountersCollection.document("${entry.doctorId}_${entry.dateFormatted}")
        val entryDocRef = queueEntriesCollection.document(entry.id)

        val assignedToken = firestore.runTransaction { transaction ->
            val snapshot = transaction.get(counterDocRef)
            val currentToken = snapshot.getLong("nextToken")?.toInt() ?: 1
            transaction.set(counterDocRef, mapOf("nextToken" to (currentToken + 1)))

            val data = hashMapOf(
                "id" to entry.id,
                "doctorId" to entry.doctorId,
                "doctorName" to entry.doctorName,
                "dateFormatted" to entry.dateFormatted,
                "tokenNumber" to currentToken,
                "provisionalToken" to false,
                "appointmentId" to (entry.appointmentId ?: ""),
                "patientId" to entry.patientId,
                "patientName" to entry.patientName,
                "source" to entry.source.name,
                "status" to entry.status.name,
                "priorityFlag" to entry.priorityFlag,
                "checkedInAt" to entry.checkedInAt,
                "calledAt" to (entry.calledAt ?: 0L),
                "consultationStartedAt" to (entry.consultationStartedAt ?: 0L),
                "completedAt" to (entry.completedAt ?: 0L),
                "outcomeNotes" to (entry.outcomeNotes ?: "")
            )
            transaction.set(entryDocRef, data)
            currentToken
        }.await()

        Log.d(TAG, "Assigned authoritative token #$assignedToken to entry ${entry.id} for doctor ${entry.doctorId}")
        return entry.copy(tokenNumber = assignedToken, provisionalToken = false)
    }

    suspend fun uploadQueueEntry(entry: QueueEntry) {
        try {
            val data = hashMapOf(
                "id" to entry.id,
                "doctorId" to entry.doctorId,
                "doctorName" to entry.doctorName,
                "dateFormatted" to entry.dateFormatted,
                "tokenNumber" to entry.tokenNumber,
                "provisionalToken" to entry.provisionalToken,
                "appointmentId" to (entry.appointmentId ?: ""),
                "patientId" to entry.patientId,
                "patientName" to entry.patientName,
                "source" to entry.source.name,
                "status" to entry.status.name,
                "priorityFlag" to entry.priorityFlag,
                "checkedInAt" to entry.checkedInAt,
                "calledAt" to (entry.calledAt ?: 0L),
                "consultationStartedAt" to (entry.consultationStartedAt ?: 0L),
                "completedAt" to (entry.completedAt ?: 0L),
                "outcomeNotes" to (entry.outcomeNotes ?: "")
            )
            queueEntriesCollection.document(entry.id).set(data).await()
            Log.d(TAG, "Uploaded queue entry ${entry.id} (Status: ${entry.status.name})")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to upload queue entry: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadDoctorSlot(slot: DoctorDaySlotConfig) {
        try {
            val data = hashMapOf(
                "id" to slot.id,
                "doctorId" to slot.doctorId,
                "dateFormatted" to slot.dateFormatted,
                "startTime" to slot.startTime,
                "endTime" to slot.endTime,
                "capacity" to slot.capacity,
                "isWalkInOpen" to slot.isWalkInOpen
            )
            doctorSlotsCollection.document(slot.id).set(data).await()
            Log.d(TAG, "Uploaded doctor day slot: ${slot.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to upload doctor day slot: ${e.message}", e)
            throw e
        }
    }

    fun observeDoctorQueueStream(doctorId: String, date: String): Flow<List<QueueEntry>> = callbackFlow {
        val listener = queueEntriesCollection
            .whereEqualTo("doctorId", doctorId)
            .whereEqualTo("dateFormatted", date)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Doctor queue snapshot transient error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        QueueEntry(
                            id = doc.getString("id") ?: doc.id,
                            doctorId = doc.getString("doctorId") ?: "",
                            doctorName = doc.getString("doctorName") ?: "",
                            dateFormatted = doc.getString("dateFormatted") ?: "",
                            tokenNumber = doc.getLong("tokenNumber")?.toInt() ?: 0,
                            provisionalToken = doc.getBoolean("provisionalToken") ?: false,
                            appointmentId = doc.getString("appointmentId")?.ifEmpty { null },
                            patientId = doc.getString("patientId") ?: "",
                            patientName = doc.getString("patientName") ?: "",
                            source = QueueEntrySource.valueOf(doc.getString("source") ?: QueueEntrySource.WALK_IN.name),
                            status = QueueEntryStatus.valueOf(doc.getString("status") ?: QueueEntryStatus.WAITING.name),
                            priorityFlag = doc.getBoolean("priorityFlag") ?: false,
                            checkedInAt = doc.getLong("checkedInAt") ?: 0L,
                            calledAt = doc.getLong("calledAt")?.takeIf { it > 0L },
                            consultationStartedAt = doc.getLong("consultationStartedAt")?.takeIf { it > 0L },
                            completedAt = doc.getLong("completedAt")?.takeIf { it > 0L },
                            outcomeNotes = doc.getString("outcomeNotes")?.ifEmpty { null },
                            isPendingSync = false
                        )
                    } catch (e: Exception) { null }
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    fun observePatientQueueEntryStream(patientId: String, date: String): Flow<QueueEntry?> = callbackFlow {
        val listener = queueEntriesCollection
            .whereEqualTo("patientId", patientId)
            .whereEqualTo("dateFormatted", date)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Patient queue snapshot transient error: ${error.message}")
                    return@addSnapshotListener
                }
                val doc = snapshot?.documents?.firstOrNull()
                val entry = if (doc != null) {
                    try {
                        QueueEntry(
                            id = doc.getString("id") ?: doc.id,
                            doctorId = doc.getString("doctorId") ?: "",
                            doctorName = doc.getString("doctorName") ?: "",
                            dateFormatted = doc.getString("dateFormatted") ?: "",
                            tokenNumber = doc.getLong("tokenNumber")?.toInt() ?: 0,
                            provisionalToken = doc.getBoolean("provisionalToken") ?: false,
                            appointmentId = doc.getString("appointmentId")?.ifEmpty { null },
                            patientId = doc.getString("patientId") ?: "",
                            patientName = doc.getString("patientName") ?: "",
                            source = QueueEntrySource.valueOf(doc.getString("source") ?: QueueEntrySource.WALK_IN.name),
                            status = QueueEntryStatus.valueOf(doc.getString("status") ?: QueueEntryStatus.WAITING.name),
                            priorityFlag = doc.getBoolean("priorityFlag") ?: false,
                            checkedInAt = doc.getLong("checkedInAt") ?: 0L,
                            calledAt = doc.getLong("calledAt")?.takeIf { it > 0L },
                            consultationStartedAt = doc.getLong("consultationStartedAt")?.takeIf { it > 0L },
                            completedAt = doc.getLong("completedAt")?.takeIf { it > 0L },
                            outcomeNotes = doc.getString("outcomeNotes")?.ifEmpty { null },
                            isPendingSync = false
                        )
                    } catch (e: Exception) { null }
                } else null
                trySend(entry)
            }
        awaitClose { listener.remove() }
    }

    fun observeDoctorSlotsStream(doctorId: String, date: String): Flow<List<DoctorDaySlotConfig>> = callbackFlow {
        val listener = doctorSlotsCollection
            .whereEqualTo("doctorId", doctorId)
            .whereEqualTo("dateFormatted", date)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Doctor slots snapshot transient error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        DoctorDaySlotConfig(
                            id = doc.getString("id") ?: doc.id,
                            doctorId = doc.getString("doctorId") ?: "",
                            dateFormatted = doc.getString("dateFormatted") ?: "",
                            startTime = doc.getString("startTime") ?: "",
                            endTime = doc.getString("endTime") ?: "",
                            capacity = doc.getLong("capacity")?.toInt() ?: 20,
                            isWalkInOpen = doc.getBoolean("isWalkInOpen") ?: true
                        )
                    } catch (e: Exception) { null }
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    fun observeAllQueueEntriesForDateStream(date: String): Flow<List<QueueEntry>> = callbackFlow {
        val listener = queueEntriesCollection
            .whereEqualTo("dateFormatted", date)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "All queue entries snapshot transient error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        QueueEntry(
                            id = doc.getString("id") ?: doc.id,
                            doctorId = doc.getString("doctorId") ?: "",
                            doctorName = doc.getString("doctorName") ?: "",
                            dateFormatted = doc.getString("dateFormatted") ?: "",
                            tokenNumber = doc.getLong("tokenNumber")?.toInt() ?: 0,
                            provisionalToken = doc.getBoolean("provisionalToken") ?: false,
                            appointmentId = doc.getString("appointmentId")?.ifEmpty { null },
                            patientId = doc.getString("patientId") ?: "",
                            patientName = doc.getString("patientName") ?: "",
                            source = QueueEntrySource.valueOf(doc.getString("source") ?: QueueEntrySource.WALK_IN.name),
                            status = QueueEntryStatus.valueOf(doc.getString("status") ?: QueueEntryStatus.WAITING.name),
                            priorityFlag = doc.getBoolean("priorityFlag") ?: false,
                            checkedInAt = doc.getLong("checkedInAt") ?: 0L,
                            calledAt = doc.getLong("calledAt")?.takeIf { it > 0L },
                            consultationStartedAt = doc.getLong("consultationStartedAt")?.takeIf { it > 0L },
                            completedAt = doc.getLong("completedAt")?.takeIf { it > 0L },
                            outcomeNotes = doc.getString("outcomeNotes")?.ifEmpty { null },
                            isPendingSync = false
                        )
                    } catch (e: Exception) { null }
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }
}
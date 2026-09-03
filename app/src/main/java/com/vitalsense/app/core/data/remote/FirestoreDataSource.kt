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
                "syncState" to "SYNCED",
                "serverVersion" to 0L
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

    suspend fun uploadDepartment(department: Department) {
        try {
            val data = hashMapOf(
                "id" to department.id,
                "name" to department.name,
                "code" to department.code,
                "emoji" to department.emoji,
                "type" to department.type.name,
                "colorHex" to department.colorHex,
                "headDoctorId" to (department.headDoctorId ?: ""),
                "headDoctorName" to (department.headDoctorName ?: ""),
                "isActive" to department.isActive,
                "availableDoctorCount" to department.availableDoctorCount,
                "pendingReferralCount" to department.pendingReferralCount,
                "description" to department.description,
                "operatingHours" to department.operatingHours,
                "location" to department.location,
                "syncState" to "SYNCED",
                "serverVersion" to 0L
            )
            firestore.collection("departments").document(department.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded department: ${department.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload department: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadReferral(referral: Referral) {
        try {
            val data = hashMapOf(
                "id" to referral.id,
                "caseId" to referral.caseId,
                "patientId" to referral.patientId,
                "patientName" to referral.patientName,
                "fromDoctorId" to referral.fromDoctorId,
                "fromDoctorName" to referral.fromDoctorName,
                "fromDepartmentId" to referral.fromDepartmentId,
                "fromDepartmentName" to referral.fromDepartmentName,
                "toDepartmentId" to referral.toDepartmentId,
                "toDepartmentName" to referral.toDepartmentName,
                "toDoctorId" to (referral.toDoctorId ?: ""),
                "toDoctorName" to (referral.toDoctorName ?: ""),
                "referralType" to referral.referralType.name,
                "urgency" to referral.urgency.name,
                "reason" to referral.reason,
                "clinicalNotes" to referral.clinicalNotes,
                "clinicalHistory" to referral.clinicalHistory,
                "status" to referral.status.name,
                "acceptedByDoctorId" to (referral.acceptedByDoctorId ?: ""),
                "acceptedByDoctorName" to (referral.acceptedByDoctorName ?: ""),
                "acceptedAt" to (referral.acceptedAt ?: 0L),
                "serviceReportText" to (referral.serviceReportText ?: ""),
                "serviceReportAttachmentPath" to (referral.serviceReportAttachmentPath ?: ""),
                "serviceReportAttachmentUrl" to (referral.serviceReportAttachmentUrl ?: ""),
                "serviceReportTimestamp" to (referral.serviceReportTimestamp ?: 0L),
                "parentReferralId" to (referral.parentReferralId ?: ""),
                "referralChainIndex" to referral.referralChainIndex,
                "createdAt" to referral.createdAt,
                "updatedAt" to referral.updatedAt,
                "completedAt" to (referral.completedAt ?: 0L),
                "syncState" to "SYNCED",
                "serverVersion" to 0L
            )
            firestore.collection("referrals").document(referral.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded referral: ${referral.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload referral: ${e.message}", e)
            throw e
        }
    }

    // --- REAL-TIME LISTENERS (Reads) ---

    fun getConditionRecordsStream(): Flow<List<ConditionRecord>> = callbackFlow {
        val listener = conditionsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Condition records stream error: ${error.message}")
                close(error)
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
                            syncState = com.vitalsense.app.core.data.model.SyncState.SYNCED,
                            serverVersion = 0L
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
                Log.w(TAG, "Broadcast notices stream error: ${error.message}")
                close(error)
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

    fun getDepartmentsStream(): Flow<List<Department>> = callbackFlow {
        val listener = firestore.collection("departments").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Departments stream error: ${error.message}")
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    try {
                        Department(
                            id = doc.getString("id") ?: doc.id,
                            name = doc.getString("name") ?: "",
                            code = doc.getString("code") ?: "",
                            emoji = doc.getString("emoji") ?: "",
                            type = runCatching { DepartmentType.valueOf(doc.getString("type") ?: "CLINICAL") }.getOrDefault(DepartmentType.CLINICAL),
                            colorHex = doc.getLong("colorHex") ?: 0xFFE8EB7D,
                            headDoctorId = doc.getString("headDoctorId")?.takeIf { it.isNotBlank() },
                            headDoctorName = doc.getString("headDoctorName")?.takeIf { it.isNotBlank() },
                            isActive = doc.getBoolean("isActive") ?: true,
                            availableDoctorCount = doc.getLong("availableDoctorCount")?.toInt() ?: 0,
                            pendingReferralCount = doc.getLong("pendingReferralCount")?.toInt() ?: 0,
                            description = doc.getString("description") ?: "",
                            operatingHours = doc.getString("operatingHours") ?: "24x7",
                            location = doc.getString("location") ?: "",
                            syncState = SyncState.SYNCED,
                            serverVersion = doc.getLong("serverVersion") ?: 0L
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

    fun getReferralsStream(): Flow<List<Referral>> = callbackFlow {
        val listener = firestore.collection("referrals").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Referrals stream error: ${error.message}")
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    try {
                        Referral(
                            id = doc.getString("id") ?: doc.id,
                            caseId = doc.getString("caseId") ?: "",
                            patientId = doc.getString("patientId") ?: "",
                            patientName = doc.getString("patientName") ?: "",
                            fromDoctorId = doc.getString("fromDoctorId") ?: "",
                            fromDoctorName = doc.getString("fromDoctorName") ?: "",
                            fromDepartmentId = doc.getString("fromDepartmentId") ?: "",
                            fromDepartmentName = doc.getString("fromDepartmentName") ?: "",
                            toDepartmentId = doc.getString("toDepartmentId") ?: "",
                            toDepartmentName = doc.getString("toDepartmentName") ?: "",
                            toDoctorId = doc.getString("toDoctorId")?.takeIf { it.isNotBlank() },
                            toDoctorName = doc.getString("toDoctorName")?.takeIf { it.isNotBlank() },
                            referralType = runCatching { ReferralType.valueOf(doc.getString("referralType") ?: "CLINICAL") }.getOrDefault(ReferralType.CLINICAL),
                            urgency = runCatching { ReferralUrgency.valueOf(doc.getString("urgency") ?: "ROUTINE") }.getOrDefault(ReferralUrgency.ROUTINE),
                            reason = doc.getString("reason") ?: "",
                            clinicalNotes = doc.getString("clinicalNotes") ?: "",
                            clinicalHistory = doc.getString("clinicalHistory") ?: "",
                            status = runCatching { ReferralStatus.valueOf(doc.getString("status") ?: "PENDING") }.getOrDefault(ReferralStatus.PENDING),
                            acceptedByDoctorId = doc.getString("acceptedByDoctorId")?.takeIf { it.isNotBlank() },
                            acceptedByDoctorName = doc.getString("acceptedByDoctorName")?.takeIf { it.isNotBlank() },
                            acceptedAt = doc.getLong("acceptedAt")?.takeIf { it > 0 },
                            serviceReportText = doc.getString("serviceReportText")?.takeIf { it.isNotBlank() },
                            serviceReportAttachmentPath = doc.getString("serviceReportAttachmentPath")?.takeIf { it.isNotBlank() },
                            serviceReportAttachmentUrl = doc.getString("serviceReportAttachmentUrl")?.takeIf { it.isNotBlank() },
                            serviceReportTimestamp = doc.getLong("serviceReportTimestamp")?.takeIf { it > 0 },
                            parentReferralId = doc.getString("parentReferralId")?.takeIf { it.isNotBlank() },
                            referralChainIndex = doc.getLong("referralChainIndex")?.toInt() ?: 0,
                            createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis(),
                            updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis(),
                            completedAt = doc.getLong("completedAt")?.takeIf { it > 0 },
                            syncState = SyncState.SYNCED,
                            serverVersion = doc.getLong("serverVersion") ?: 0L
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
}
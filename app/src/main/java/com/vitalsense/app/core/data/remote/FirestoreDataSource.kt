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
            Log.d(TAG, "✅ Successfully uploaded medical history: ${entry.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload medical history: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadDailyRound(round: DailyRound) {
        try {
            val data = hashMapOf(
                "id" to round.id,
                "dateFormatted" to round.dateFormatted,
                "villageName" to round.villageName,
                "householdName" to round.householdName,
                "personName" to round.personName,
                "ashaWorkerId" to round.ashaWorkerId,
                "purpose" to round.purpose,
                "isPregnancyChecked" to round.isPregnancyChecked,
                "isChildHealthChecked" to round.isChildHealthChecked,
                "isImmunizationChecked" to round.isImmunizationChecked,
                "isMedicineGiven" to round.isMedicineGiven,
                "isCounsellingDone" to round.isCounsellingDone,
                "notes" to round.notes,
                "status" to round.status
            )
            firestore.collection("daily_rounds").document(round.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded daily_round: ${round.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload daily_round: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadImmunizationRecord(record: ImmunizationRecord) {
        try {
            val data = hashMapOf(
                "id" to record.id,
                "childName" to record.childName,
                "motherName" to record.motherName,
                "dobFormatted" to record.dobFormatted,
                "gender" to record.gender,
                "villageName" to record.villageName,
                "ashaWorkerId" to record.ashaWorkerId,
                "vaccines" to record.vaccines.map {
                    hashMapOf(
                        "name" to it.name,
                        "dueDateFormatted" to it.dueDateFormatted,
                        "status" to it.status,
                        "givenDateFormatted" to (it.givenDateFormatted ?: "")
                    )
                }
            )
            firestore.collection("immunization_records").document(record.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded immunization_record: ${record.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload immunization_record: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadAshaMedicine(medicine: AshaMedicine) {
        try {
            val data = hashMapOf(
                "id" to medicine.id,
                "ashaWorkerId" to medicine.ashaWorkerId,
                "medicineName" to medicine.medicineName,
                "availableQuantity" to medicine.availableQuantity,
                "unit" to medicine.unit,
                "minStockQuantity" to medicine.minStockQuantity,
                "expiryDateFormatted" to medicine.expiryDateFormatted,
                "lastRestockDateFormatted" to (medicine.lastRestockDateFormatted ?: "")
            )
            firestore.collection("asha_medicines").document(medicine.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded asha_medicine: ${medicine.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload asha_medicine: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadDispensaryItem(item: DispensaryItem) {
        try {
            val data = hashMapOf(
                "id" to item.id,
                "medicineName" to item.medicineName,
                "category" to item.category,
                "availableQuantity" to item.availableQuantity,
                "unit" to item.unit,
                "reorderThreshold" to item.reorderThreshold,
                "lastRestockDateFormatted" to (item.lastRestockDateFormatted ?: "")
            )
            firestore.collection("dispensary_stock").document(item.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded dispensary_stock: ${item.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload dispensary_stock: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadReferral(referral: Referral) {
        try {
            val data = hashMapOf(
                "id" to referral.id,
                "patientId" to referral.patientId,
                "patientName" to referral.patientName,
                "referringDoctorId" to referral.referringDoctorId,
                "referringDoctorName" to referral.referringDoctorName,
                "referringDoctorSpecialty" to referral.referringDoctorSpecialty,
                "targetDoctorId" to (referral.targetDoctorId ?: ""),
                "targetDoctorName" to (referral.targetDoctorName ?: ""),
                "targetSpecialty" to referral.targetSpecialty,
                "reason" to referral.reason,
                "clinicalQuestion" to referral.clinicalQuestion,
                "urgency" to referral.urgency.name,
                "attachedRecordIds" to referral.attachedRecordIds,
                "status" to referral.status.name,
                "declineReason" to (referral.declineReason ?: ""),
                "suggestedSpecialtyOrDoctor" to (referral.suggestedSpecialtyOrDoctor ?: ""),
                "infoRequestNote" to (referral.infoRequestNote ?: ""),
                "specialistFindings" to (referral.specialistFindings ?: ""),
                "specialistRecommendations" to (referral.specialistRecommendations ?: ""),
                "specialistFollowUpNeeded" to referral.specialistFollowUpNeeded,
                "createdAt" to referral.createdAt,
                "respondedAt" to (referral.respondedAt ?: 0L),
                "completedAt" to (referral.completedAt ?: 0L)
            )
            firestore.collection("referrals").document(referral.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded referral: ${referral.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload referral: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadLabReport(report: LabReport) {
        try {
            val data = hashMapOf(
                "id" to report.id,
                "patientId" to report.patientId,
                "patientName" to report.patientName,
                "testCategory" to report.testCategory,
                "doctorName" to report.doctorName,
                "dateFormatted" to report.dateFormatted,
                "items" to report.items.map {
                    hashMapOf(
                        "testName" to it.testName,
                        "resultValue" to it.resultValue,
                        "unit" to it.unit,
                        "referenceRange" to it.referenceRange,
                        "flag" to it.flag
                    )
                },
                "notes" to report.notes,
                "status" to report.status
            )
            firestore.collection("lab_reports").document(report.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded lab_report: ${report.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload lab_report: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadOpdToken(token: OpdToken) {
        try {
            val data = hashMapOf(
                "id" to token.id,
                "tokenNumber" to token.tokenNumber,
                "patientId" to token.patientId,
                "patientName" to token.patientName,
                "doctorName" to token.doctorName,
                "department" to token.department,
                "cabinNumber" to token.cabinNumber,
                "currentServingToken" to token.currentServingToken,
                "estimatedWaitMinutes" to token.estimatedWaitMinutes,
                "status" to token.status,
                "dateFormatted" to token.dateFormatted
            )
            firestore.collection("opd_tokens").document(token.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded opd_token: ${token.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload opd_token: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadMedicalCertificate(certificate: MedicalCertificate) {
        try {
            val data = hashMapOf(
                "id" to certificate.id,
                "certificateNumber" to certificate.certificateNumber,
                "patientId" to certificate.patientId,
                "patientName" to certificate.patientName,
                "patientAge" to certificate.patientAge,
                "patientGender" to certificate.patientGender,
                "doctorName" to certificate.doctorName,
                "doctorRegistrationNumber" to certificate.doctorRegistrationNumber,
                "diagnosis" to certificate.diagnosis,
                "restStartDate" to certificate.restStartDate,
                "restEndDate" to certificate.restEndDate,
                "fitDate" to certificate.fitDate,
                "certificateType" to certificate.certificateType,
                "issuedDateFormatted" to certificate.issuedDateFormatted
            )
            firestore.collection("medical_certificates").document(certificate.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded medical_certificate: ${certificate.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload medical_certificate: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadExternalReferral(referral: ExternalReferral) {
        try {
            val data = hashMapOf(
                "id" to referral.id,
                "referralLetterId" to referral.referralLetterId,
                "patientId" to referral.patientId,
                "patientName" to referral.patientName,
                "referringDoctorName" to referral.referringDoctorName,
                "empanelledHospitalName" to referral.empanelledHospitalName,
                "specialtyRequired" to referral.specialtyRequired,
                "clinicalSummary" to referral.clinicalSummary,
                "isCashlessApproved" to referral.isCashlessApproved,
                "ambulanceRequisitioned" to referral.ambulanceRequisitioned,
                "issuedDate" to referral.issuedDate,
                "status" to referral.status
            )
            firestore.collection("external_referrals").document(referral.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded external_referral: ${referral.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload external_referral: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadCallLog(callLog: CallLog) {
        try {
            val data = hashMapOf(
                "id" to callLog.id,
                "callType" to callLog.callType.name,
                "callMode" to callLog.callMode,
                "patientId" to callLog.patientId,
                "patientName" to callLog.patientName,
                "doctorId" to callLog.doctorId,
                "doctorName" to callLog.doctorName,
                "timestamp" to callLog.timestamp,
                "durationSeconds" to callLog.durationSeconds,
                "outcome" to callLog.outcome.name,
                "outcomeNotes" to (callLog.outcomeNotes ?: "")
            )
            firestore.collection("call_logs").document(callLog.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded call_log: ${callLog.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload call_log: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadAuditLog(auditLog: AuditLog) {
        try {
            val data = hashMapOf(
                "id" to auditLog.id,
                "timestamp" to auditLog.timestamp,
                "actorId" to auditLog.actorId,
                "actorRole" to auditLog.actorRole,
                "action" to auditLog.action,
                "resourceId" to (auditLog.resourceId ?: ""),
                "resourceType" to (auditLog.resourceType ?: ""),
                "details" to (auditLog.details ?: ""),
                "isSynced" to true
            )
            firestore.collection("audit_logs").document(auditLog.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded audit_log: ${auditLog.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload audit_log: ${e.message}", e)
            throw e
        }
    }

    suspend fun uploadDiseaseTrend(trend: DiseaseTrendRecord) {
        try {
            val data = hashMapOf(
                "id" to trend.id,
                "villageName" to trend.villageName,
                "diseaseName" to trend.diseaseName,
                "caseCount" to trend.caseCount,
                "dateFormatted" to trend.dateFormatted,
                "severity" to (trend.severity ?: "")
            )
            firestore.collection("disease_trends").document(trend.id).set(data).await()
            Log.d(TAG, "✅ Successfully uploaded disease_trend: ${trend.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to upload disease_trend: ${e.message}", e)
            throw e
        }
    }

    // --- REAL-TIME LISTENERS (Reads) ---

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
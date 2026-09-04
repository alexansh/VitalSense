package com.vitalsense.app.core.referral

import com.vitalsense.app.core.data.local.entity.ReferralEntity
import com.vitalsense.app.core.data.local.entity.toEntity
import com.vitalsense.app.core.data.model.*
import org.junit.Assert.*
import org.junit.Test

class ReferralSystemTest {

    @Test
    fun testReferralCreationAndEntityMapping() {
        val referral = Referral(
            id = "ref_test_01",
            patientId = "pat_ramesh",
            patientName = "Ramesh Kumar",
            referringUserId = "doc_rajesh",
            referringUserName = "Dr. Rajesh Varma",
            referringUserSpecialty = "General Physician",
            targetDoctorId = "doc_anita_cardio",
            targetDoctorName = "Dr. Anita Sharma",
            targetSpecialty = "Cardiology",
            reason = "Chest pain and abnormal rhythm",
            clinicalQuestion = "Please evaluate for ischemia and advise on 2D ECHO",
            urgency = ReferralUrgency.URGENT,
            attachedRecordIds = listOf("cond_1", "rx_1"),
            status = ReferralStatus.SENT,
            createdAt = 1000L
        )

        val entity: ReferralEntity = referral.toEntity()
        assertEquals("ref_test_01", entity.id)
        assertEquals("pat_ramesh", entity.patientId)
        assertEquals("Cardiology", entity.targetSpecialty)
        assertEquals(ReferralUrgency.URGENT, entity.urgency)
        assertEquals(ReferralStatus.SENT, entity.status)
        assertEquals(2, entity.attachedRecordIds.size)

        val mappedBack: Referral = entity.toModel()
        assertEquals(referral.id, mappedBack.id)
        assertEquals(referral.patientName, mappedBack.patientName)
        assertEquals(referral.clinicalQuestion, mappedBack.clinicalQuestion)
        assertEquals(referral.urgency, mappedBack.urgency)
        assertEquals(referral.status, mappedBack.status)
        assertEquals(referral.attachedRecordIds, mappedBack.attachedRecordIds)
    }

    @Test
    fun testSpecialistAcceptanceWorkflow() {
        val referral = Referral(
            id = "ref_test_02",
            patientId = "pat_anita",
            patientName = "Anita Sharma",
            referringUserId = "doc_rajesh",
            referringUserName = "Dr. Rajesh Varma",
            referringUserSpecialty = "General Physician",
            targetDoctorId = "doc_sunita_gynae",
            targetDoctorName = "Dr. Sunita Rao",
            targetSpecialty = "Gynecologist & Maternal Care",
            reason = "Gestational edema and borderline Hb",
            clinicalQuestion = "Review nutritional titration",
            urgency = ReferralUrgency.ROUTINE,
            status = ReferralStatus.SENT,
            createdAt = 2000L
        )

        assertEquals(ReferralStatus.SENT, referral.status)
        assertNull(referral.respondedAt)

        val respondedTime = 2500L
        val acceptedReferral = referral.copy(
            status = ReferralStatus.ACCEPTED,
            respondedAt = respondedTime
        )

        assertEquals(ReferralStatus.ACCEPTED, acceptedReferral.status)
        assertEquals(respondedTime, acceptedReferral.respondedAt)
    }

    @Test
    fun testSpecialistDeclineWorkflowWithRationale() {
        val referral = Referral(
            id = "ref_test_03",
            patientId = "pat_vikram",
            patientName = "Vikram Singh",
            referringUserId = "doc_rajesh",
            referringUserName = "Dr. Rajesh Varma",
            referringUserSpecialty = "General Physician",
            targetSpecialty = "Orthopedic Surgeon",
            reason = "Chronic joint pain",
            clinicalQuestion = "Evaluate knee replacement",
            urgency = ReferralUrgency.ROUTINE,
            status = ReferralStatus.SENT
        )

        val declinedReferral = referral.copy(
            status = ReferralStatus.DECLINED,
            declineReason = "Department surgical capacity reached for the month",
            suggestedSpecialtyOrDoctor = "Dr. Meera Nambiar / Rheumatology",
            respondedAt = 3000L
        )

        assertEquals(ReferralStatus.DECLINED, declinedReferral.status)
        assertEquals("Department surgical capacity reached for the month", declinedReferral.declineReason)
        assertEquals("Dr. Meera Nambiar / Rheumatology", declinedReferral.suggestedSpecialtyOrDoctor)
    }

    @Test
    fun testSpecialistRequestMoreInformationWorkflow() {
        val referral = Referral(
            id = "ref_test_04",
            patientId = "pat_ramesh",
            patientName = "Ramesh Kumar",
            referringUserId = "doc_rajesh",
            referringUserName = "Dr. Rajesh Varma",
            referringUserSpecialty = "General Physician",
            targetSpecialty = "Cardiology",
            reason = "Arrhythmia",
            clinicalQuestion = "Advise on beta blocker",
            urgency = ReferralUrgency.URGENT,
            status = ReferralStatus.SENT
        )

        val infoRequested = referral.copy(
            status = ReferralStatus.INFO_REQUESTED,
            infoRequestNote = "Please upload 12-lead ECG strip before cardiac consultation",
            respondedAt = 4000L
        )

        assertEquals(ReferralStatus.INFO_REQUESTED, infoRequested.status)
        assertEquals("Please upload 12-lead ECG strip before cardiac consultation", infoRequested.infoRequestNote)
    }

    @Test
    fun testClosedLoopSpecialistFindingsWorkflow() {
        val acceptedReferral = Referral(
            id = "ref_test_05",
            patientId = "pat_vikram",
            patientName = "Vikram Singh",
            referringUserId = "doc_rajesh",
            referringUserName = "Dr. Rajesh Varma",
            referringUserSpecialty = "General Physician",
            targetDoctorId = "doc_arun_ortho",
            targetDoctorName = "Dr. Arun Kumar",
            targetSpecialty = "Orthopedic Surgeon",
            reason = "Bilateral knee osteoarthritis",
            clinicalQuestion = "Evaluate suitability for steroid injection",
            urgency = ReferralUrgency.ROUTINE,
            status = ReferralStatus.ACCEPTED
        )

        val completedTime = 5000L
        val closedLoopReferral = acceptedReferral.copy(
            status = ReferralStatus.COMPLETED,
            specialistFindings = "Confirmed Grade IV Osteoarthritis with right effusion",
            specialistRecommendations = "Triamcinolone 40mg injection administered. Advised quad exercises, re-evaluate in 4 weeks.",
            specialistFollowUpNeeded = true,
            completedAt = completedTime
        )

        assertEquals(ReferralStatus.COMPLETED, closedLoopReferral.status)
        assertEquals("Confirmed Grade IV Osteoarthritis with right effusion", closedLoopReferral.specialistFindings)
        assertTrue(closedLoopReferral.specialistFollowUpNeeded)
        assertEquals(completedTime, closedLoopReferral.completedAt)
    }

    @Test
    fun testUrgencySortingPriority() {
        val routineRef = Referral(
            id = "ref_routine",
            patientId = "p1",
            patientName = "Patient 1",
            referringUserId = "d1",
            referringUserName = "Dr. 1",
            referringUserSpecialty = "GP",
            targetSpecialty = "Dermatologist",
            reason = "Rash",
            clinicalQuestion = "Confirm eczema",
            urgency = ReferralUrgency.ROUTINE,
            createdAt = 1000L
        )

        val urgentRef = Referral(
            id = "ref_urgent",
            patientId = "p2",
            patientName = "Patient 2",
            referringUserId = "d1",
            referringUserName = "Dr. 1",
            referringUserSpecialty = "GP",
            targetSpecialty = "Cardiology",
            reason = "Hypertension spike",
            clinicalQuestion = "Titrate ACEi",
            urgency = ReferralUrgency.URGENT,
            createdAt = 500L
        )

        val emergencyRef = Referral(
            id = "ref_emergency",
            patientId = "p3",
            patientName = "Patient 3",
            referringUserId = "d1",
            referringUserName = "Dr. 1",
            referringUserSpecialty = "GP",
            targetSpecialty = "Cardiology",
            reason = "Acute crushing chest pain",
            clinicalQuestion = "Emergency consult for STEMI suspicion",
            urgency = ReferralUrgency.EMERGENCY,
            createdAt = 100L
        )

        val unsortedList = listOf(routineRef, urgentRef, emergencyRef)
        val sortedList = unsortedList.sortedWith(
            compareBy<Referral> {
                when (it.urgency) {
                    ReferralUrgency.EMERGENCY -> 1
                    ReferralUrgency.URGENT -> 2
                    ReferralUrgency.ROUTINE -> 3
                }
            }.thenByDescending { it.createdAt }
        )

        // Emergency MUST be first, Urgent second, Routine last
        assertEquals(ReferralUrgency.EMERGENCY, sortedList[0].urgency)
        assertEquals("ref_emergency", sortedList[0].id)

        assertEquals(ReferralUrgency.URGENT, sortedList[1].urgency)
        assertEquals("ref_urgent", sortedList[1].id)

        assertEquals(ReferralUrgency.ROUTINE, sortedList[2].urgency)
        assertEquals("ref_routine", sortedList[2].id)
    }

    @Test
    fun testSpecialistDirectoryProvider() {
        val cardioDocs = SpecialistDirectoryProvider.getSpecialistsForSpecialty("Cardiology")
        assertTrue("Cardiologists should be available", cardioDocs.isNotEmpty())
        assertEquals("Cardiologist", cardioDocs.first().specialty.displayName)

        val dermaDocs = SpecialistDirectoryProvider.getSpecialistsForSpecialty("Dermatologist")
        assertTrue("Dermatologists should be available", dermaDocs.isNotEmpty())
        assertEquals("Dermatologist", dermaDocs.first().specialty.displayName)

        val orthoDocs = SpecialistDirectoryProvider.getSpecialistsForSpecialty("Orthopedic Surgeon")
        assertTrue("Orthopedics should be available", orthoDocs.isNotEmpty())
        assertEquals("Orthopedic Surgeon", orthoDocs.first().specialty.displayName)
    }
}

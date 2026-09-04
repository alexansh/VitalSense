package com.vitalsense.app.core.data.local.seed

import com.google.gson.Gson
import com.vitalsense.app.core.data.local.entity.*
import com.vitalsense.app.core.data.model.*

object SeedDataProvider {
    private val gson = Gson()

    val initialVillages = listOf(
        Village(
            id = "v_sundarpura",
            name = "Sundarpura",
            district = "Rampur",
            state = "Uttar Pradesh",
            population = 1450,
            latitude = 26.8467,
            longitude = 80.9462,
            activeCases = 14,
            highRiskCount = 3
        ),
        Village(
            id = "v_kalyanpur",
            name = "Kalyanpur",
            district = "Rampur",
            state = "Uttar Pradesh",
            population = 2100,
            latitude = 26.8821,
            longitude = 80.9812,
            activeCases = 22,
            highRiskCount = 6
        ),
        Village(
            id = "v_bhimnagar",
            name = "Bhimnagar",
            district = "Rampur",
            state = "Uttar Pradesh",
            population = 980,
            latitude = 26.8150,
            longitude = 80.9120,
            activeCases = 8,
            highRiskCount = 1
        )
    )

    val initialAshaWorkers = listOf(
        AshaWorker(
            id = "asha_priya",
            name = "Priya Devi",
            ashaUniqueId = "ASHA-7701",
            phone = "+91 98765 43210",
            assignedVillages = listOf("Sundarpura", "Bhimnagar"),
            activePatientCount = 18,
            alertCount = 3
        ),
        AshaWorker(
            id = "asha_sunita",
            name = "Sunita Sharma",
            ashaUniqueId = "ASHA-8842",
            phone = "+91 98765 12345",
            assignedVillages = listOf("Kalyanpur"),
            activePatientCount = 24,
            alertCount = 5
        )
    )

    val initialDoctors = listOf(
        Doctor(
            id = "doc_rajesh",
            name = "Dr. Rajesh Varma",
            specialty = DoctorSpecialty.GENERAL_PHYSICIAN,
            qualification = "MBBS, MD (Medicine)",
            hospitalName = "Rampur Civil Hospital",
            distanceKm = 4.2,
            phone = "+91 94150 11223",
            availableDays = "Mon - Sat (9:00 AM - 4:00 PM)"
        ),
        Doctor(
            id = "doc_ananya",
            name = "Dr. Ananya Sen",
            specialty = DoctorSpecialty.PSYCHOLOGIST,
            qualification = "Ph.D. Clinical Psychology",
            hospitalName = "District Community Wellness Center",
            distanceKm = 6.5,
            phone = "+91 94150 99887",
            availableDays = "Mon - Fri (10:00 AM - 3:00 PM)"
        ),
        Doctor(
            id = "doc_ayushman",
            name = "Dr. Ayushman Dev Singh",
            specialty = DoctorSpecialty.MAXILLOFACIAL_RECONSTRUCTIVE_SURGEON,
            qualification = "Orthognathic, Oncogenic, Trauma, Oral & Maxillofacial, and Cosmetic Surgeon | MS, MCh, FICS",
            hospitalName = "District Apex Trauma & Maxillofacial Reconstructive Center",
            distanceKm = 5.8,
            phone = "+91 94150 77665",
            availableDays = "Mon - Sat (9:00 AM - 5:00 PM)"
        )
    )

    val initialPatients = listOf(
        Patient(
            id = "pat_ramesh",
            name = "Ramesh Kumar",
            age = 42,
            gender = "Male",
            phone = "+91 98111 22334",
            villageId = "v_sundarpura",
            villageName = "Sundarpura",
            ashaWorkerId = "asha_priya",
            ashaWorkerName = "Priya Devi",
            currentRiskLevel = SeverityLevel.SEVERE,
            lastCondition = "Severe Chest Congestion & High Spiking Fever (103°F)",
            lastVisitDate = "2026-08-10",
            nextAppointmentDate = "2026-08-18 (10:30 AM)",
            emergencyContact = "+91 98111 99999 (Brother - Suresh)"
        ),
        Patient(
            id = "pat_anita",
            name = "Anita Sharma",
            age = 28,
            gender = "Female",
            phone = "+91 98222 33445",
            villageId = "v_sundarpura",
            villageName = "Sundarpura",
            ashaWorkerId = "asha_priya",
            ashaWorkerName = "Priya Devi",
            currentRiskLevel = SeverityLevel.MODERATE,
            lastCondition = "2nd Trimester Routine Prenatal Care & Mild Anemia",
            lastVisitDate = "2026-08-05",
            nextAppointmentDate = "2026-08-20 (11:00 AM)",
            emergencyContact = "+91 98222 88888 (Husband - Manoj)"
        ),
        Patient(
            id = "pat_vikram",
            name = "Vikram Singh",
            age = 65,
            gender = "Male",
            phone = "+91 98333 44556",
            villageId = "v_kalyanpur",
            villageName = "Kalyanpur",
            ashaWorkerId = "asha_sunita",
            ashaWorkerName = "Sunita Sharma",
            currentRiskLevel = SeverityLevel.HIGH,
            lastCondition = "Hypertension (160/95) & Chronic Dizziness with Fatigue",
            lastVisitDate = "2026-08-12",
            nextAppointmentDate = "2026-08-16 (02:00 PM)",
            emergencyContact = "+91 98333 77777 (Son - Rahul)"
        ),
        Patient(
            id = "pat_meena",
            name = "Meena Patel",
            age = 19,
            gender = "Female",
            phone = "+91 98444 55667",
            villageId = "v_bhimnagar",
            villageName = "Bhimnagar",
            ashaWorkerId = "asha_priya",
            ashaWorkerName = "Priya Devi",
            currentRiskLevel = SeverityLevel.LOW,
            lastCondition = "Dietary Guidance & Iron Supplements Check",
            lastVisitDate = "2026-07-28",
            nextAppointmentDate = null,
            emergencyContact = "+91 98444 66666 (Mother - Shakuntala)"
        ),
        Patient(
            id = "pat_suresh",
            name = "Suresh Yadav",
            age = 35,
            gender = "Male",
            phone = "+91 98555 66778",
            villageId = "v_kalyanpur",
            villageName = "Kalyanpur",
            ashaWorkerId = "asha_sunita",
            ashaWorkerName = "Sunita Sharma",
            currentRiskLevel = SeverityLevel.MODERATE,
            lastCondition = "Chronic Agricultural Stress & Severe Sleep Disruption",
            lastVisitDate = "2026-08-08",
            nextAppointmentDate = "2026-08-17 (03:30 PM)",
            emergencyContact = "+91 98555 55555 (Wife - Geeta)"
        )
    )

    val initialConditionRecords = listOf(
        ConditionRecord(
            id = "cond_1",
            patientId = "pat_ramesh",
            patientName = "Ramesh Kumar",
            villageId = "v_sundarpura",
            villageName = "Sundarpura",
            category = ConditionCategory.GENERAL_MEDICINE,
            severity = SeverityLevel.SEVERE,
            requestedDoctorType = DoctorSpecialty.GENERAL_PHYSICIAN,
            notes = "Patient experiencing severe coughing with yellowish phlegm, 103°F fever for 3 days, and shortness of breath.",
            timestamp = System.currentTimeMillis() - 86400000L * 2,
            ashaProxyLogged = false
        ),
        ConditionRecord(
            id = "cond_2",
            patientId = "pat_anita",
            patientName = "Anita Sharma",
            villageId = "v_sundarpura",
            villageName = "Sundarpura",
            category = ConditionCategory.MATERNAL_HEALTH,
            severity = SeverityLevel.MODERATE,
            requestedDoctorType = DoctorSpecialty.GYNECOLOGIST,
            notes = "Week 22 pregnancy checkup. Mild fatigue and leg cramps reported. Hb level 10.2.",
            timestamp = System.currentTimeMillis() - 86400000L * 4,
            ashaProxyLogged = true
        ),
        ConditionRecord(
            id = "cond_3",
            patientId = "pat_vikram",
            patientName = "Vikram Singh",
            villageId = "v_kalyanpur",
            villageName = "Kalyanpur",
            category = ConditionCategory.GENERAL_MEDICINE,
            severity = SeverityLevel.HIGH,
            requestedDoctorType = DoctorSpecialty.GENERAL_PHYSICIAN,
            notes = "Blood pressure spiked to 160/95. Persistent morning headache and blurry vision on standing.",
            timestamp = System.currentTimeMillis() - 86400000L * 1,
            ashaProxyLogged = true
        ),
        ConditionRecord(
            id = "cond_4",
            patientId = "pat_suresh",
            patientName = "Suresh Yadav",
            villageId = "v_kalyanpur",
            villageName = "Kalyanpur",
            category = ConditionCategory.MENTAL_HEALTH,
            severity = SeverityLevel.MODERATE,
            requestedDoctorType = DoctorSpecialty.PSYCHOLOGIST,
            notes = "Crop failure stress resulting in insomnia, anxiety attacks, and loss of appetite.",
            timestamp = System.currentTimeMillis() - 86400000L * 3,
            ashaProxyLogged = false
        ),
        ConditionRecord(
            id = "cond_5",
            patientId = "pat_vikram",
            patientName = "Vikram Singh",
            villageId = "v_kalyanpur",
            villageName = "Kalyanpur",
            category = ConditionCategory.GENERAL_MEDICINE,
            severity = SeverityLevel.SEVERE,
            requestedDoctorType = DoctorSpecialty.ORTHOPLASTIC_SURGEON,
            notes = "Post-traumatic lower extremity crush injury with extensive soft-tissue defect and compound fracture. Urgent orthoplastic reconstructive evaluation requested.",
            timestamp = System.currentTimeMillis() - 86400000L * 1,
            ashaProxyLogged = true
        )
    )

    val initialPrescriptions = listOf(
        Prescription(
            id = "rx_1",
            patientId = "pat_ramesh",
            patientName = "Ramesh Kumar",
            doctorId = "doc_rajesh",
            doctorName = "Dr. Rajesh Varma",
            doctorSpecialty = "General Physician",
            timestamp = System.currentTimeMillis() - 86400000L,
            dateFormatted = "13 Aug 2026",
            medicines = listOf(
                PrescribedMedicine("Amoxicillin 500mg", "1 capsule", "3 times daily after meals", "5 days", 15),
                PrescribedMedicine("Paracetamol 650mg", "1 tablet", "SOS (if fever > 100°F)", "3 days", 6),
                PrescribedMedicine("Ambroxol Cough Syrup", "10 ml", "Twice daily after food", "5 days", 1)
            ),
            instructions = "Drink lukewarm water, avoid heavy physical labor, and review at PHC if breathing difficulty worsens.",
            isOcrExtracted = false
        ),
        Prescription(
            id = "rx_2",
            patientId = "pat_anita",
            patientName = "Anita Sharma",
            doctorId = "doc_rajesh",
            doctorName = "Dr. Rajesh Varma",
            doctorSpecialty = "General Physician",
            timestamp = System.currentTimeMillis() - 86400000L * 5,
            dateFormatted = "09 Aug 2026",
            medicines = listOf(
                PrescribedMedicine("Iron Folic Acid (IFA) Tablets", "1 tablet", "Once daily after lunch", "30 days", 30),
                PrescribedMedicine("Calcium 500mg + Vit D3", "1 tablet", "Once daily after dinner", "30 days", 30)
            ),
            instructions = "Do not take Iron and Calcium tablets together. Maintain high green leafy vegetable diet.",
            isOcrExtracted = true
        )
    )

    val initialAppointments = listOf(
        Appointment(
            id = "apt_1",
            patientId = "pat_ramesh",
            patientName = "Ramesh Kumar",
            doctorId = "doc_rajesh",
            doctorName = "Dr. Rajesh Varma",
            doctorSpecialty = "General Physician",
            dateFormatted = "18 Aug 2026",
            timeSlot = "10:30 AM",
            status = "Confirmed",
            proposedBy = UserRole.DOCTOR
        ),
        Appointment(
            id = "apt_2",
            patientId = "pat_vikram",
            patientName = "Vikram Singh",
            doctorId = "doc_rajesh",
            doctorName = "Dr. Rajesh Varma",
            doctorSpecialty = "General Physician",
            dateFormatted = "16 Aug 2026",
            timeSlot = "02:00 PM",
            status = "Confirmed",
            proposedBy = UserRole.PATIENT
        ),
        Appointment(
            id = "apt_3",
            patientId = "pat_suresh",
            patientName = "Suresh Yadav",
            doctorId = "doc_ananya",
            doctorName = "Dr. Ananya Sen",
            doctorSpecialty = "Psychologist & Mental Health",
            dateFormatted = "17 Aug 2026",
            timeSlot = "03:30 PM",
            status = "Pending Confirmation",
            proposedBy = UserRole.PATIENT
        ),
        Appointment(
            id = "apt_4",
            patientId = "pat_vikram",
            patientName = "Vikram Singh",
            doctorId = "doc_ayushman",
            doctorName = "Dr. Ayushman Dev Singh",
            doctorSpecialty = "Orthoplastic Surgeon",
            dateFormatted = "19 Aug 2026",
            timeSlot = "11:00 AM",
            status = "Confirmed",
            proposedBy = UserRole.DOCTOR
        )
    )

    val initialDispensaryItems = listOf(
        DispensaryItem("disp_1", "Paracetamol 650mg", "Analgesic / Antipyretic", 450, "tablets", 100, "10 Aug 2026"),
        DispensaryItem("disp_2", "Amoxicillin 500mg", "Antibiotic", 180, "capsules", 50, "12 Aug 2026"),
        DispensaryItem("disp_3", "Oral Rehydration Salts (ORS)", "Hydration", 320, "packets", 80, "01 Aug 2026"),
        DispensaryItem("disp_4", "Iron & Folic Acid (IFA)", "Maternal / Anemia", 500, "tablets", 150, "15 Jul 2026"),
        DispensaryItem("disp_5", "Cetirizine 10mg", "Antihistamine", 220, "tablets", 60, "05 Aug 2026"),
        DispensaryItem("disp_6", "Amlodipine 5mg", "Hypertension", 35, "tablets", 50, "01 Jun 2026"), // Low stock
        DispensaryItem("disp_7", "Metformin 500mg", "Diabetes", 240, "tablets", 70, "10 Jul 2026"),
        DispensaryItem("disp_8", "Ambroxol Syrup (100ml)", "Respiratory", 12, "bottles", 20, "20 May 2026") // Low stock
    )

    val initialDiseaseTrendRecords = listOf(
        DiseaseTrendRecord("dt_1", "Sundarpura", "Viral Fever", 12, "10 Aug 2026", "Moderate"),
        DiseaseTrendRecord("dt_2", "Sundarpura", "Viral Fever", 15, "12 Aug 2026", "High"),
        DiseaseTrendRecord("dt_3", "Sundarpura", "Viral Fever", 18, "15 Aug 2026", "Severe"),
        DiseaseTrendRecord("dt_4", "Sundarpura", "Viral Fever", 14, "18 Aug 2026", "Moderate"),
        DiseaseTrendRecord("dt_5", "Kalyanpur", "Dengue", 2, "05 Aug 2026", "High"),
        DiseaseTrendRecord("dt_6", "Kalyanpur", "Dengue", 5, "10 Aug 2026", "Severe"),
        DiseaseTrendRecord("dt_7", "Kalyanpur", "Dengue", 8, "15 Aug 2026", "Severe")
    )

    val initialNotices = listOf(
        BroadcastNotice(
            id = "not_1",
            senderRole = UserRole.ADMIN,
            senderName = "District Chief Medical Officer",
            targetRole = "ALL",
            targetVillage = "Sundarpura",
            title = "⚠️ Seasonal Viral & Fever Outbreak Advisory",
            message = "High incidence of respiratory fever detected in Sundarpura. All ASHA workers are requested to conduct door-to-door temperature monitoring and distribute ORS packets.",
            timestamp = System.currentTimeMillis() - 3600000L * 4,
            isUrgent = true
        ),
        BroadcastNotice(
            id = "not_2",
            senderRole = UserRole.ASHA,
            senderName = "Priya Devi (ASHA-7701)",
            targetRole = "PATIENT",
            targetVillage = "Sundarpura",
            title = "👶 Weekly Village Maternal Immunization Camp",
            message = "Immunization and nutrition checkup camp this Friday at Sundarpura Primary School from 9:00 AM to 1:00 PM. Please bring your Health Card.",
            timestamp = System.currentTimeMillis() - 3600000L * 18,
            isUrgent = false
        )
    )

    val initialSchemes = listOf(
        GovernmentScheme(
            id = "sch_1",
            title = "Ayushman Bharat — PM-JAY",
            category = "Universal Health Coverage",
            targetBeneficiary = "All Rural Families / BPL Card Holders",
            benefitsSummary = "Cashless health cover up to ₹5 Lakh per family per year for secondary and tertiary hospitalization care.",
            eligibility = "Identified via SECC 2011 database or verified ration card holder."
        ),
        GovernmentScheme(
            id = "sch_2",
            title = "Pradhan Mantri Matru Vandana Yojana (PMMVY)",
            category = "Maternal & Child Health",
            targetBeneficiary = "Pregnant Women & Lactating Mothers",
            benefitsSummary = "Direct cash incentive of ₹5,000 in three installments upon early pregnancy registration and institutional delivery.",
            eligibility = "First live birth, registered at Anganwadi/PHC center."
        ),
        GovernmentScheme(
            id = "sch_3",
            title = "Rashtriya Kishor Swasthya Karyakram (RKSK)",
            category = "Adolescent & Mental Health",
            targetBeneficiary = "Adolescents (Age 10–19)",
            benefitsSummary = "Free peer counseling, nutrition advice, IFA supplements, and mental wellness support at Adolescent Friendly Health Clinics (AFHC).",
            eligibility = "All rural adolescents residing in the district."
        ),
        GovernmentScheme(
            id = "sch_4",
            title = "National TB Elimination Programme (Nikshay Poshan)",
            category = "Communicable Diseases",
            targetBeneficiary = "Notified TB Patients",
            benefitsSummary = "Financial incentive of ₹500/month directly into bank account for nutritional support throughout treatment.",
            eligibility = "All active TB patients registered on the Nikshay portal."
        )
    )

    val initialImmunizations = listOf(
        ImmunizationRecord(
            id = "imm_1",
            childName = "Aarav Kumar",
            motherName = "Anita Sharma",
            dobFormatted = "10 Jan 2026",
            gender = "Male",
            villageName = "Sundarpura",
            ashaWorkerId = "asha_priya",
            vaccines = listOf(
                VaccineInfo("BCG", "10 Jan 2026", "11 Jan 2026", "Completed"),
                VaccineInfo("OPV-1", "10 Feb 2026", "15 Feb 2026", "Completed"),
                VaccineInfo("Pentavalent-1", "10 Feb 2026", null, "Overdue"),
                VaccineInfo("Rotavirus-1", "10 Feb 2026", null, "Overdue")
            )
        )
    )

    val initialDailyRounds = listOf(
        DailyRound(
            id = "round_1",
            dateFormatted = "16 Aug 2026",
            villageName = "Sundarpura",
            householdName = "Sharma Household",
            personName = "Anita Sharma",
            ashaWorkerId = "asha_priya",
            purpose = "Prenatal checkup follow-up",
            isPregnancyChecked = true,
            isChildHealthChecked = false,
            isImmunizationChecked = false,
            isMedicineGiven = true,
            isCounsellingDone = true,
            notes = "Provided IFA supplements, vitals normal.",
            status = "Completed"
        ),
        DailyRound(
            id = "round_2",
            dateFormatted = "16 Aug 2026",
            villageName = "Bhimnagar",
            householdName = "Patel Household",
            personName = "Meena Patel",
            ashaWorkerId = "asha_priya",
            purpose = "Dietary Guidance",
            isPregnancyChecked = false,
            isChildHealthChecked = false,
            isImmunizationChecked = false,
            isMedicineGiven = false,
            isCounsellingDone = false,
            notes = "",
            status = "Pending"
        )
    )

    val initialAshaMedicines = listOf(
        AshaMedicine("asha_med_1", "asha_priya", "Paracetamol 500mg", 40, "tablets", 20, "12 Dec 2027", "01 Aug 2026"),
        AshaMedicine("asha_med_2", "asha_priya", "IFA Tablets", 15, "tablets", 30, "10 Nov 2027", "15 Jul 2026"),
        AshaMedicine("asha_med_3", "asha_priya", "ORS Packets", 5, "packets", 15, "05 Jan 2028", "20 Jun 2026"),
        AshaMedicine("asha_med_4", "asha_priya", "Zinc Tablets", 0, "tablets", 10, "01 Sep 2026", "10 May 2026")
    )

    // Entity conversions for Room seeding
    fun getVillageEntities(): List<VillageEntity> = initialVillages.map {
        VillageEntity(it.id, it.name, it.district, it.state, it.population, it.latitude, it.longitude, it.activeCases, it.highRiskCount)
    }

    fun getAshaEntities(): List<AshaWorkerEntity> = initialAshaWorkers.map {
        AshaWorkerEntity(it.id, it.name, it.ashaUniqueId, it.phone, gson.toJson(it.assignedVillages), it.activePatientCount, it.alertCount)
    }

    fun getDoctorEntities(): List<DoctorEntity> = initialDoctors.map {
        DoctorEntity(it.id, it.name, it.specialty, it.qualification, it.hospitalName, it.distanceKm, it.phone, it.availableDays)
    }

    fun getPatientEntities(): List<PatientEntity> = initialPatients.map {
        PatientEntity(it.id, it.name, it.age, it.gender, it.phone, it.villageId, it.villageName, it.ashaWorkerId, it.ashaWorkerName, it.currentRiskLevel, it.lastCondition, it.lastVisitDate, it.nextAppointmentDate, it.emergencyContact, it.profilePhotoUrl)
    }

    fun getConditionEntities(): List<ConditionRecordEntity> = initialConditionRecords.map {
        ConditionRecordEntity(
            it.id, it.patientId, it.patientName, it.villageId, it.villageName,
            it.category, it.severity, it.requestedDoctorType, it.notes, it.timestamp,
            it.ashaProxyLogged, it.status, it.assignedDoctorId, it.assignedDoctorName,
            it.doctorResponse, it.doctorResponseTimestamp, it.doctorResponseDoctorName,
            it.privateDoctorNotes, it.referredByDoctorId, it.referredByDoctorName,
            it.referralNotes, it.isPendingSync
        )
    }

    fun getPrescriptionEntities(): List<PrescriptionEntity> = initialPrescriptions.map {
        PrescriptionEntity(
            it.id, it.caseId, it.patientId, it.patientName, it.doctorId, it.doctorName,
            it.doctorSpecialty, it.timestamp, it.dateFormatted, gson.toJson(it.medicines),
            it.instructions, it.isOcrExtracted
        )
    }

    fun getAppointmentEntities(): List<AppointmentEntity> = initialAppointments.map {
        AppointmentEntity(
            it.id, it.patientId, it.patientName, it.doctorId, it.doctorName,
            it.doctorSpecialty, it.dateFormatted, it.timeSlot, it.status, it.proposedBy,
            it.outcomeNotes, it.callType.name, it.scheduledTimestamp
        )
    }

    fun getDispensaryEntities(): List<DispensaryEntity> = initialDispensaryItems.map {
        DispensaryEntity(it.id, it.medicineName, it.category, it.availableQuantity, it.unit, it.reorderThreshold, it.lastRestockDateFormatted)
    }

    fun getNoticeEntities(): List<BroadcastNoticeEntity> = initialNotices.map {
        BroadcastNoticeEntity(it.id, it.senderRole, it.senderName, it.targetRole, it.targetVillage, it.title, it.message, it.timestamp, it.isUrgent)
    }

    fun getSchemeEntities(): List<GovernmentSchemeEntity> = initialSchemes.map {
        GovernmentSchemeEntity(it.id, it.title, it.category, it.targetBeneficiary, it.benefitsSummary, it.eligibility, it.applicationUrl)
    }

    fun getImmunizationEntities(): List<ImmunizationRecordEntity> = initialImmunizations.map {
        ImmunizationRecordEntity(it.id, it.childName, it.motherName, it.dobFormatted, it.gender, it.villageName, it.ashaWorkerId, gson.toJson(it.vaccines))
    }

    fun getDailyRoundEntities(): List<DailyRoundEntity> = initialDailyRounds.map {
        DailyRoundEntity(it.id, it.dateFormatted, it.villageName, it.householdName, it.personName, it.ashaWorkerId, it.purpose, it.isPregnancyChecked, it.isChildHealthChecked, it.isImmunizationChecked, it.isMedicineGiven, it.isCounsellingDone, it.notes, it.status)
    }

    fun getAshaMedicineEntities(): List<AshaMedicineEntity> = initialAshaMedicines.map {
        AshaMedicineEntity(it.id, it.ashaWorkerId, it.medicineName, it.availableQuantity, it.unit, it.minStockQuantity, it.expiryDateFormatted, it.lastRestockDateFormatted)
    }

    fun getDiseaseTrendRecordEntities(): List<DiseaseTrendRecordEntity> = initialDiseaseTrendRecords.map {
        DiseaseTrendRecordEntity(it.id, it.villageName, it.diseaseName, it.caseCount, it.dateFormatted, it.severity)
    }

    val initialLabReports = listOf(
        LabReport(
            id = "lab_1",
            patientId = "pat_1",
            patientName = "Ramesh Kumar",
            testCategory = "Complete Blood Count (CBC)",
            doctorName = "Dr. Rajesh Kumar",
            dateFormatted = "18 Aug 2026",
            items = listOf(
                LabTestItem("Hemoglobin", "11.2", "g/dL", "13.0 - 17.0", "LOW"),
                LabTestItem("Total Leukocyte Count (WBC)", "12,400", "/mcL", "4,000 - 11,000", "HIGH"),
                LabTestItem("Platelet Count", "1.85", "Lakh/mcL", "1.50 - 4.50", "NORMAL"),
                LabTestItem("Hematocrit (PCV)", "36.8", "%", "40.0 - 50.0", "LOW")
            ),
            notes = "Mild microcytic hypochromic anemia with mild leukocytosis. Clinical correlation suggested."
        ),
        LabReport(
            id = "lab_2",
            patientId = "pat_1",
            patientName = "Ramesh Kumar",
            testCategory = "Biochemistry / Fasting Blood Sugar",
            doctorName = "Dr. Rajesh Kumar",
            dateFormatted = "10 Aug 2026",
            items = listOf(
                LabTestItem("Fasting Blood Glucose", "142", "mg/dL", "70 - 100", "HIGH"),
                LabTestItem("HbA1c (Glycated Hemoglobin)", "7.4", "%", "< 5.7", "HIGH"),
                LabTestItem("Serum Creatinine", "0.9", "mg/dL", "0.7 - 1.3", "NORMAL")
            ),
            notes = "Uncontrolled fasting hyperglycemia. Follow up with diabetic dietary regimen and Metformin."
        ),
        LabReport(
            id = "lab_3",
            patientId = "pat_2",
            patientName = "Sunita Devi",
            testCategory = "Maternal & Antenatal Serology",
            doctorName = "Dr. Rajesh Kumar",
            dateFormatted = "15 Aug 2026",
            items = listOf(
                LabTestItem("Hemoglobin", "9.4", "g/dL", "11.0 - 15.0", "LOW"),
                LabTestItem("ABO Blood Grouping & Rh", "B Positive", "-", "B Positive", "NORMAL"),
                LabTestItem("VDRL / RPR Serology", "Non-Reactive", "-", "Non-Reactive", "NORMAL"),
                LabTestItem("Urine Albumin / Sugar", "Nil / Nil", "-", "Nil", "NORMAL")
            ),
            notes = "Moderate gestational anemia. Prescribed double dose of Iron & Folic Acid (IFA)."
        ),
        LabReport(
            id = "lab_4",
            patientId = "pat_2",
            patientName = "Sunita Devi",
            testCategory = "Dengue & Febrile Serology",
            doctorName = "Dr. Rajesh Kumar",
            dateFormatted = "19 Aug 2026",
            items = listOf(
                LabTestItem("Dengue NS1 Antigen", "Negative", "-", "Negative", "NORMAL"),
                LabTestItem("Widal S. Typhi 'O'", "1:80", "Titre", "< 1:80", "NORMAL"),
                LabTestItem("Malarial Parasite Smear", "Negative", "-", "Negative", "NORMAL")
            ),
            notes = "Viral prodrome. Dengue & Malaria negative."
        )
    )

    val initialOpdTokens = listOf(
        OpdToken(
            id = "tok_1",
            tokenNumber = "OPD-A24",
            patientId = "pat_1",
            patientName = "Ramesh Kumar",
            doctorName = "Dr. Rajesh Kumar",
            department = "General Medicine",
            cabinNumber = "Room 4",
            currentServingToken = "OPD-A21",
            estimatedWaitMinutes = 12,
            status = "In Queue",
            dateFormatted = "Today"
        ),
        OpdToken(
            id = "tok_2",
            tokenNumber = "OPD-B12",
            patientId = "pat_2",
            patientName = "Sunita Devi",
            doctorName = "Dr. Rajesh Kumar",
            department = "Maternal & Antenatal Care",
            cabinNumber = "Room 2",
            currentServingToken = "OPD-B12",
            estimatedWaitMinutes = 0,
            status = "Serving",
            dateFormatted = "Today"
        ),
        OpdToken(
            id = "tok_3",
            tokenNumber = "OPD-C05",
            patientId = "pat_3",
            patientName = "Vikram Singh",
            doctorName = "Dr. Ayushman Dev Singh",
            department = "Orthopedics & Trauma Surgery",
            cabinNumber = "Trauma Bay 1",
            currentServingToken = "OPD-C03",
            estimatedWaitMinutes = 20,
            status = "In Queue",
            dateFormatted = "Today"
        )
    )

    val initialMedicalCertificates = listOf(
        MedicalCertificate(
            id = "cert_1",
            certificateNumber = "MC-2026-8910",
            patientId = "pat_1",
            patientName = "Ramesh Kumar",
            patientAge = 45,
            patientGender = "Male",
            doctorName = "Dr. Rajesh Kumar",
            doctorRegistrationNumber = "MCI-48201",
            diagnosis = "Acute Febrile Illness & Bronchitis",
            restStartDate = "12 Aug 2026",
            restEndDate = "17 Aug 2026",
            fitDate = "18 Aug 2026",
            certificateType = "Sick Leave Certificate",
            issuedDateFormatted = "12 Aug 2026"
        ),
        MedicalCertificate(
            id = "cert_2",
            certificateNumber = "MC-2026-9042",
            patientId = "pat_1",
            patientName = "Ramesh Kumar",
            patientAge = 45,
            patientGender = "Male",
            doctorName = "Dr. Rajesh Kumar",
            doctorRegistrationNumber = "MCI-48201",
            diagnosis = "Recovered from Acute Bronchitis - Fit to Resume Normal Duties",
            restStartDate = "12 Aug 2026",
            restEndDate = "17 Aug 2026",
            fitDate = "18 Aug 2026",
            certificateType = "Medical Fitness Certificate",
            issuedDateFormatted = "18 Aug 2026"
        )
    )

    val initialBloodStock = listOf(
        BloodStockItem("bs_1", "A+", 24, "District Civil Hospital & Blood Centre (Rampur)", "0595-2348101", "Available"),
        BloodStockItem("bs_2", "A-", 4, "District Civil Hospital & Blood Centre (Rampur)", "0595-2348101", "Low Stock"),
        BloodStockItem("bs_3", "B+", 38, "District Civil Hospital & Blood Centre (Rampur)", "0595-2348101", "Available"),
        BloodStockItem("bs_4", "B-", 6, "Community Health Centre (Bilaspur)", "0595-2348102", "Available"),
        BloodStockItem("bs_5", "O+", 45, "District Civil Hospital & Blood Centre (Rampur)", "0595-2348101", "Available"),
        BloodStockItem("bs_6", "O-", 3, "District Civil Hospital & Blood Centre (Rampur)", "0595-2348101", "Critical"),
        BloodStockItem("bs_7", "AB+", 18, "Sub-District Hospital (Shahabad)", "0595-2348103", "Available"),
        BloodStockItem("bs_8", "AB-", 2, "District Civil Hospital & Blood Centre (Rampur)", "0595-2348101", "Critical")
    )

    val initialFamilyMembers = listOf(
        FamilyMember("fam_1", "pat_1", "Geeta Devi", "Spouse", 42, "Female", "O+", "91-4920-1182-3910"),
        FamilyMember("fam_2", "pat_1", "Amit Kumar", "Son", 14, "Male", "B+", "91-8830-4920-1102"),
        FamilyMember("fam_3", "pat_1", "Kavita Kumari", "Daughter", 9, "Female", "A+", "91-3390-1129-8831")
    )

    fun getLabReportEntities(): List<LabReportEntity> = initialLabReports.map {
        LabReportEntity(it.id, it.patientId, it.patientName, it.testCategory, it.doctorName, it.dateFormatted, it.items, it.notes, it.status)
    }

    fun getOpdTokenEntities(): List<OpdTokenEntity> = initialOpdTokens.map {
        OpdTokenEntity(it.id, it.tokenNumber, it.patientId, it.patientName, it.doctorName, it.department, it.cabinNumber, it.currentServingToken, it.estimatedWaitMinutes, it.status, it.dateFormatted)
    }

    fun getMedicalCertificateEntities(): List<MedicalCertificateEntity> = initialMedicalCertificates.map {
        MedicalCertificateEntity(it.id, it.certificateNumber, it.patientId, it.patientName, it.patientAge, it.patientGender, it.doctorName, it.doctorRegistrationNumber, it.diagnosis, it.restStartDate, it.restEndDate, it.fitDate, it.certificateType, it.issuedDateFormatted)
    }

    fun getBloodStockEntities(): List<BloodStockEntity> = initialBloodStock.map {
        BloodStockEntity(it.id, it.bloodGroup, it.unitsAvailable, it.hospitalName, it.contactPhone, it.status)
    }

    val initialIpdBeds = listOf(
        IpdBed("bed_1", "Male Medical Ward", "BED-01", true, "pat_1", "Ramesh Kumar", "18 Aug 2026", "Dr. Rajesh Kumar", "Acute Bronchitis with Hypoxia", "Sister Sunita R."),
        IpdBed("bed_2", "Male Medical Ward", "BED-02", false),
        IpdBed("bed_3", "Male Medical Ward", "BED-03", true, "pat_3", "Devi Dayal", "19 Aug 2026", "Dr. Rajesh Kumar", "Decompensated Chronic Liver Disease", "Sister Sunita R."),
        IpdBed("bed_4", "Male Medical Ward", "BED-04", false),
        IpdBed("bed_5", "Female & Maternal Ward", "BED-05", true, "pat_2", "Sunita Devi", "17 Aug 2026", "Dr. Ananya Sen", "High-Risk Gestational Anemia (Hb 7.2)", "Sister Rekha M."),
        IpdBed("bed_6", "Female & Maternal Ward", "BED-06", false),
        IpdBed("bed_7", "Female & Maternal Ward", "BED-07", false),
        IpdBed("bed_8", "Emergency Trauma Ward", "BED-08", true, "pat_4", "Kunal Sharma", "20 Aug 2026", "Dr. Ayushman Dev Singh", "RTA Compound Mandibular Fracture", "Sister Preeti K."),
        IpdBed("bed_9", "Emergency Trauma Ward", "BED-09", false),
        IpdBed("bed_10", "Intensive Care Unit (ICU)", "ICU-01", true, "pat_5", "Harish Chandra", "19 Aug 2026", "Dr. Rajesh Kumar", "Septic Shock / Sepsis on Norepinephrine", "Sister Mary J."),
        IpdBed("bed_11", "Intensive Care Unit (ICU)", "ICU-02", false)
    )

    val initialOtSurgeryBookings = listOf(
        OtSurgeryBooking(
            id = "ot_1",
            otRoomName = "Trauma & Ortho OT-2",
            patientId = "pat_4",
            patientName = "Kunal Sharma",
            surgeryName = "Open Reduction & Internal Fixation (ORIF) Mandible",
            surgeonName = "Dr. Ayushman Dev Singh",
            anesthetistName = "Dr. S. K. Verma (Sr. Anesthetist)",
            scheduledDate = "Tomorrow",
            scheduledTimeSlot = "09:00 AM - 11:30 AM",
            pacCleared = true,
            status = "Scheduled"
        ),
        OtSurgeryBooking(
            id = "ot_2",
            otRoomName = "Major OT-1",
            patientId = "pat_1",
            patientName = "Ramesh Kumar",
            surgeryName = "Elective Maxillofacial Cyst Enucleation",
            surgeonName = "Dr. Ayushman Dev Singh",
            anesthetistName = "Dr. Preeti Saxena",
            scheduledDate = "24 Aug 2026",
            scheduledTimeSlot = "12:00 PM - 02:00 PM",
            pacCleared = true,
            status = "Scheduled"
        ),
        OtSurgeryBooking(
            id = "ot_3",
            otRoomName = "Emergency Minor OT",
            patientId = "pat_2",
            patientName = "Sunita Devi",
            surgeryName = "Emergency Cervical Cerclage",
            surgeonName = "Dr. Ananya Sen",
            anesthetistName = "Dr. S. K. Verma",
            scheduledDate = "Yesterday",
            scheduledTimeSlot = "03:00 PM - 04:00 PM",
            pacCleared = true,
            status = "Completed"
        )
    )

    val initialExternalReferrals = listOf(
        ExternalReferral(
            id = "ref_1",
            referralLetterId = "REF-2026-4401",
            patientId = "pat_1",
            patientName = "Ramesh Kumar",
            referringDoctorName = "Dr. Rajesh Kumar",
            empanelledHospitalName = "Railway Central Hospital, New Delhi",
            specialtyRequired = "Cardiothoracic & Vascular Surgery",
            clinicalSummary = "Severe Tri-Vessel Coronary Artery Disease with post-infarct angina requiring urgent CABG evaluation.",
            isCashlessApproved = true,
            ambulanceRequisitioned = true,
            issuedDate = "19 Aug 2026",
            status = "Active"
        ),
        ExternalReferral(
            id = "ref_2",
            referralLetterId = "REF-2026-4402",
            patientId = "pat_2",
            patientName = "Sunita Devi",
            referringDoctorName = "Dr. Ananya Sen",
            empanelledHospitalName = "AIIMS New Delhi (Apex Maternal Center)",
            specialtyRequired = "Fetal Medicine & High Risk Perinatology",
            clinicalSummary = "Severe Rh Isoimmunization with Fetal Hydrops signs for specialized intrauterine transfusion.",
            isCashlessApproved = true,
            ambulanceRequisitioned = false,
            issuedDate = "16 Aug 2026",
            status = "Active"
        )
    )

    val initialBioMedicalEquipment = listOf(
        BioMedicalEquipment("bme_1", "BME-OX-104", "PSA Medical Oxygen Generator (250 LPM)", "Hospital Central Supply", "OPERATIONAL", "15 Jul 2026", "15 Oct 2026", "Utility Block B", "Er. Manoj Kumar (9876541101)"),
        BioMedicalEquipment("bme_2", "BME-ECG-02", "12-Lead Digital ECG Machine (Schiller)", "Emergency Trauma Ward", "OPERATIONAL", "01 Aug 2026", "01 Nov 2026", "Trauma Cabin 1", "Er. Manoj Kumar (9876541101)"),
        BioMedicalEquipment("bme_3", "BME-DEF-01", "Biphasic Defibrillator with Pacer", "ICU / Critical Care", "OPERATIONAL", "10 Aug 2026", "10 Nov 2026", "ICU Station Alpha", "Er. Manoj Kumar (9876541101)"),
        BioMedicalEquipment("bme_4", "BME-USG-03", "Color Doppler Ultrasound Scanner (GE)", "Radiology & Imaging", "CALIBRATION_DUE", "12 May 2026", "12 Aug 2026", "Radiology Room 2", "Er. Amit Saxena (9876541102)"),
        BioMedicalEquipment("bme_5", "BME-AUTO-01", "High-Pressure Horizontal Autoclave", "Central Sterile Supply (CSSD)", "OPERATIONAL", "25 Jul 2026", "25 Oct 2026", "CSSD Sterilization Room", "Er. Manoj Kumar (9876541101)"),
        BioMedicalEquipment("bme_6", "BME-VENT-04", "Advanced ICU Invasive Ventilator", "ICU / Critical Care", "UNDER_MAINTENANCE", "05 Jun 2026", "05 Sep 2026", "ICU Bay 4", "Er. Amit Saxena (9876541102)")
    )

    fun getIpdBedEntities(): List<IpdBedEntity> = initialIpdBeds.map {
        IpdBedEntity(it.id, it.wardName, it.bedNumber, it.isOccupied, it.patientId, it.patientName, it.admissionDate, it.attendingDoctorName, it.diagnosis, it.nurseInCharge)
    }

    fun getOtSurgeryBookingEntities(): List<OtSurgeryBookingEntity> = initialOtSurgeryBookings.map {
        OtSurgeryBookingEntity(it.id, it.otRoomName, it.patientId, it.patientName, it.surgeryName, it.surgeonName, it.anesthetistName, it.scheduledDate, it.scheduledTimeSlot, it.pacCleared, it.status)
    }

    fun getExternalReferralEntities(): List<ExternalReferralEntity> = initialExternalReferrals.map {
        ExternalReferralEntity(it.id, it.referralLetterId, it.patientId, it.patientName, it.referringDoctorName, it.empanelledHospitalName, it.specialtyRequired, it.clinicalSummary, it.isCashlessApproved, it.ambulanceRequisitioned, it.issuedDate, it.status)
    }

    fun getBioMedicalEquipmentEntities(): List<BioMedicalEquipmentEntity> = initialBioMedicalEquipment.map {
        BioMedicalEquipmentEntity(it.id, it.assetCode, it.name, it.department, it.status, it.lastServiceDate, it.nextServiceDue, it.location, it.inChargeContact)
    }

    val initialReferrals = listOf(
        Referral(
            id = "ref_101",
            patientId = "pat_ramesh",
            patientName = "Ramesh Kumar",
            referringUserId = "doc_rajesh",
            referringUserName = "Dr. Rajesh Varma",
            referringUserSpecialty = "General Physician",
            targetDoctorId = "doc_anita_cardio",
            targetDoctorName = "Dr. Anita Sharma",
            targetSpecialty = "Cardiology",
            reason = "Persistent severe chest tightness, exertional dyspnea, and irregular pulse noted during PHC evaluation.",
            clinicalQuestion = "Please evaluate for ischemic heart disease or arrhythmia; advise on 2D-ECHO and medical optimization.",
            urgency = ReferralUrgency.URGENT,
            attachedRecordIds = listOf("cond_1", "rx_1"),
            status = ReferralStatus.CREATED,
            createdAt = System.currentTimeMillis() - 3600000 * 4
        ),
        Referral(
            id = "ref_102",
            patientId = "pat_anita",
            patientName = "Anita Sharma",
            referringUserId = "doc_rajesh",
            referringUserName = "Dr. Rajesh Varma",
            referringUserSpecialty = "General Physician",
            targetDoctorId = "doc_sunita_gynae",
            targetDoctorName = "Dr. Sunita Rao",
            targetSpecialty = "Gynecologist & Maternal Care",
            reason = "2nd Trimester Routine Prenatal with borderline hemoglobin (9.4 g/dL) and elevated pedal edema.",
            clinicalQuestion = "Confirm maternal nutrition & iron titration plan, check for preeclampsia risk markers.",
            urgency = ReferralUrgency.ROUTINE,
            attachedRecordIds = listOf("cond_2"),
            status = ReferralStatus.ACCEPTED,
            createdAt = System.currentTimeMillis() - 3600000 * 20,
            respondedAt = System.currentTimeMillis() - 3600000 * 18
        ),
        Referral(
            id = "ref_103",
            patientId = "pat_vikram",
            patientName = "Vikram Singh",
            referringUserId = "doc_rajesh",
            referringUserName = "Dr. Rajesh Varma",
            referringUserSpecialty = "General Physician",
            targetDoctorId = "doc_arun_ortho",
            targetDoctorName = "Dr. Arun Kumar",
            targetSpecialty = "Orthopedic Surgeon",
            reason = "Severe osteoarthritis of bilateral knees with limited ambulation, refractory to NSAIDs.",
            clinicalQuestion = "Evaluate suitability for intra-articular steroid injection or total knee replacement candidate.",
            urgency = ReferralUrgency.ROUTINE,
            attachedRecordIds = listOf("cond_3"),
            status = ReferralStatus.COMPLETED,
            specialistFindings = "Patient has Grade IV Kellgren-Lawrence bilateral knee osteoarthritis. Right knee effusion present without acute septic signs.",
            specialistRecommendations = "Administered right knee Triamcinolone 40mg injection under aseptic precautions. Advised isometric quadriceps physiotherapy and scheduled follow-up in 4 weeks. Candidate for staged TKR once glycemic control stabilizes.",
            specialistFollowUpNeeded = true,
            createdAt = System.currentTimeMillis() - 3600000 * 48,
            respondedAt = System.currentTimeMillis() - 3600000 * 40,
            completedAt = System.currentTimeMillis() - 3600000 * 24
        )
    )

    fun getReferralEntities(): List<ReferralEntity> = initialReferrals.map { it.toEntity() }
}


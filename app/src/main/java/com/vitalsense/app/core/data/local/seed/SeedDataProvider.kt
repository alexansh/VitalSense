package com.vitalsense.app.core.data.local.seed

import com.vitalsense.app.core.data.local.entity.DepartmentEntity
import com.vitalsense.app.core.data.local.entity.DispensaryEntity
import com.vitalsense.app.core.data.local.entity.GovernmentSchemeEntity
import com.vitalsense.app.core.data.local.entity.ReferralEntity
import com.vitalsense.app.core.data.model.*

object SeedDataProvider {

    // ──────────────────────────────────────────────
    // DEPARTMENTS — full hospital set, database-driven
    // ──────────────────────────────────────────────
    val initialDepartments = listOf(
        // Clinical Departments
        Department("dept_general_medicine", "General Medicine", "GEN_MED", "🩺", DepartmentType.CLINICAL, 0xFFE8EB7D, description = "Primary care, fever, infections, chronic disease management", operatingHours = "24x7", location = "OPD Block A, Ground Floor"),
        Department("dept_general_surgery", "General Surgery", "GEN_SURG", "🔪", DepartmentType.CLINICAL, 0xFFFF9F43, description = "Surgical interventions, wound care, minor and major operations", operatingHours = "Mon-Sat 9AM-5PM", location = "Surgical Block B, Floor 1"),
        Department("dept_pediatrics", "Pediatrics", "PEDIA", "👶", DepartmentType.CLINICAL, 0xFFC8F5D4, description = "Child health, vaccinations, neonatal care", operatingHours = "24x7", location = "OPD Block A, Floor 1"),
        Department("dept_obgyn", "OB-GYN & Maternal Care", "OBGYN", "🤱", DepartmentType.CLINICAL, 0xFFFFB8F0, description = "Pregnancy, delivery, women's reproductive health", operatingHours = "24x7", location = "Maternity Wing, Floor 2"),
        Department("dept_orthopedics", "Orthopedics", "ORTHO", "🦴", DepartmentType.CLINICAL, 0xFFD4E5FF, description = "Bones, joints, fractures, musculoskeletal problems", operatingHours = "Mon-Sat 9AM-5PM", location = "Surgical Block B, Floor 2"),
        Department("dept_cardiology", "Cardiology", "CARDIO", "❤️", DepartmentType.CLINICAL, 0xFFFF6B6B, description = "Heart disease, chest pain, ECG, blood pressure", operatingHours = "Mon-Sat 9AM-5PM", location = "OPD Block A, Floor 2"),
        Department("dept_neurology", "Neurology & Neurosurgery", "NEURO", "🧠", DepartmentType.CLINICAL, 0xFFA3AEFE, description = "Brain, spinal cord, nerves, seizures, stroke", operatingHours = "Mon-Sat 9AM-5PM", location = "Specialty Block C, Floor 1"),
        Department("dept_psychiatry", "Psychiatry & Mental Health", "PSYCH", "🧘", DepartmentType.CLINICAL, 0xFFE1D9F9, description = "Mental wellness, depression, anxiety, counseling", operatingHours = "Mon-Sat 10AM-4PM", location = "OPD Block A, Floor 3"),
        Department("dept_ent", "ENT (Ear, Nose, Throat)", "ENT", "👂", DepartmentType.CLINICAL, 0xFFFFD166, description = "Ear infections, hearing, sinus, throat problems", operatingHours = "Mon-Sat 9AM-5PM", location = "OPD Block A, Floor 2"),
        Department("dept_ophthalmology", "Ophthalmology (Eye)", "OPHTH", "👁️", DepartmentType.CLINICAL, 0xFF90CAF9, description = "Eye care, vision problems, cataract, glaucoma", operatingHours = "Mon-Sat 9AM-4PM", location = "Eye Centre, Ground Floor"),
        Department("dept_dermatology", "Dermatology (Skin)", "DERM", "🧴", DepartmentType.CLINICAL, 0xFFFFCCBC, description = "Skin diseases, allergies, rashes, fungal infections", operatingHours = "Mon-Sat 9AM-4PM", location = "OPD Block A, Floor 3"),
        Department("dept_pulmonology", "Pulmonology (Chest/Lungs)", "PULMO", "🫁", DepartmentType.CLINICAL, 0xFFB2DFDB, description = "Asthma, TB, breathing problems, chest X-Ray review", operatingHours = "Mon-Sat 9AM-5PM", location = "Specialty Block C, Floor 2"),
        Department("dept_nephrology", "Nephrology (Kidney)", "NEPHRO", "🫘", DepartmentType.CLINICAL, 0xFFCE93D8, description = "Kidney disease, dialysis, urinary tract problems", operatingHours = "Mon-Sat 9AM-4PM", location = "Specialty Block C, Floor 2"),
        Department("dept_gastroenterology", "Gastroenterology (GI)", "GASTRO", "🍽️", DepartmentType.CLINICAL, 0xFFFFE082, description = "Stomach, liver, intestinal problems, digestion", operatingHours = "Mon-Sat 9AM-5PM", location = "OPD Block A, Floor 2"),
        Department("dept_endocrinology", "Endocrinology (Diabetes/Thyroid)", "ENDO", "💉", DepartmentType.CLINICAL, 0xFFEF9A9A, description = "Diabetes, thyroid, hormonal disorders", operatingHours = "Mon-Sat 9AM-4PM", location = "OPD Block A, Floor 3"),
        Department("dept_oncology", "Oncology (Cancer)", "ONCO", "🎗️", DepartmentType.CLINICAL, 0xFFB39DDB, description = "Cancer screening, diagnosis, treatment referrals", operatingHours = "Mon-Fri 9AM-5PM", location = "Specialty Block C, Floor 3"),
        Department("dept_emergency", "Emergency Medicine", "EMERG", "🚨", DepartmentType.CLINICAL, 0xFFFF6B6B, description = "Accidents, trauma, acute emergencies, critical care", operatingHours = "24x7", location = "Emergency Block, Ground Floor"),
        Department("dept_dental", "Dental", "DENTAL", "🦷", DepartmentType.CLINICAL, 0xFF80DEEA, description = "Tooth care, dental surgery, oral health", operatingHours = "Mon-Sat 9AM-4PM", location = "Dental Wing, Ground Floor"),

        // Service / Diagnostic Departments
        Department("dept_radiology", "Radiology & Imaging", "RADIO", "📡", DepartmentType.SERVICE, 0xFFB0BEC5, description = "X-Ray, ultrasound, CT scan, MRI imaging", operatingHours = "24x7 (Emergency), Mon-Sat 8AM-6PM (Routine)", location = "Imaging Centre, Ground Floor"),
        Department("dept_pathology", "Pathology & Lab", "PATH", "🧪", DepartmentType.SERVICE, 0xFFFFCC80, description = "Blood tests, urine tests, biopsy, lab reports", operatingHours = "24x7 (Emergency), Mon-Sat 7AM-5PM (Routine)", location = "Lab Block, Ground Floor"),
        Department("dept_microbiology", "Microbiology", "MICRO", "🦠", DepartmentType.SERVICE, 0xFFA5D6A7, description = "Infection testing, culture sensitivity, TB testing", operatingHours = "Mon-Sat 8AM-4PM", location = "Lab Block, Floor 1"),
        Department("dept_blood_bank", "Blood Bank", "BLOOD", "🩸", DepartmentType.SERVICE, 0xFFEF9A9A, description = "Blood storage, cross-matching, transfusion services", operatingHours = "24x7", location = "Blood Bank Building"),
        Department("dept_pharmacy", "Pharmacy / Dispensary", "PHARMA", "💊", DepartmentType.SERVICE, 0xFF80CBC4, description = "Medicine dispensing, drug information, stock management", operatingHours = "24x7", location = "Pharmacy Counter, Ground Floor"),
        Department("dept_dietetics", "Dietetics & Nutrition", "DIET", "🥗", DepartmentType.SERVICE, 0xFFDCE775, description = "Diet plans, nutrition counseling, malnutrition management", operatingHours = "Mon-Sat 9AM-4PM", location = "OPD Block A, Floor 1"),
        Department("dept_physiotherapy", "Physiotherapy & Rehab", "PHYSIO", "🏋️", DepartmentType.SERVICE, 0xFFC5E1A5, description = "Physical rehabilitation, mobility exercises, post-surgery recovery", operatingHours = "Mon-Sat 8AM-4PM", location = "Rehab Centre, Ground Floor")
    )

    // ──────────────────────────────────────────────
    // SAMPLE REFERRALS — for demo/testing
    // ──────────────────────────────────────────────
    val initialReferrals = listOf(
        Referral(
            id = "ref_sample_1",
            caseId = "case_demo_1",
            patientId = "demo_patient_1",
            patientName = "Ramesh Kumar",
            fromDoctorId = "demo_doc_1",
            fromDoctorName = "Dr. Rajesh Sharma",
            fromDepartmentId = "dept_general_medicine",
            fromDepartmentName = "General Medicine",
            toDepartmentId = "dept_cardiology",
            toDepartmentName = "Cardiology",
            referralType = ReferralType.CLINICAL,
            urgency = ReferralUrgency.URGENT,
            reason = "Suspected arrhythmia — ECG shows irregular pattern, needs specialist evaluation",
            clinicalNotes = "Patient presents with palpitations for 3 days. BP 150/95. ECG shows irregular rhythm. Needs cardiology opinion.",
            clinicalHistory = "45M, HTN on Amlodipine 5mg. Previous visit for general fever 2 weeks ago.",
            status = ReferralStatus.PENDING,
            createdAt = System.currentTimeMillis() - 3600000L
        ),
        Referral(
            id = "ref_sample_2",
            caseId = "case_demo_1",
            patientId = "demo_patient_1",
            patientName = "Ramesh Kumar",
            fromDoctorId = "demo_doc_1",
            fromDoctorName = "Dr. Rajesh Sharma",
            fromDepartmentId = "dept_general_medicine",
            fromDepartmentName = "General Medicine",
            toDepartmentId = "dept_radiology",
            toDepartmentName = "Radiology & Imaging",
            referralType = ReferralType.SERVICE,
            urgency = ReferralUrgency.PRIORITY,
            reason = "Request PA chest X-Ray to rule out cardiomegaly",
            clinicalNotes = "Please do PA view chest X-Ray. Patient has suspected cardiac issue with palpitations.",
            clinicalHistory = "45M, HTN. Presenting with palpitations.",
            status = ReferralStatus.COMPLETED,
            serviceReportText = "PA chest X-Ray: Heart size within normal limits. Lung fields clear. No cardiomegaly. Costophrenic angles clear.",
            serviceReportTimestamp = System.currentTimeMillis() - 1800000L,
            createdAt = System.currentTimeMillis() - 7200000L,
            completedAt = System.currentTimeMillis() - 1800000L
        )
    )

    // ──────────────────────────────────────────────
    // DISPENSARY STOCK (unchanged)
    // ──────────────────────────────────────────────
    val initialDispensaryItems = listOf(
        DispensaryItem("disp_1", "Paracetamol 650mg", "Analgesic / Antipyretic", 450, "tablets", 100),
        DispensaryItem("disp_2", "Amoxicillin 500mg", "Antibiotic", 180, "capsules", 50),
        DispensaryItem("disp_3", "Oral Rehydration Salts (ORS)", "Hydration", 320, "packets", 80),
        DispensaryItem("disp_4", "Iron & Folic Acid (IFA)", "Maternal / Anemia", 500, "tablets", 150),
        DispensaryItem("disp_5", "Cetirizine 10mg", "Antihistamine", 220, "tablets", 60),
        DispensaryItem("disp_6", "Amlodipine 5mg", "Hypertension", 35, "tablets", 50),
        DispensaryItem("disp_7", "Metformin 500mg", "Diabetes", 240, "tablets", 70),
        DispensaryItem("disp_8", "Ambroxol Syrup (100ml)", "Respiratory", 12, "bottles", 20)
    )

    // ──────────────────────────────────────────────
    // GOVERNMENT SCHEMES (unchanged)
    // ──────────────────────────────────────────────
    val initialSchemes = listOf(
        GovernmentScheme(id = "sch_1", title = "Ayushman Bharat — PM-JAY", category = "Universal Health Coverage", targetBeneficiary = "All Rural Families / BPL Card Holders", benefitsSummary = "Cashless health cover up to ₹5 Lakh per family per year for secondary and tertiary hospitalization care.", eligibility = "Identified via SECC 2011 database or verified ration card holder."),
        GovernmentScheme(id = "sch_2", title = "Pradhan Mantri Matru Vandana Yojana (PMMVY)", category = "Maternal & Child Health", targetBeneficiary = "Pregnant Women & Lactating Mothers", benefitsSummary = "Direct cash incentive of ₹5,000 in three installments upon early pregnancy registration and institutional delivery.", eligibility = "First live birth, registered at Anganwadi/PHC center."),
        GovernmentScheme(id = "sch_3", title = "Rashtriya Kishor Swasthya Karyakram (RKSK)", category = "Adolescent & Mental Health", targetBeneficiary = "Adolescents (Age 10–19)", benefitsSummary = "Free peer counseling, nutrition advice, IFA supplements, and mental wellness support at Adolescent Friendly Health Clinics (AFHC).", eligibility = "All rural adolescents residing in the district."),
        GovernmentScheme(id = "sch_4", title = "National TB Elimination Programme (Nikshay Poshan)", category = "Communicable Diseases", targetBeneficiary = "Notified TB Patients", benefitsSummary = "Financial incentive of ₹500/month directly into bank account for nutritional support throughout treatment.", eligibility = "All active TB patients registered on the Nikshay portal.")
    )

    // ──────────────────────────────────────────────
    // Entity conversion helpers
    // ──────────────────────────────────────────────
    fun getDepartmentEntities(): List<DepartmentEntity> = initialDepartments.map {
        DepartmentEntity(
            id = it.id, name = it.name, code = it.code, emoji = it.emoji,
            type = it.type, colorHex = it.colorHex,
            headDoctorId = it.headDoctorId, headDoctorName = it.headDoctorName,
            isActive = it.isActive, availableDoctorCount = it.availableDoctorCount,
            pendingReferralCount = it.pendingReferralCount, description = it.description,
            operatingHours = it.operatingHours, location = it.location
        )
    }

    fun getReferralEntities(): List<ReferralEntity> = initialReferrals.map {
        ReferralEntity(
            id = it.id, caseId = it.caseId, patientId = it.patientId,
            patientName = it.patientName, fromDoctorId = it.fromDoctorId,
            fromDoctorName = it.fromDoctorName, fromDepartmentId = it.fromDepartmentId,
            fromDepartmentName = it.fromDepartmentName, toDepartmentId = it.toDepartmentId,
            toDepartmentName = it.toDepartmentName, toDoctorId = it.toDoctorId,
            toDoctorName = it.toDoctorName, referralType = it.referralType,
            urgency = it.urgency, reason = it.reason, clinicalNotes = it.clinicalNotes,
            clinicalHistory = it.clinicalHistory, status = it.status,
            acceptedByDoctorId = it.acceptedByDoctorId,
            acceptedByDoctorName = it.acceptedByDoctorName,
            acceptedAt = it.acceptedAt, serviceReportText = it.serviceReportText,
            serviceReportAttachmentPath = it.serviceReportAttachmentPath,
            serviceReportAttachmentUrl = it.serviceReportAttachmentUrl,
            serviceReportTimestamp = it.serviceReportTimestamp,
            parentReferralId = it.parentReferralId,
            referralChainIndex = it.referralChainIndex,
            createdAt = it.createdAt, updatedAt = it.updatedAt,
            completedAt = it.completedAt
        )
    }

    fun getDispensaryEntities(): List<DispensaryEntity> = initialDispensaryItems.map {
        DispensaryEntity(it.id, it.medicineName, it.category, it.availableQuantity, it.unit, it.reorderThreshold)
    }

    fun getSchemeEntities(): List<GovernmentSchemeEntity> = initialSchemes.map {
        GovernmentSchemeEntity(it.id, it.title, it.category, it.targetBeneficiary, it.benefitsSummary, it.eligibility, it.applicationUrl)
    }
}

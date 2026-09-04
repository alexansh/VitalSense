package com.vitalsense.app.feature.prescriptions.ocr

import org.junit.Assert.*
import org.junit.Test

class PrescriptionOcrHelperTest {

    @Test
    fun testLevenshteinDistance() {
        assertEquals(0, PrescriptionOcrHelper.calculateLevenshteinDistance("Paracetamol", "Paracetamol"))
        assertEquals(1, PrescriptionOcrHelper.calculateLevenshteinDistance("Dolo", "D0lo"))
        assertEquals(1, PrescriptionOcrHelper.calculateLevenshteinDistance("Augmentin", "Augmentn"))
        assertEquals(1, PrescriptionOcrHelper.calculateLevenshteinDistance("Crocin", "Crocn"))
    }

    @Test
    fun testFuzzyMatchMedicineWithCommonOcrErrors() {
        // Character substitution: 0 -> o, 1 -> l
        assertEquals("Paracetamol", PrescriptionOcrHelper.fuzzyMatchMedicine("Paracetam0l"))
        assertEquals("Dolo", PrescriptionOcrHelper.fuzzyMatchMedicine("D0lo"))
        assertEquals("Combiflam", PrescriptionOcrHelper.fuzzyMatchMedicine("Combif1am"))

        // Missing letters in handwriting
        assertEquals("Augmentin", PrescriptionOcrHelper.fuzzyMatchMedicine("Augmentn"))
        assertEquals("Crocin", PrescriptionOcrHelper.fuzzyMatchMedicine("Crocn"))
        assertEquals("Metformin", PrescriptionOcrHelper.fuzzyMatchMedicine("Metformn"))
        assertEquals("Cetirizine", PrescriptionOcrHelper.fuzzyMatchMedicine("Cetrizne"))
        assertEquals("Azithromycin", PrescriptionOcrHelper.fuzzyMatchMedicine("Azithromycen"))
    }

    @Test
    fun testMedicalShorthandFrequencyAndFoodTiming() {
        // BD / 1-0-1
        val bdFreq = PrescriptionOcrHelper.extractFrequency("Tab PCM 1-0-1")
        assertTrue(bdFreq.contains("Twice daily", ignoreCase = true))

        // TDS / 1-1-1
        val tdsFreq = PrescriptionOcrHelper.extractFrequency("Cap Amox TDS")
        assertTrue(tdsFreq.contains("3 times daily", ignoreCase = true))

        // HS / 0-0-1 (Bedtime)
        val hsFreq = PrescriptionOcrHelper.extractFrequency("Tab Cetirizine 10mg HS")
        assertTrue(hsFreq.contains("bedtime", ignoreCase = true))

        // OD / 1-0-0 (Morning)
        val odFreq = PrescriptionOcrHelper.extractFrequency("Tab Telma 40 OD")
        assertTrue(odFreq.contains("Once daily (morning)", ignoreCase = true))

        // SOS (As needed)
        val sosFreq = PrescriptionOcrHelper.extractFrequency("Tab Dolo 650 SOS")
        assertTrue(sosFreq.contains("As needed", ignoreCase = true))

        // AC (Before food)
        val acTiming = PrescriptionOcrHelper.extractFrequency("Cap Pantocid 40mg OD AC")
        assertTrue(acTiming.contains("Before Food", ignoreCase = true))

        // PC (After food)
        val pcTiming = PrescriptionOcrHelper.extractFrequency("Tab Combiflam 1-0-1 PC")
        assertTrue(pcTiming.contains("After Food", ignoreCase = true))
    }

    @Test
    fun testParseMedicinesFromFeverPrescription() {
        val sampleText = """
            Rx:
            Tab Paracetamol 650mg 1-0-1 (BD) for 3 days
            Tab Cetirizine 10mg 0-0-1 (HS) x 5 days
            Syp Cough Syrup 10ml TDS 5/7
        """.trimIndent()

        val medicines = PrescriptionOcrHelper.parseMedicinesFromText(sampleText)

        assertEquals(3, medicines.size)

        // Paracetamol
        val pcm = medicines.find { it.name == "Paracetamol" }
        assertNotNull(pcm)
        assertEquals("650 mg", pcm?.dosage)
        assertTrue(pcm?.frequency?.contains("Twice daily", ignoreCase = true) == true)
        assertEquals("3 Days", pcm?.duration)

        // Cetirizine
        val ctz = medicines.find { it.name == "Cetirizine" }
        assertNotNull(ctz)
        assertEquals("10 mg", ctz?.dosage)
        assertTrue(ctz?.frequency?.contains("bedtime", ignoreCase = true) == true)
        assertEquals("5 Days", ctz?.duration)

        // Cough Syrup
        val syp = medicines.find { it.name == "Cough Syrup" }
        assertNotNull(syp)
        assertEquals("10 ml", syp?.dosage)
        assertTrue(syp?.frequency?.contains("3 times daily", ignoreCase = true) == true)
    }

    @Test
    fun testParseMedicinesFromMaternalPrescription() {
        val sampleText = """
            Rx:
            Tab Iron Folic Acid 100mg 1-0-0 OD 30 days
            Tab Calcium 500mg 0-0-1 HS 30 days
        """.trimIndent()

        val medicines = PrescriptionOcrHelper.parseMedicinesFromText(sampleText)

        assertEquals(2, medicines.size)
        val ifa = medicines.find { it.name == "Iron Folic Acid" }
        assertNotNull(ifa)
        assertEquals("100 mg", ifa?.dosage)
        assertTrue(ifa?.frequency?.contains("Once daily (morning)", ignoreCase = true) == true)
        assertEquals("30 Days", ifa?.duration)
    }

    @Test
    fun testParseMedicinesWithHandwrittenOcrNoise() {
        // Messy OCR with digit confusion, abbreviations, and informal spacing
        val noisyText = """
            Dr. S. K. Gupta MBBS
            1. Tab Paracetam0l 650 mg 1 - 0 - 1 x 3d
            2. Cap Augmentn 625mg TDS 5/7
            3. Tab Pantocid 40mg OD AC 14 days
            4. Tab D0lo 650 SOS for pain
        """.trimIndent()

        val medicines = PrescriptionOcrHelper.parseMedicinesFromText(noisyText)

        assertEquals(4, medicines.size)

        // Paracetam0l -> Paracetamol
        val pcm = medicines.find { it.name == "Paracetamol" }
        assertNotNull(pcm)
        assertEquals("650 mg", pcm?.dosage)
        assertEquals("3 Days", pcm?.duration)

        // Augmentn -> Augmentin
        val aug = medicines.find { it.name == "Augmentin" }
        assertNotNull(aug)
        assertEquals("625 mg", aug?.dosage)
        assertTrue(aug?.frequency?.contains("3 times daily", ignoreCase = true) == true)
        assertEquals("5 Days", aug?.duration)

        // Pantocid with AC before meals
        val pnt = medicines.find { it.name == "Pantocid" }
        assertNotNull(pnt)
        assertEquals("40 mg", pnt?.dosage)
        assertTrue(pnt?.frequency?.contains("Before Food", ignoreCase = true) == true)
        assertEquals("14 Days", pnt?.duration)

        // D0lo -> Dolo / Dolo 650 with SOS
        val dolo = medicines.find { it.name.startsWith("Dolo") }
        assertNotNull(dolo)
        assertTrue(dolo?.frequency?.contains("As needed", ignoreCase = true) == true)
    }

    @Test
    fun testParseMedicinesWithNoRecognizedMedicines() {
        val gibberish = "Notes: Rest well and hydrate with coconut water."
        val medicines = PrescriptionOcrHelper.parseMedicinesFromText(gibberish)
        assertTrue(medicines.isEmpty())
    }

    @Test
    fun testNoFalsePositivesOnClinicalHeadersAndAdvice() {
        // Real-world prescription text with headers, visiting hours, symptoms, advice, and diagnosis
        // Previously, "hours"/"visitors" triggered "ORS", "if a" triggered "IFA", "cough" triggered "Cough Syrup"
        val nonPrescriptionText = """
            Government Community Health Center
            Doctor: Dr. Ramesh Sharma MBBS MD
            Registration No: 48291
            Clinic hours: 9:00 AM - 1:00 PM
            Notice: Visiting hours for all visitors are 4:00 PM - 6:00 PM
            If a fever occurs, consult the nearest primary health center
            Patient: Ramesh Kumar, Age: 42, Gender: Male
            Symptoms: Patient has severe cough and fever for 2 days
            Diagnosis: Acute viral upper respiratory infection
            Vitals: BP 120/80, Pulse 76, SpO2 98%, Temp 99.2 F
            Advice: Drink plenty of warm water, avoid oily and spicy food
            Instructions: Take complete bed rest and sleep well
            Follow up after 5 days with CBC blood report
        """.trimIndent()

        val medicines = PrescriptionOcrHelper.parseMedicinesFromText(nonPrescriptionText)
        // Must be empty: no phantom medicines like ORS, IFA, Cough Syrup, etc.
        assertTrue("Expected 0 medicines but found: ${medicines.map { it.name }}", medicines.isEmpty())
    }

    @Test
    fun testStrictWordBoundaryForAcronyms() {
        // "hours", "visitors", "words" must NEVER match "ORS"
        assertNull(PrescriptionOcrHelper.extractMedicineFromLine("Clinic hours 9am to 2pm"))
        assertNull(PrescriptionOcrHelper.extractMedicineFromLine("Important notice for all visitors"))
        assertNull(PrescriptionOcrHelper.extractMedicineFromLine("Please verify if words are recognized"))

        // "if a" must NEVER match "IFA"
        assertNull(PrescriptionOcrHelper.extractMedicineFromLine("If a patient feels pain, call doctor"))
        assertNull(PrescriptionOcrHelper.extractMedicineFromLine("Contact clinic ifany emergency occurs"))

        // Genuine ORS and IFA with word boundaries MUST match
        assertEquals("ORS", PrescriptionOcrHelper.extractMedicineFromLine("ORS 1 sachet in 1 liter water daily"))
        assertEquals("IFA", PrescriptionOcrHelper.extractMedicineFromLine("Tab IFA 100mg 1-0-0"))
        assertEquals("PCM", PrescriptionOcrHelper.extractMedicineFromLine("Tab PCM 500mg 1-0-1 (BD)"))
    }

    @Test
    fun testDigitalPrescriptionCleanParsing() {
        // Digital screen prescription with mixed generics, brands, and clear dosage/frequency
        val digitalScreenText = """
            Ramesh Health Clinic
            Dr. Priya Nair MBBS
            Date: 04/09/2026
            
            Rx:
            1. Tab Azithromycin 500mg 1-0-0 OD for 3 days
            2. Tab Levocetirizine 5mg 0-0-1 HS x 5 days
            3. Tab Pantoprazole 40mg 1-0-0 OD AC 7 days
            4. Syp Ambroxol 10ml TDS 5 days
            
            Advice: Drink boiled water. Avoid cold drinks.
            Review in 5 days.
        """.trimIndent()

        val medicines = PrescriptionOcrHelper.parseMedicinesFromText(digitalScreenText)

        assertEquals(4, medicines.size)
        assertEquals("Azithromycin", medicines[0].name)
        assertEquals("500 mg", medicines[0].dosage)
        assertTrue(medicines[0].frequency.contains("Once daily (morning)", ignoreCase = true))

        assertEquals("Levocetirizine", medicines[1].name)
        assertEquals("5 mg", medicines[1].dosage)
        assertTrue(medicines[1].frequency.contains("bedtime", ignoreCase = true))

        assertEquals("Pantoprazole", medicines[2].name)
        assertEquals("40 mg", medicines[2].dosage)
        assertTrue(medicines[2].frequency.contains("Before Food", ignoreCase = true))

        assertEquals("Ambroxol", medicines[3].name)
        assertEquals("10 ml", medicines[3].dosage)
        assertTrue(medicines[3].frequency.contains("3 times daily", ignoreCase = true))
    }

    @Test
    fun testStructuredPrefixExtractionForUnlistedMedicines() {
        // A doctor writes an unlisted specialty medication with clear "Tab <Name> <Dosage> <Frequency>" syntax
        val line = "Tab Gabapentin 300mg 0-0-1 at night"
        val med = PrescriptionOcrHelper.extractMedicineFromLine(line)
        assertEquals("Gabapentin", med)
    }

    @Test
    fun testCleanOcrTextSolitaryNoiseRemoval() {
        val rawNoise = """
            ~
            Dr. Anita Roy
            |
            ^
            Tab Paracetamol 500mg 1 - 0 - 1
            `
            #
        """.trimIndent()

        val cleaned = PrescriptionOcrHelper.cleanOcrText(rawNoise)
        assertFalse(cleaned.contains("~"))
        assertFalse(cleaned.contains("^"))
        assertFalse(cleaned.contains("`"))
        assertFalse(cleaned.contains("#"))
        assertTrue(cleaned.contains("1-0-1"))
        assertTrue(cleaned.contains("Paracetamol"))
    }
}


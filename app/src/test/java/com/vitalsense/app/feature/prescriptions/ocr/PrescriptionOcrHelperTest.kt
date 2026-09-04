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
}

package com.vitalsense.app.core.localization

import com.vitalsense.app.core.data.model.SeverityLevel
import com.vitalsense.app.core.ui.theme.AppLanguage
import com.vitalsense.app.core.ui.theme.AppLanguageManager
import com.vitalsense.app.core.util.AudioGuidanceHelper
import org.junit.Assert.*
import org.junit.Test

class LocalizationTest {

    @Test
    fun testAllLanguagesAreDefined() {
        val languages = AppLanguage.values()
        assertEquals("VitalSense must support exactly 4 languages", 4, languages.size)

        val codes = languages.map { it.code }
        assertTrue(codes.contains("en"))
        assertTrue(codes.contains("hi"))
        assertTrue(codes.contains("ta"))
        assertTrue(codes.contains("mr"))

        for (lang in languages) {
            assertTrue("Code cannot be blank for ${lang.name}", lang.code.isNotBlank())
            assertTrue("Display name cannot be blank for ${lang.name}", lang.displayName.isNotBlank())
            assertTrue("Native name cannot be blank for ${lang.name}", lang.nativeName.isNotBlank())
        }
    }

    @Test
    fun testAppStringsArePopulatedForAllLanguages() {
        for (lang in AppLanguage.values()) {
            val strings = AppLanguageManager.getStrings(lang)
            assertNotNull("Strings must exist for ${lang.name}", strings)
            assertTrue("appName must not be blank in ${lang.name}", strings.appName.isNotBlank())
            assertTrue("tagline must not be blank in ${lang.name}", strings.tagline.isNotBlank())
            assertTrue("emergencySos must not be blank in ${lang.name}", strings.emergencySos.isNotBlank())
            assertTrue("selectLanguageTitle must not be blank in ${lang.name}", strings.selectLanguageTitle.isNotBlank())
            assertTrue("doctorReferralsTitle must not be blank in ${lang.name}", strings.doctorReferralsTitle.isNotBlank())
            assertTrue("medicineAvailabilityTitle must not be blank in ${lang.name}", strings.medicineAvailabilityTitle.isNotBlank())
            assertTrue("videoCall must not be blank in ${lang.name}", strings.videoCall.isNotBlank())
            assertTrue("voiceCall must not be blank in ${lang.name}", strings.voiceCall.isNotBlank())
            assertTrue("liveQueueTitle must not be blank in ${lang.name}", strings.liveQueueTitle.isNotBlank())
            assertTrue("triageBreakdownTitle must not be blank in ${lang.name}", strings.triageBreakdownTitle.isNotBlank())
            assertTrue("medicalHistoryTitle must not be blank in ${lang.name}", strings.medicalHistoryTitle.isNotBlank())

            // Low connectivity & Sync strings
            assertTrue("slowNetwork must not be blank in ${lang.name}", strings.slowNetwork.isNotBlank())
            assertTrue("syncing must not be blank in ${lang.name}", strings.syncing.isNotBlank())
            assertTrue("pendingChanges must not be blank in ${lang.name}", strings.pendingChanges.isNotBlank())
            assertTrue("lastSynchronized must not be blank in ${lang.name}", strings.lastSynchronized.isNotBlank())
            assertTrue("offlineSosWarning must not be blank in ${lang.name}", strings.offlineSosWarning.isNotBlank())
            assertTrue("cachedDataFreshness must not be blank in ${lang.name}", strings.cachedDataFreshness.isNotBlank())
            assertTrue("syncComplete must not be blank in ${lang.name}", strings.syncComplete.isNotBlank())
            assertTrue("manualSync must not be blank in ${lang.name}", strings.manualSync.isNotBlank())
        }
    }

    @Test
    fun testAudioGuidanceSpokenHealthSummaryInAllLanguages() {
        val patientName = "Aarav Sharma"
        for (lang in AppLanguage.values()) {
            for (severity in listOf(SeverityLevel.LOW, SeverityLevel.MODERATE, SeverityLevel.HIGH, SeverityLevel.SEVERE)) {
                val summary = AudioGuidanceHelper.getSpokenHealthSummary(
                    patientName = patientName,
                    severity = severity,
                    heartRate = 74,
                    spO2 = 98,
                    language = lang
                )
                assertNotNull("Summary cannot be null for $lang with $severity", summary)
                assertTrue("Summary cannot be blank for $lang with $severity", summary.isNotBlank())
                assertTrue("Summary must include patient first name for $lang", summary.contains("Aarav"))
            }
        }
    }

    @Test
    fun testLanguageNativeNamesMatchExpectedScripts() {
        assertEquals("English", AppLanguage.ENGLISH.nativeName)
        assertEquals("हिन्दी", AppLanguage.HINDI.nativeName)
        assertEquals("தமிழ்", AppLanguage.TAMIL.nativeName)
        assertEquals("मराठी", AppLanguage.MARATHI.nativeName)
    }
}

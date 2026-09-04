package com.vitalsense.app.feature.prescriptions.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.media.ExifInterface
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.vitalsense.app.core.data.model.PrescribedMedicine
import kotlinx.coroutines.tasks.await
import java.io.File

object PrescriptionOcrHelper {

    private val textRecognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    /**
     * Standard Indian Pharmacopeia (NLEM / Jan Aushadhi / OPD / Primary Care) medicines list.
     * Categorized for comprehensive generic and common brand recognition.
     */
    val commonMedicines = listOf(
        // Analgesics, Antipyretics & NSAIDs
        "Paracetamol", "PCM", "Crocin", "Dolo", "Dolo 650", "Calpol", "Combiflam",
        "Ibuprofen", "Meftal-Spas", "Diclofenac", "Aceclofenac", "Zerodol", "Zerodol-SP", "Voveran", "Tramadol",

        // Antibiotics & Antimicrobials
        "Amoxicillin", "Amox", "Augmentin", "Azithromycin", "Azithral", "Ciprofloxacin", "Ofloxacin",
        "Metronidazole", "Cefixime", "Taxim-O", "Ceftriaxone", "Doxycycline", "Levofloxacin", "Clavam", "Moxikind",

        // Antihistamines, Cold, Cough & Respiratory
        "Cetirizine", "Levocetirizine", "Cpm", "Allegra", "Montelukast", "Sinarest", "Cheston Cold",
        "Ambroxol", "Ascoril", "Asthalin", "Salbutamol", "Budecort", "Deriphyllin", "Cough Syrup",

        // Gastrointestinal, Antacids & Antiemetics
        "Pantoprazole", "Pantocid", "Pan-D", "Omeprazole", "Omee", "Ranitidine", "Aciloc", "Rantac",
        "Rabeprazole", "Domperidone", "Ondansetron", "Emeset", "Digene", "Gelusil", "Razo-D", "Sucralfate",

        // Chronic, Cardiovascular, Hypertension & Diabetes
        "Metformin", "Glycomet", "Amlodipine", "Telmisartan", "Telma", "Losartan", "Atorvastatin",
        "Glimepiride", "Teneligliptin", "Cilacar", "Nebicard", "Thyronorm", "Eltroxin",

        // Public Health, Maternal, Oral Rehydration & Supplements
        "ORS", "Zinc", "Iron Folic Acid", "IFA", "Albendazole", "Vitamin C", "Limcee",
        "Calcium", "Shelcal", "Multivitamin", "B-Complex", "Becosules", "Zincovit", "Neurobion", "Electral"
    )

    /**
     * Terms commonly found on prescription headers, clinical notes, advice, and examination sections
     * that must NEVER be falsely converted into medicine names.
     */
    private val NON_MEDICINE_TERMS = setOf(
        // General / English stop words
        "and", "the", "for", "with", "from", "take", "daily", "after", "before",
        "meals", "water", "food", "well", "rest", "days", "notes", "have", "dose",
        "tablet", "capsule", "syrup", "slip", "dr", "clinic", "hospital", "patient",
        "name", "date", "time", "year", "years", "male", "female", "age", "gender",
        "phone", "mobile", "address", "sign", "signature", "reg", "registration",
        "opd", "ipd", "dept", "department", "unit", "consultant", "attending",
        "room", "bed", "ward", "card", "center", "health", "care", "rural", "primary",

        // Clinical findings, vitals & symptoms
        "symptoms", "complaint", "complaints", "diagnosis", "history", "examination",
        "investigation", "investigations", "test", "tests", "report", "reports",
        "findings", "impression", "vitals", "pulse", "temp", "temperature", "bp",
        "spo2", "weight", "height", "fever", "cough", "cold", "pain", "headache",
        "vomiting", "nausea", "diarrhea", "swelling", "rash", "bleeding", "wound",
        "sugar", "glucose", "blood", "urine", "stool", "cbc", "xray", "ecg", "ultrasound",

        // Advice, instructions & non-medical words prone to false positive matches
        "advice", "advise", "instructions", "instructions:", "direction", "directions",
        "avoid", "oily", "spicy", "drink", "boiled", "warm", "milk", "diet", "sleep",
        "exercise", "walk", "follow", "followup", "review", "visit", "next",
        "hours", "visitors", "doors", "words", "errors", "sensors", "motors", "factors",
        "general", "government", "medical", "officer", "asha", "anm", "phc", "chc"
    )

    /**
     * Calculates the Levenshtein edit distance between two character sequences
     */
    fun calculateLevenshteinDistance(s1: CharSequence, s2: CharSequence): Int {
        val len1 = s1.length
        val len2 = s2.length
        var prev = IntArray(len2 + 1) { it }
        var curr = IntArray(len2 + 1)

        for (i in 1..len1) {
            curr[0] = i
            for (j in 1..len2) {
                val cost = if (s1[i - 1].equals(s2[j - 1], ignoreCase = true)) 0 else 1
                curr[j] = minOf(
                    curr[j - 1] + 1,       // insertion
                    prev[j] + 1,           // deletion
                    prev[j - 1] + cost     // substitution
                )
            }
            val temp = prev
            prev = curr
            curr = temp
        }
        return prev[len2]
    }

    /**
     * Normalizes OCR character misreads (e.g. '0' -> 'o', '1' -> 'l', '@' -> 'a')
     */
    fun normalizeOcrText(text: String): String {
        return text
            .replace('0', 'o')
            .replace('1', 'l')
            .replace('|', 'l')
            .replace('@', 'a')
            .replace('5', 's')
    }

    /**
     * Reads image EXIF orientation from file and returns clockwise rotation in degrees.
     */
    fun getExifRotation(filePath: String): Int {
        return try {
            val exif = ExifInterface(filePath)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Rotates a Bitmap by [degrees] clockwise if non-zero.
     */
    fun rotateBitmap(source: Bitmap, degrees: Int): Bitmap {
        if (degrees % 360 == 0) return source
        val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    /**
     * Checks if a line contains prescription context indicators:
     * - Dosage (500mg, 10ml, 1 tab)
     * - Frequency (1-0-1, TDS, BD, OD, HS, SOS, daily, etc.)
     * - Form prefix (Tab, Cap, Syp, Inj, Rx, 1., 2.)
     */
    fun hasPrescriptionContext(line: String): Boolean {
        val hasDosage = Regex("""\b\d+\s*(mg|ml|mcg|gm|g|tab|cap)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ||
                Regex("""\b(650|500|250|100|50|40|20|10|5)\b""").containsMatchIn(line)
        val hasFrequency = Regex("""\b(1-1-1|1-0-1|0-0-1|1-0-0|0-1-0|1-1-1-1|tds|bd|bid|od|hs|qid|sos|prn|daily|times|days?)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line)
        val hasPrefix = Regex("""\b(rx|tab|tablet|cap|capsule|syp|syrup|inj|injection|drops|ointment)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ||
                Regex("""^\s*(?:\d+[\.\)]|[-•*])\s*""").containsMatchIn(line)

        return hasDosage || hasFrequency || hasPrefix
    }

    /**
     * Identifies non-prescription header or advice lines to skip false medicine matches.
     */
    fun isHeaderOrAdviceLine(line: String): Boolean {
        val trimmed = line.trim()
        val lower = trimmed.lowercase()

        // Common header / metadata prefixes
        val nonPrescriptionPrefixes = listOf(
            "patient:", "pt:", "name:", "age:", "gender:", "sex:", "date:", "time:",
            "doctor:", "dr.", "dr:", "clinic:", "hospital:", "address:", "reg:", "reg no:",
            "symptoms:", "complaints:", "c/o:", "diagnosis:", "dx:", "provisional diagnosis:",
            "vitals:", "bp:", "pulse:", "temp:", "spo2:", "investigation:", "tests:",
            "advice:", "instructions:", "note:", "notes:", "review:", "follow up:", "follow-up:"
        )

        for (prefix in nonPrescriptionPrefixes) {
            if (lower.startsWith(prefix)) {
                // If line explicitly starts with Diagnosis/Symptoms/Advice without prescription indicators, skip
                if (!hasPrescriptionContext(line)) return true
            }
        }

        // Lines that are purely advice
        if (lower.startsWith("drink ") || lower.startsWith("avoid ") || lower.startsWith("bed rest") ||
            lower.startsWith("rest well") || lower.startsWith("hydrate ") || lower.startsWith("diet:")) {
            return true
        }

        return false
    }

    /**
     * Fuzzy matches a token or word against the standard medicine list using Levenshtein distance.
     * Guaranteed safe against hallucinating stop words, symptoms, or non-medicine terms.
     */
    fun fuzzyMatchMedicine(rawWord: String): String? {
        val cleanWord = rawWord.trim().trim(',', '.', ':', ';', '(', ')', '[', ']', '-', '+', '/', '\\', '"', '\'')
        val lowerClean = cleanWord.lowercase()

        if (cleanWord.length < 3 || lowerClean in NON_MEDICINE_TERMS) return null

        // 1. Direct case-insensitive match
        commonMedicines.firstOrNull { it.equals(cleanWord, ignoreCase = true) }?.let { return it }

        // 2. Normalized OCR substitution match (e.g. 'Paracetam0l' -> 'Paracetamol', 'D0lo' -> 'Dolo', 'Combif1am' -> 'Combiflam')
        val normalized = normalizeOcrText(cleanWord)
        commonMedicines.firstOrNull { it.equals(normalized, ignoreCase = true) }?.let { return it }

        val normLen = normalized.length
        // For short words (<= 4 characters), strictly forbid fuzzy edit-distance matching
        // to prevent false positives like "words" -> ORS or "door" -> Dolo
        if (normLen <= 4) return null

        // 3. Levenshtein edit distance thresholding for words > 4 chars
        var bestMatch: String? = null
        var minDistance = Int.MAX_VALUE

        for (medicine in commonMedicines) {
            val candidate = medicine.substringBefore(" ").replace("-", "")
            val candLen = candidate.length
            if (kotlin.math.abs(normLen - candLen) > 2) continue

            val distance = calculateLevenshteinDistance(normalized, candidate)
            val maxLen = maxOf(normLen, candLen)

            // Dynamic strict threshold:
            // <= 7 chars: max 1 edit (similarity >= 80%)
            // > 7 chars: max 2 edits (similarity >= 75%)
            val allowedDistance = if (maxLen <= 7) 1 else 2

            // Require first letter match (or common OCR confusion O/0, I/1/l) to avoid spurious matches
            val firstCharMatches = normalized.first().equals(candidate.first(), ignoreCase = true) ||
                    (normalized.first() in "0ol" && candidate.first() in "Oo") ||
                    (normalized.first() in "1il" && candidate.first() in "IiLl")

            if (distance <= allowedDistance && distance < minDistance && firstCharMatches) {
                minDistance = distance
                bestMatch = medicine
            }
        }

        return bestMatch
    }

    /**
     * Extracts a verified medicine name from a prescription line with strict word boundary checks
     * and non-medicine term filtering.
     */
    fun extractMedicineFromLine(line: String): String? {
        val trimmed = line.trim()
        if (trimmed.isBlank() || isHeaderOrAdviceLine(trimmed)) return null

        // 1. Check multi-word medicines first (e.g., "Iron Folic Acid", "Dolo 650", "Cheston Cold", "Cough Syrup")
        val multiWordMedicines = commonMedicines.filter { it.contains(" ") }
        for (med in multiWordMedicines) {
            val pattern = Regex("""\b${Regex.escape(med)}\b""", RegexOption.IGNORE_CASE)
            if (pattern.containsMatchIn(trimmed)) {
                return med
            }
        }

        // 2. Check short acronym medicines with STRICT isolated word boundary (ORS, IFA, PCM, CPM, etc.)
        val shortAcronyms = listOf("ORS", "IFA", "PCM", "CPM", "AMOX", "ZINC")
        for (acronym in shortAcronyms) {
            val acronymPattern = Regex("""\b${acronym}\b""", RegexOption.IGNORE_CASE)
            if (acronymPattern.containsMatchIn(trimmed)) {
                return commonMedicines.first { it.equals(acronym, ignoreCase = true) }
            }
        }

        // 3. Check single-word medicines with exact word boundaries
        val singleWordMedicines = commonMedicines.filter { !it.contains(" ") && it !in shortAcronyms }
        for (med in singleWordMedicines) {
            val pattern = Regex("""\b${Regex.escape(med)}\b""", RegexOption.IGNORE_CASE)
            if (pattern.containsMatchIn(trimmed)) {
                return med
            }
        }

        // 4. Token-by-token normalized OCR & fuzzy matching (only on lines with medical context or prefix)
        val tokens = trimmed.split(Regex("""[\s,;\(\)\[\]\+\*]+"""))
        for (token in tokens) {
            val clean = token.trim('-', '.', '/', ':')
            if (clean.length < 3 || clean.lowercase() in NON_MEDICINE_TERMS) continue

            val matched = fuzzyMatchMedicine(clean)
            if (matched != null) {
                return matched
            }
        }

        // 5. Structure-based extraction fallback:
        // If line has explicit medical prefix like "Tab <Name> 500mg" or "Cap <Name> 1-0-1",
        // capture the actual prescribed medicine even if unlisted in commonMedicines.
        val structuredPrefixRegex = Regex("""\b(?:Tab|Tablet|Cap|Capsule|Syp|Syrup|Inj|Injection)\s+([A-Za-z0-9\-]+)\b""", RegexOption.IGNORE_CASE)
        val prefixMatch = structuredPrefixRegex.find(trimmed)
        if (prefixMatch != null) {
            val extractedName = prefixMatch.groupValues[1].trim()
            if (extractedName.length >= 3 && extractedName.lowercase() !in NON_MEDICINE_TERMS) {
                // Return formatted extracted name
                return extractedName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
        }

        return null
    }

    /**
     * Performs on-device text recognition on a captured photo file with EXIF orientation correction
     * and noise filtering to prevent hallucinated characters.
     */
    suspend fun recognizeTextFromFile(context: Context, file: File): String {
        return try {
            // ML Kit InputImage.fromFilePath automatically inspects ExifInterface
            // and applies the correct rotation angle to the image buffer
            val uri = Uri.fromFile(file)
            val image = InputImage.fromFilePath(context, uri)
            val visionText = textRecognizer.process(image).await()
            val text = visionText.text.trim()

            if (text.isNotBlank()) {
                cleanOcrText(text)
            } else {
                // Secondary fallback: manual EXIF rotation and bitmap processing
                val exifDegrees = getExifRotation(file.absolutePath)
                val rawBitmap = BitmapFactory.decodeFile(file.absolutePath)
                if (rawBitmap != null) {
                    val oriented = rotateBitmap(rawBitmap, exifDegrees)
                    val fallbackImage = InputImage.fromBitmap(oriented, 0)
                    val fallbackVision = textRecognizer.process(fallbackImage).await()
                    cleanOcrText(fallbackVision.text.trim())
                } else {
                    ""
                }
            }
        } catch (e: Exception) {
            "OCR Processing Error: ${e.localizedMessage ?: e.message}"
        }
    }

    /**
     * Performs on-device text recognition using ML Kit from an in-memory Bitmap.
     * Ensures any orientation rotation is respected.
     */
    suspend fun recognizeTextFromBitmap(bitmap: Bitmap, rotationDegrees: Int = 0): String {
        return try {
            val oriented = if (rotationDegrees != 0) rotateBitmap(bitmap, rotationDegrees) else bitmap
            val image = InputImage.fromBitmap(oriented, 0)
            val visionText = textRecognizer.process(image).await()
            cleanOcrText(visionText.text.trim())
        } catch (e: Exception) {
            "OCR Processing Error: ${e.localizedMessage ?: e.message}"
        }
    }

    /**
     * Filters out solitary OCR punctuation noise, screen moiré artifacts, and cleans up text formatting.
     */
    fun cleanOcrText(rawText: String): String {
        val lines = rawText.split("\n")
        val cleanedLines = mutableListOf<String>()

        for (line in lines) {
            val trimmed = line.trim()
            // Skip solitary noise lines (e.g. "~", "^", "|", "`", single punctuation marks)
            if (trimmed.length <= 1 && !trimmed.all { it.isLetterOrDigit() }) continue
            if (trimmed.all { it in "-_=~`|/\\.,;:^\"'*#" }) continue

            // Normalize OCR spacing around hyphens in frequencies (e.g. "1 - 0 - 1" -> "1-0-1")
            val normalizedFrequency = trimmed
                .replace(Regex("""(\d)\s*-\s*(\d)\s*-\s*(\d)"""), "$1-$2-$3")
                .replace(Regex("""\b[lI]\s*-\s*0\s*-\s*[lI]\b"""), "1-0-1")
                .replace(Regex("""\b[lI]\s*-\s*[lI]\s*-\s*[lI]\b"""), "1-1-1")

            cleanedLines.add(normalizedFrequency)
        }

        return cleanedLines.joinToString("\n")
    }

    /**
     * Decodes medical shorthand frequency into plain language
     */
    fun extractFrequency(line: String): String {
        val lower = line.lowercase()

        val baseFrequency = when {
            // 1-1-1 or TDS
            lower.contains("1-1-1") ||
            Regex("""\b(tds|tid|thrice|3 times)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "3 times daily (morning, afternoon & night)"

            // 1-0-1 or BD
            lower.contains("1-0-1") ||
            Regex("""\b(bd|bid|twice|2 times)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Twice daily (morning & night after food)"

            // 0-0-1 or HS (bedtime)
            lower.contains("0-0-1") ||
            Regex("""\b(hs|h\.s\.|bedtime|night)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Once daily at bedtime (night)"

            // 1-0-0 or OD (once daily morning)
            lower.contains("1-0-0") ||
            Regex("""\b(od|o\.d\.|once daily|morning)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Once daily (morning)"

            // 0-1-0 (afternoon)
            lower.contains("0-1-0") || lower.contains("afternoon") ->
                "Once daily (afternoon)"

            // 1-1-1-1 or QID
            lower.contains("1-1-1-1") ||
            Regex("""\b(qid|q\.i\.d\.|4 times)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "4 times daily"

            // SOS / PRN
            Regex("""\b(sos|s\.o\.s\.|prn|as needed|when needed)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "As needed for pain/fever (जब ज़रूरत हो)"

            else -> "Once daily"
        }

        val mealTiming = extractMealTiming(line)
        return if (mealTiming.isNotEmpty() && !baseFrequency.contains("after food", ignoreCase = true)) {
            "$baseFrequency · $mealTiming"
        } else {
            baseFrequency
        }
    }

    /**
     * Decodes meal administration timing (AC = before food, PC = after food)
     */
    fun extractMealTiming(line: String): String {
        return when {
            Regex("""\b(ac|a\.c\.|empty stomach|before meals?|before food)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Take Before Food (खाली पेट)"
            Regex("""\b(pc|p\.c\.|after meals?|after food)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Take After Food (भोजन के बाद)"
            else -> ""
        }
    }

    /**
     * Extracts numerical and unit dosage (e.g. 650mg, 500 mg, 10ml)
     */
    fun extractDosage(line: String): String {
        val dosageRegex = Regex("""\b(\d+(\.\d+)?)\s*(mg|ml|mcg|gm|g|tab|tablet|cap|capsule)\b""", RegexOption.IGNORE_CASE)
        val match = dosageRegex.find(line)
        if (match != null) {
            val amount = match.groupValues[1]
            val unit = match.groupValues[3].lowercase()
            val normalizedUnit = when (unit) {
                "mg" -> "mg"
                "ml" -> "ml"
                "mcg" -> "mcg"
                "gm", "g" -> "g"
                "tab", "tablet" -> "Tablet"
                "cap", "capsule" -> "Capsule"
                else -> unit
            }
            return "$amount $normalizedUnit"
        }

        // Common rural clinic dosage cues
        return when {
            line.contains("650") -> "650 mg"
            line.contains("500") -> "500 mg"
            line.contains("250") -> "250 mg"
            line.contains("100") -> "100 mg"
            line.contains("50") -> "50 mg"
            line.contains("40") -> "40 mg"
            line.contains("20") -> "20 mg"
            line.contains("10") -> "10 mg"
            line.contains("5") -> "5 mg"
            else -> "Standard dose"
        }
    }

    /**
     * Extracts treatment duration (e.g. 5 days, 1 week, 5/7)
     */
    fun extractDuration(line: String): String {
        val durationRegex = Regex("""\b(\d+)\s*(days?|d|weeks?|w|months?|m|/7)\b""", RegexOption.IGNORE_CASE)
        val match = durationRegex.find(line)
        if (match != null) {
            val count = match.groupValues[1]
            val unit = match.groupValues[2].lowercase()
            return when {
                unit.startsWith("w") -> "${count.toInt() * 7} Days"
                unit.startsWith("m") -> "${count.toInt() * 30} Days"
                unit == "/7" -> "$count Days"
                else -> "$count Days"
            }
        }
        return "5 Days"
    }

    /**
     * Parses raw OCR text into structured PrescribedMedicine objects with strict hallucination guards.
     */
    fun parseMedicinesFromText(rawText: String): List<PrescribedMedicine> {
        val medicines = mutableListOf<PrescribedMedicine>()
        val lines = rawText.split("\n")

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isBlank() || isHeaderOrAdviceLine(trimmed)) continue

            val matchedMed = extractMedicineFromLine(trimmed)

            if (matchedMed != null) {
                val frequency = extractFrequency(trimmed)
                val dosage = extractDosage(trimmed)
                val duration = extractDuration(trimmed)

                // Avoid duplicate medicine entries on adjacent lines
                if (medicines.none { it.name.equals(matchedMed, ignoreCase = true) }) {
                    medicines.add(
                        PrescribedMedicine(
                            name = matchedMed,
                            dosage = dosage,
                            frequency = frequency,
                            duration = duration,
                            quantity = 10
                        )
                    )
                }
            }
        }

        return medicines
    }
}


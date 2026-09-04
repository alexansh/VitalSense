package com.vitalsense.app.feature.prescriptions.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
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
     * Standard Indian Pharmacopeia (NLEM / Jan Aushadhi / OPD) medicines list
     */
    val commonMedicines = listOf(
        // Analgesics & Antipyretics
        "Paracetamol", "PCM", "Crocin", "Dolo", "Dolo 650", "Calpol", "Combiflam", "Ibuprofen", "Meftal-Spas", "Diclofenac",
        // Antibiotics & Antimicrobials
        "Amoxicillin", "Amox", "Augmentin", "Azithromycin", "Azithral", "Ciprofloxacin", "Ofloxacin", "Metronidazole", "Cefixime",
        // Antihistamines & Cold/Allergy
        "Cetirizine", "Levocetirizine", "Cpm", "Allegra", "Montelukast", "Sinarest", "Cheston Cold",
        // Gastrointestinal & Antacids
        "Pantoprazole", "Pantocid", "Pan-D", "Omeprazole", "Omee", "Ranitidine", "Domperidone", "Ondansetron", "Digene", "Gelusil", "Razo-D",
        // Chronic, Cardiovascular & Diabetes
        "Metformin", "Glycomet", "Amlodipine", "Telmisartan", "Telma", "Losartan", "Atorvastatin", "Glimepiride",
        // Public Health, Maternal & Supplements
        "ORS", "Zinc", "Iron Folic Acid", "IFA", "Albendazole", "Vitamin C", "Calcium", "Shelcal", "Multivitamin", "B-Complex", "Becosules", "Cough Syrup"
    )

    private val STOP_WORDS = setOf(
        "and", "the", "for", "with", "from", "take", "daily", "after", "before",
        "meals", "water", "food", "well", "rest", "days", "notes", "have", "dose",
        "tablet", "capsule", "syrup", "slip", "dr", "clinic", "hospital", "patient"
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
     * Fuzzy matches a token or word against the standard medicine list using Levenshtein distance
     */
    fun fuzzyMatchMedicine(rawWord: String): String? {
        val cleanWord = rawWord.trim().trim(',', '.', ':', ';', '(', ')', '[', ']', '-', '+')
        if (cleanWord.length < 3 || cleanWord.lowercase() in STOP_WORDS) return null

        // 1. Direct case-insensitive match
        commonMedicines.firstOrNull { it.equals(cleanWord, ignoreCase = true) }?.let { return it }

        // 2. Normalized OCR substitution match (e.g. 'Paracetam0l' -> 'Paracetamol', 'D0lo' -> 'Dolo')
        val normalized = normalizeOcrText(cleanWord)
        commonMedicines.firstOrNull { it.equals(normalized, ignoreCase = true) }?.let { return it }

        val normLen = normalized.length
        // For words <= 4 characters, only allow exact or normalized match
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

            // Dynamic threshold: 1 for <= 7 chars, 2 for > 7 chars
            val allowedDistance = if (maxLen <= 7) 1 else 2

            if (distance <= allowedDistance && distance < minDistance) {
                minDistance = distance
                bestMatch = medicine
            }
        }

        return bestMatch
    }

    /**
     * Pre-processes an image Bitmap for optimal OCR text extraction:
     * 1. Downscales if oversized (>2000px) to prevent OOM and speed up inference
     * 2. Converts to high-contrast grayscale (saturation = 0)
     * 3. Stretches contrast curve to push paper grain to white and ink to crisp black
     */
    fun enhanceBitmapForOcr(original: Bitmap): Bitmap {
        val width = original.width
        val height = original.height
        val maxDimension = 2000f

        val scaleFactor = if (maxOf(width, height) > maxDimension) {
            maxDimension / maxOf(width, height)
        } else {
            1.0f
        }

        val targetW = (width * scaleFactor).toInt().coerceAtLeast(1)
        val targetH = (height * scaleFactor).toInt().coerceAtLeast(1)

        val enhanced = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(enhanced)

        // 1. Grayscale saturation reduction
        val grayscaleMatrix = ColorMatrix().apply { setSaturation(0f) }

        // 2. High Contrast stretch: scale = 1.6f, brightness offset = -30f
        val contrast = 1.6f
        val brightnessOffset = -30f
        val contrastMatrix = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, brightnessOffset,
                0f, contrast, 0f, 0f, brightnessOffset,
                0f, 0f, contrast, 0f, brightnessOffset,
                0f, 0f, 0f, 1f, 0f
            )
        )

        grayscaleMatrix.postConcat(contrastMatrix)

        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(grayscaleMatrix)
            isFilterBitmap = true
        }

        val srcRect = Rect(0, 0, width, height)
        val dstRect = Rect(0, 0, targetW, targetH)
        canvas.drawBitmap(original, srcRect, dstRect, paint)

        return enhanced
    }

    /**
     * Binarizes a grayscale bitmap using adaptive thresholding for high-contrast ink isolation
     */
    fun binarizeBitmap(grayscaleBitmap: Bitmap, threshold: Int = 135): Bitmap {
        val width = grayscaleBitmap.width
        val height = grayscaleBitmap.height
        val binarized = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(width * height)
        grayscaleBitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in pixels.indices) {
            val pixel = pixels[i]
            val red = (pixel shr 16) and 0xFF
            val green = (pixel shr 8) and 0xFF
            val blue = pixel and 0xFF
            val luminance = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()

            pixels[i] = if (luminance < threshold) {
                0xFF000000.toInt() // crisp dark ink
            } else {
                0xFFFFFFFF.toInt() // pure white paper
            }
        }
        binarized.setPixels(pixels, 0, width, 0, 0, width, height)
        return binarized
    }

    /**
     * Performs on-device text recognition on a captured photo file with automatic bitmap enhancement
     */
    suspend fun recognizeTextFromFile(context: Context, file: File): String {
        return try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                val enhanced = enhanceBitmapForOcr(bitmap)
                recognizeTextFromBitmap(enhanced)
            } else {
                val uri = Uri.fromFile(file)
                val image = InputImage.fromFilePath(context, uri)
                val visionText = textRecognizer.process(image).await()
                visionText.text.trim()
            }
        } catch (e: Exception) {
            "OCR Processing Error: ${e.localizedMessage ?: e.message}"
        }
    }

    /**
     * Performs on-device text recognition using ML Kit from an in-memory Bitmap
     */
    suspend fun recognizeTextFromBitmap(bitmap: Bitmap, rotationDegrees: Int = 0): String {
        return try {
            val image = InputImage.fromBitmap(bitmap, rotationDegrees)
            val visionText = textRecognizer.process(image).await()
            visionText.text.trim()
        } catch (e: Exception) {
            "OCR Processing Error: ${e.localizedMessage ?: e.message}"
        }
    }

    /**
     * Decodes medical shorthand frequency into plain language
     */
    fun extractFrequency(line: String): String {
        val lower = line.lowercase()

        val baseFrequency = when {
            // 1-1-1 or TDS
            lower.contains("1-1-1") || lower.contains("1 - 1 - 1") ||
            Regex("""\b(tds|tid|thrice|3 times)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "3 times daily (morning, afternoon & night)"

            // 1-0-1 or BD
            lower.contains("1-0-1") || lower.contains("1 - 0 - 1") ||
            Regex("""\b(bd|bid|twice|2 times)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Twice daily (morning & night after food)"

            // 0-0-1 or HS (bedtime)
            lower.contains("0-0-1") || lower.contains("0 - 0 - 1") ||
            Regex("""\b(hs|h\.s\.|bedtime|night)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Once daily at bedtime (night)"

            // 1-0-0 or OD (once daily morning)
            lower.contains("1-0-0") || lower.contains("1 - 0 - 0") ||
            Regex("""\b(od|o\.d\.|once daily|morning)\b""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Once daily (morning)"

            // 0-1-0 (afternoon)
            lower.contains("0-1-0") || lower.contains("0 - 1 - 0") ||
            lower.contains("afternoon") ->
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
     * Parses raw OCR text into structured PrescribedMedicine objects with fuzzy matching & shorthand decoding
     */
    fun parseMedicinesFromText(rawText: String): List<PrescribedMedicine> {
        val medicines = mutableListOf<PrescribedMedicine>()
        val lines = rawText.split("\n")

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isBlank()) continue

            // 1. Check direct substring match
            var matchedMed = commonMedicines.firstOrNull {
                trimmed.contains(it, ignoreCase = true)
            }

            // 2. If no direct substring match, check word tokens via fuzzy matching
            if (matchedMed == null) {
                val words = trimmed.split(" ", "\t", ",", "-", "/")
                for (word in words) {
                    val fuzzy = fuzzyMatchMedicine(word)
                    if (fuzzy != null) {
                        matchedMed = fuzzy
                        break
                    }
                }
            }

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

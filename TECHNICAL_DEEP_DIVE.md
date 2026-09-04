# 🔬 VitalSense — Technical Deep Dive: How Every Component Works & Why It Was Chosen

**Project:** VitalSense (SehatSetu) — SIH 26133  
**Platform:** Android Native + Firebase Backend + Next.js Landing Page  
**Team Reference — For Internal Understanding & Judge Technical Grilling**

---

## Table of Contents
1. [Language & Build Toolchain](#1-language--build-toolchain)
2. [UI Framework — Jetpack Compose & Material 3](#2-ui-framework--jetpack-compose--material-3)
3. [Architecture — Clean Architecture + MVVM](#3-architecture--clean-architecture--mvvm)
4. [Dependency Injection — Dagger Hilt](#4-dependency-injection--dagger-hilt)
5. [Local Database — Room SQLite (Offline-First)](#5-local-database--room-sqlite-offline-first)
6. [Offline Sync Engine — Outbox Pattern + WorkManager](#6-offline-sync-engine--outbox-pattern--workmanager)
7. [Cloud Backend — Firebase Suite](#7-cloud-backend--firebase-suite)
8. [AI / ML — Prescription OCR Pipeline](#8-ai--ml--prescription-ocr-pipeline)
9. [Clinical Decision Support — TriageEngine](#9-clinical-decision-support--triageengine)
10. [Tele-Consultation — CameraX & AudioRecord](#10-tele-consultation--camerax--audiorecord)
11. [Localization — Two-Tier 4-Language System](#11-localization--two-tier-4-language-system)
12. [Maps & Geolocation](#12-maps--geolocation)
13. [Security & Privacy](#13-security--privacy)
14. [Cloud Functions — Server-Side Automation](#14-cloud-functions--server-side-automation)
15. [Web Landing Page — Next.js](#15-web-landing-page--nextjs)
16. [Testing Strategy](#16-testing-strategy)
17. [Complete Dependency Map](#17-complete-dependency-map)

---

## 1. Language & Build Toolchain

### Kotlin 1.9.22
- **What it does:** Kotlin is the primary language for the entire Android app — 100% Kotlin, zero Java files.
- **Why we chose it:** Google officially recommends Kotlin for Android. It gives us **null safety** (prevents NullPointerException crashes that plague rural field workers who can't debug), **coroutines** (lightweight threads for background work without blocking the UI), **sealed classes** (for modeling states like `PENDING_SYNC`, `SYNCED`, `FAILED` exhaustively), and **extension functions** (cleaner code).
- **Key features we use:**
  - `Coroutines` + `Flow` — for reactive database observations and async network calls
  - `Sealed interfaces` — for exhaustive `when` statements on role types (`PATIENT`, `ASHA`, `DOCTOR`, `ADMIN`)
  - `Data classes` — for all Room entities and domain models (auto-generates `equals()`, `hashCode()`, `copy()`)

### Gradle 8.7 + Android Gradle Plugin 8.7.0
- **What it does:** Our build system that compiles Kotlin, processes annotations (KAPT), bundles resources, and produces the final APK.
- **Why this version:** AGP 8.7 supports the latest Android SDK 34/35, has incremental build caching (faster recompiles), and R8 code shrinking for smaller APK sizes.

### KAPT (Kotlin Annotation Processing Tool)
- **What it does:** Generates boilerplate code at compile time for Room DAOs, Hilt dependency injection, and type converters.
- **Why needed:** Room and Hilt both rely on annotation processing to generate implementation classes. For example, when we annotate `@Dao interface VitalSenseDao`, KAPT generates the full SQLite implementation.

---

## 2. UI Framework — Jetpack Compose & Material 3

### Jetpack Compose (BOM 2024.02.01)
- **What it does:** Google's modern declarative UI toolkit. Instead of writing XML layout files, we write UI as Kotlin functions.
- **How it works technically:**
  - UI is described as `@Composable` functions that re-execute when state changes (recomposition).
  - Uses a virtual tree (Composition) similar to React's virtual DOM — only changed nodes are re-rendered.
  - `StateFlow` from ViewModels triggers recomposition automatically.
- **Why we chose it over XML Views:**
  - **50-70% fewer lines of code** than equivalent XML layouts
  - **Hot reload** during development — instant visual feedback
  - **Type-safe navigation** — no more `findFragmentById` crashes
  - **Composition over inheritance** — compose small reusable components like `GlumeStatCard`, `SeverityBadge`, `StatusHaloCard`
  - XML is legacy; all new Google Android samples use Compose

### Material 3 (Material Design 3) with Custom Design Tokens
- **What it does:** Google's latest design system with dynamic theming, rounded shapes, and elevated surfaces.
- **Our custom tokens:**
  - **Glume Design System** — custom color palette: `GlumePrimaryPurple` (primary), `GlumeSuccessMint` (healthy vitals), `GlumeAlertCoral` (critical alerts)
  - **Sunlight High-Contrast Mode** — toggleable high-luminance palette for ASHA workers doing outdoor field surveys under harsh Indian sunlight. Standard phone screens wash out at 50,000+ lux; our palette pushes contrast ratios above 7:1 even outdoors.
- **Why Material 3:** Government app credibility, built-in accessibility support (minimum 48dp touch targets), and dynamic color theming.

### Key Custom UI Components
| Component | File | Purpose |
|:---|:---|:---|
| `GlumeStatCard` | `core/ui/components/GlumeStatCard.kt` | Reusable metric card (bed count, case count, stock level) with icon, value, and trend indicator |
| `SeverityBadge` | `core/ui/components/SeverityBadge.kt` | Color-coded pill badge showing LOW / MODERATE / HIGH / SEVERE |
| `StatusHaloCard` | `core/ui/components/StatusHaloCard.kt` | Card with animated halo ring indicating sync status or alert |
| `FloatingBottomNavBar` | `core/ui/components/FloatingBottomNavBar.kt` | Pill-shaped floating nav bar for role-specific navigation |
| `TopRoleSwitcherBar` | `core/ui/components/TopRoleSwitcherBar.kt` | Top bar that switches between 4 user roles |
| `VitalSenseDialog` | `core/ui/components/VitalSenseDialog.kt` | Standardized dialog template used across all modals |
| `AdaptiveScreenContainer` | `core/ui/util/AdaptiveScreenContainer.kt` | Responsive container that adapts to phone vs tablet screens |

---

## 3. Architecture — Clean Architecture + MVVM

### Clean Architecture (3-Layer Separation)
```
┌──────────────────────────────┐
│   UI Layer (Compose Screens) │  ← What the user sees
│   Observes StateFlow         │
├──────────────────────────────┤
│   Domain Layer (Models,      │  ← Business rules (TriageEngine, ConsentManager)
│   Use Cases, Enums)          │
├──────────────────────────────┤
│   Data Layer (Room, Firebase,│  ← Where data lives and syncs
│   Repository, Outbox)        │
└──────────────────────────────┘
```

- **Why Clean Architecture:** Testability. The `TriageEngine` has zero Android dependencies — it's a pure Kotlin `object` we can unit test with plain JUnit without an emulator. Same for `PrescriptionOcrHelper`'s fuzzy matching logic.

### MVVM (Model-View-ViewModel)
- **How it works:**
  1. **Model** = Room entities + domain data classes (e.g., `PatientEntity`, `ConditionRecord`)
  2. **View** = Composable screens (e.g., `PatientDashboardScreen`, `DoctorOpdScreen`)
  3. **ViewModel** = Holds UI state via `StateFlow`, processes user actions, calls repository methods
- **Data flow:** User action → ViewModel → Repository → Room DAO → Flow emits → Compose recomposes
- **Why MVVM:** Survives configuration changes (screen rotation), lifecycle-aware, and the officially recommended pattern by Google for Compose.

---

## 4. Dependency Injection — Dagger Hilt 2.51

### What it does:
Hilt automatically creates and provides instances of classes like `VitalSenseRepository`, `VitalSenseDatabase`, `FirebaseFirestore` wherever they're needed, without manual instantiation.

### How it works technically:
1. `@HiltAndroidApp` on `VitalSenseApp.kt` — generates the root DI component
2. `@Module` + `@Provides` in `DatabaseModule.kt` and `FirebaseModule.kt` — declare how to create singletons
3. `@Inject` on ViewModel constructors — Hilt auto-injects the repository
4. `@HiltViewModel` on ViewModels — enables `hiltViewModel()` in Compose

### Why Hilt over manual DI or Koin:
- **Compile-time safety** — dependency graph errors are caught at build time, not at runtime crash in a rural field
- **Google's official DI** for Android — best documentation, community, and Compose integration via `hilt-navigation-compose`
- **Scoping** — `@Singleton` ensures one database instance app-wide

---

## 5. Local Database — Room SQLite (v2.6.1)

### What it does:
Room is an abstraction layer over SQLite that provides compile-time verified SQL queries, reactive `Flow` observations, and type-safe entity mapping.

### Our database schema (25+ tables):
The `VitalSenseDatabase` manages tables for: `patients`, `doctors`, `asha_workers`, `villages`, `condition_records`, `prescriptions`, `appointments`, `queue_entries`, `ipd_beds`, `ot_surgery_bookings`, `biomedical_equipment`, `blood_stock`, `lab_reports`, `opd_tokens`, `immunization_records`, `daily_rounds`, `dispensary_stock`, `broadcast_notices`, `medical_history`, `referrals`, `audit_logs`, `call_logs`, `nearby_pharmacy_cache`, `medical_certificates`, `government_schemes`, `asha_medicines`, `disease_trend_records`, `doctor_day_slots`, and the `outbox_records` sync queue.

### Why Room over raw SQLite or Realm:
- **Compile-time SQL verification** — misspelled column names are caught during build
- **Kotlin Flow integration** — `@Query` returns `Flow<List<PatientEntity>>` that auto-emits when data changes, driving reactive Compose UIs
- **Type converters** — `Converters.kt` handles `List<String>` ↔ JSON, `Enum` ↔ String conversions automatically
- **Migration support** — safe schema upgrades without data loss in the field

### Why it's critical for rural India:
Room is the **single source of truth**. Every screen reads from Room, never directly from Firebase. This means the app works identically whether the ASHA worker is in a village with zero connectivity or a town with 4G.

---

## 6. Offline Sync Engine — Outbox Pattern + WorkManager

### The Outbox Pattern
This is the **most technically innovative component** of VitalSense.

**How it works (step by step):**
1. User performs an action (logs symptom, books appointment, writes prescription)
2. Data is saved **immediately** to local Room SQLite
3. A corresponding `OutboxEntity` record is created with `actionType` (e.g., `"CONDITION_RECORD"`, `"PRESCRIPTION"`) and `payloadJson` (serialized data)
4. The UI updates **instantly** from Room's reactive Flow — the user sees confirmation immediately
5. `SyncWorker` (a `CoroutineWorker` registered with WorkManager) is enqueued with `NetworkType.CONNECTED` constraint
6. When the device connects to any network, Android wakes `SyncWorker` in the background
7. `SyncWorker` queries all pending outbox records, deserializes each payload using Gson, and uploads to the matching Firestore collection
8. On Firestore success, the outbox record is deleted from Room

**Implementation in `SyncWorker.kt`:**
```kotlin
// Pseudo-flow:
pendingRecords = dao.getPendingOutboxRecords()
for (record in pendingRecords) {
    when (record.actionType) {
        "CONDITION_RECORD" -> firestoreDataSource.uploadConditionRecord(...)
        "PRESCRIPTION"     -> firestoreDataSource.uploadPrescription(...)
        "QUEUE_ENTRY"      -> firestoreDataSource.assignAuthoritativeTokenAndSave(...)
        // ... 6 more action types
    }
    dao.deleteOutboxRecord(record.id)  // Remove from outbox after success
}
```

### WorkManager (v2.9.0)
- **What it does:** Android's recommended API for durable, guaranteed background work that survives app kills and device reboots.
- **Why over AlarmManager or JobScheduler:** WorkManager is a unified API that internally uses `JobScheduler` (API 23+), `AlarmManager` (legacy), or `GCMNetworkManager` as needed. It handles doze mode, battery optimization, and backoff-retry automatically.
- **Our configuration:** `Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)` — worker only runs when internet is available.

### Why this matters for SIH:
A village ASHA worker can log 20 patient symptoms in a day with **zero internet**. All data is safe in Room. When she walks to a town with 4G coverage, WorkManager silently uploads everything in the background. Zero manual intervention. Zero data loss.

---

## 7. Cloud Backend — Firebase Suite

### Cloud Firestore
- **What it does:** NoSQL document database with real-time listeners and offline caching.
- **How we use it:** Remote storage for all clinical data. Collections: `queue_entries`, `condition_records`, `prescriptions`, `patientMedicalHistory`, `doctor_day_slots`, `queue_counters`.
- **Why Firestore:** Real-time multi-user sync (doctor sees patient queue updates instantly), serverless scaling, and built-in offline SDK caching.

### Firebase Auth
- **What it does:** Handles user authentication and identity.
- **Why:** Provides anonymous and email/password auth with UID-based security rules.

### Firebase Storage
- **What it does:** Blob storage for prescription scans, lab report PDFs, and doctor reference documents.
- **Why:** Integrates natively with Firestore security rules and Android SDK.

### Firebase Cloud Messaging (FCM)
- **What it does:** Push notifications for queue call alerts, appointment reminders, and epidemic broadcasts.
- **Implementation:** `VitalSenseMessagingService.kt` extends `FirebaseMessagingService` to handle incoming FCM payloads and display notifications.

### Firestore Security Rules (`firestore.rules`)
- Role-based access control: patients can read queue entries, only doctors can advance queue status, medical history is append-only (no deletes allowed) to preserve clinical audit trails.

---

## 8. AI / ML — Prescription OCR Pipeline

### The Problem:
Rural doctors hand-write prescriptions on paper. Patients lose them, can't read them, and have no digital record.

### Our 4-Stage Pipeline:

#### Stage 1: Document Scanning (Google ML Kit Document Scanner API)
- **Library:** `play-services-mlkit-document-scanner:16.0.0-beta1`
- **What it does:** Automatic 4-corner document detection, perspective unwarping (de-skews tilted photos), shadow suppression, and finger-removal filters.
- **Why:** Gives us a clean, flat, well-lit document image from a shaky phone camera photo.

#### Stage 2: Image Pre-processing (`enhanceBitmapForOcr()`)
- **What it does:**
  1. **Downscales** images > 2000px to prevent OOM crashes on low-RAM devices (2GB target)
  2. **Grayscale conversion** — removes color information (saturation = 0)
  3. **Contrast stretching** — applies a 1.6x contrast multiplier with -30 brightness offset via `ColorMatrix`. This pushes faded pencil marks to black and yellowed paper to white.
  4. **Binarization** (`binarizeBitmap()`) — pixel-level thresholding (luminance < 135 → black ink, else → white paper)
- **Why custom pre-processing:** Raw phone photos of hand-written prescriptions have uneven lighting, colored paper, shadows, and faded ink. Without pre-processing, ML Kit's OCR accuracy drops from ~85% to ~40%.

#### Stage 3: On-Device OCR (Google ML Kit Text Recognition)
- **Library:** `com.google.mlkit:text-recognition:16.0.0`
- **What it does:** Runs a neural network **entirely on-device** (no internet required) to extract text from the enhanced bitmap.
- **Why on-device:** Patient prescription images contain protected health information (PHI). Sending them to cloud OCR services would violate ABDM data privacy guidelines. ML Kit processes everything locally.

#### Stage 4: Fuzzy Matching & Shorthand Expansion (`PrescriptionOcrHelper.kt`)
This is our custom NLP post-processing layer:

- **Levenshtein Edit Distance:** Calculates the minimum number of character edits (insertions, deletions, substitutions) between the OCR output and our dictionary of 50+ standard Indian Pharmacopeia medicines. Threshold: ≤1 edit for short words, ≤2 for longer words. Example: `"Paracetam0l"` → `"Paracetamol"` (1 edit, OCR misread `o` as `0`).
  
- **OCR Character Normalization:** Systematic substitution of common OCR misreads: `0→o`, `1→l`, `|→l`, `@→a`, `5→s`.

- **Medical Shorthand Expansion:** Decodes standard Indian clinical prescription abbreviations:
  - `BD` / `1-0-1` → "Twice daily (morning & night after food)"
  - `TDS` / `1-1-1` → "3 times daily (morning, afternoon & night)"
  - `HS` / `0-0-1` → "Once daily at bedtime (night)"
  - `OD` / `1-0-0` → "Once daily (morning)"
  - `SOS` / `PRN` → "As needed for pain/fever (जब ज़रूरत हो)"
  - `AC` → "Take Before Food (खाली पेट)"
  - `PC` → "Take After Food (भोजन के बाद)"

- **Dosage Extraction:** Regex-based extraction of `650mg`, `500 mg`, `10ml`, etc. with normalization of units.

- **Duration Extraction:** Parses `5 days`, `1 week`, `5/7` (clinical notation for 5 days out of 7).

**Output:** A list of structured `PrescribedMedicine` objects with `name`, `dosage`, `frequency`, and `duration` — from a raw photo of a hand-written paper slip.

---

## 9. Clinical Decision Support — TriageEngine

### What it does:
Automated severity classification of patient conditions based on symptoms and vital signs.

### How it works (`TriageEngine.kt`):
```
Input: symptoms[], systolicBP, diastolicBP, SpO2, temperature, pulseRate
Output: SeverityLevel (LOW | MODERATE | HIGH | SEVERE)
```

**Decision hierarchy (highest priority first):**
1. **Red-flag symptoms** → SEVERE: chest pain, shortness of breath, unconscious, heavy bleeding, seizure, paralysis, sudden weakness
2. **Critical vitals** → SEVERE: SpO2 < 90%, systolic BP > 180, diastolic BP > 120, pulse < 40 or > 130, temp > 104°F
3. **High-risk vitals** → HIGH: SpO2 90-94%, systolic BP 160-179, diastolic BP 100-119, temp 102-104°F
4. **Moderate-risk vitals** → MODERATE: systolic BP 140-159, diastolic BP 90-99, temp 100.4-101.9°F
5. **Category fallback** → Emergency categories default to HIGH, maternal/mental health to MODERATE, everything else to LOW

### Why we built this:
ASHA workers are not doctors. They can measure vitals but can't interpret them. TriageEngine gives them an instant color-coded severity level (`SeverityBadge` component) that determines whether to escalate to a doctor immediately or schedule a routine follow-up.

---

## 10. Tele-Consultation — CameraX & AudioRecord

### CameraX (v1.3.2)
- **What it does:** Jetpack library for camera access with lifecycle-aware preview, capture, and camera switching.
- **How we use it:** `PreviewView` renders live front-camera feed in the tele-consultation modal. Supports front/rear camera switching.
- **Why CameraX over Camera2:** Camera2 is a low-level API with ~200 lines of boilerplate for a basic preview. CameraX reduces this to ~20 lines with automatic lifecycle binding (no memory leaks from unreleased camera handles).

### AudioRecord
- **What it does:** Low-level Android API for sampling microphone audio.
- **How we use it:** We read PCM audio buffer amplitudes to create a pulsating microphone level indicator in the tele-consultation room, giving visual confirmation that the mic is active.

### Tele-Vitals HUD
- During a live call, a floating overlay displays the patient's last recorded SpO2, heart rate, and blood pressure — giving the doctor clinical context without switching screens.

---

## 11. Localization — Two-Tier 4-Language System

### Tier 1: Compose Dynamic Strings
- **How:** An `interface AppStrings` defines every UI string. Concrete implementations (`EnglishStrings`, `HindiStrings`, `TamilStrings`, `MarathiStrings`) provide translations.
- **Runtime switching:** `LocalAppStrings` (a `CompositionLocal`) provider swaps the active implementation instantly — no app restart needed.
- **Why this approach:** Faster iteration than Android XML strings during development; supports runtime switching without `recreate()`.

### Tier 2: Android Resource XML
- **How:** `res/values-hi/strings.xml`, `res/values-ta/strings.xml`, `res/values-mr/strings.xml` + `locales_config.xml`
- **Why needed:** WorkManager notifications, system toasts, and background service messages can't access Compose `CompositionLocal`. These use standard Android string resources.

### Languages: English, हिन्दी (Hindi), தமிழ் (Tamil), मराठी (Marathi)

---

## 12. Maps & Geolocation

### Google Maps Compose (v4.3.3) + Play Services Maps (v18.2.0)
- **Used for:** Village outbreak heat maps (Admin dashboard), PHC clinic locations, patient GPS coordinates for SOS.
- **Why Google Maps:** Most reliable mapping SDK for India with good rural coverage and POI data.

### Play Services Location (v21.1.0)
- **Used for:** Fetching GPS coordinates for Emergency SOS dispatch.
- **SOS fallback:** When internet is dead, `SmsManager` sends an SMS with GPS coordinates to emergency contacts.

---

## 13. Security & Privacy

### EncryptedSharedPreferences
- **What:** Stores authentication tokens, user PINs, and session credentials encrypted with hardware-backed AES-256-GCM via Android Keystore.
- **Why:** Patient health data is legally protected under ABDM. Plain-text SharedPreferences would be a compliance violation.

### Consent Manager (`ConsentManager.kt`)
- Implements ABDM M2/M3 electronic consent artifacts: time-bounded, revocable, purpose-specific access grants.
- Emergency "break-glass" access for critical cases, logged immutably in `AuditLogEntity`.

### Audit Logging (`AuditLogEntity`)
- Every data access, proxy action, and consent grant is logged with timestamp, actor ID, role, action type, and resource ID.
- Append-only in Firestore (delete rules return `false`).

### Role-Based Access Control (RBAC)
- Firestore security rules enforce that each role can only access its permitted collections and actions.
- Navigation graph is role-gated — a Patient literally cannot navigate to Admin screens.

---

## 14. Cloud Functions — Server-Side Automation

### `functions/index.js` — 3 Firebase Cloud Functions:

1. **`onQueueEntryChanged`** — Firestore trigger: When a patient's queue status changes to `CALLED`, sends an FCM push notification to the patient's device ("It's Your Turn! 🩺").

2. **`onCaseResponded`** — Firestore trigger: When a doctor marks a condition record as `RESPONDED` or `CLOSED`, automatically creates a `CONDITION` entry in `patientMedicalHistory` for longitudinal tracking.

3. **`onPrescriptionCreated`** — Firestore trigger: When a new prescription is created, auto-generates `MEDICATION` entries in `patientMedicalHistory` for each medicine using a Firestore batch write.

**Why Cloud Functions:** These are server-side side-effects that should happen reliably regardless of which client triggered the change. A Firestore trigger guarantees execution even if the patient's phone dies mid-operation.

---

## 15. Web Landing Page — Next.js

### Stack: Next.js 16 + React 19 + Tailwind CSS 4 + Framer Motion
- **Purpose:** Marketing/download landing page for VitalSense — not a web app.
- **Why Next.js:** SEO-optimized SSR, fast static generation, and modern React 19 features.
- **Why Tailwind:** Rapid UI development with utility classes, no custom CSS files.
- **Why Framer Motion:** Smooth scroll-driven animations for the landing page hero section.

---

## 16. Testing Strategy

### Unit Tests (JUnit 4 + Coroutines Test)
- **`PrescriptionOcrHelperTest`** — Tests Levenshtein distance calculations, fuzzy medicine matching, OCR normalization, shorthand expansion accuracy
- **`AshaTriageTest`** — Tests all severity classification paths in `TriageEngine`
- **`RoomSyncOutboxTest`** — Tests offline transaction queue creation and state reconciliation

### Run command:
```bash
./gradlew testDebugUnitTest
```

---

## 17. Complete Dependency Map

| Category | Library | Version | Purpose |
|:---|:---|:---|:---|
| Language | Kotlin | 1.9.22 | Core language |
| Coroutines | kotlinx-coroutines-android | 1.7.3 | Async operations |
| UI | Jetpack Compose BOM | 2024.02.01 | Declarative UI |
| Design | Material 3 | (via BOM) | Design system |
| Icons | Material Icons Extended | (via BOM) | Icon library |
| Navigation | Navigation Compose | 2.7.7 | Screen routing |
| DI | Dagger Hilt | 2.51.1 | Dependency injection |
| DI-Compose | Hilt Navigation Compose | 1.2.0 | ViewModel injection in Compose |
| Database | Room | 2.6.1 | Local SQLite ORM |
| Background | WorkManager | 2.9.0 | Durable offline sync |
| Images | Coil Compose | 2.6.0 | Async image loading |
| Maps | Maps Compose | 4.3.3 | Map UI in Compose |
| Maps | Play Services Maps | 18.2.0 | Google Maps SDK |
| Location | Play Services Location | 21.1.0 | GPS |
| Camera | CameraX | 1.3.2 | Camera preview & capture |
| OCR | ML Kit Text Recognition | 16.0.0 | On-device OCR |
| Scanner | ML Kit Document Scanner | 16.0.0-beta1 | Document scanning |
| JSON | Gson | 2.10.1 | JSON serialization |
| Firebase | Firebase BOM | 32.8.0 | Firebase SDK management |
| Firestore | firebase-firestore-ktx | (via BOM) | Cloud NoSQL DB |
| Auth | firebase-auth-ktx | (via BOM) | Authentication |
| Storage | firebase-storage-ktx | (via BOM) | File storage |
| FCM | firebase-messaging-ktx | (via BOM) | Push notifications |
| Permissions | Accompanist Permissions | 0.34.0 | Runtime permission handling |
| Lifecycle | Lifecycle Runtime/ViewModel | 2.7.0 | Lifecycle management |
| Activity | Activity Compose | 1.8.2 | Compose activity |
| AndroidX Core | Core KTX | 1.12.0 | Kotlin extensions |
| AppCompat | AppCompat | 1.6.1 | Backward compatibility |

---

*This document should be studied by every team member before the SIH presentation. Understanding WHY each technology was chosen is as important as knowing WHAT it does.*

# 🏥 VitalSense (SehatSetu)

<div align="center">

**Smart India Hackathon (SIH 26133) — Telemedicine & Surveillance System for Rural Healthcare**  
*Connecting Frontline ASHA Workers, Rural Citizens, Doctors, and District Health Authorities in One Unified Ecosystem*

[![Platform](https://img.shields.io/badge/Platform-Android_Native-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin_1.9.22-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![UI Toolkit](https://img.shields.io/badge/UI-Jetpack_Compose_Material_3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean_Architecture_+_MVVM-00897B?style=for-the-badge)](./system-design.md)
[![Offline-First](https://img.shields.io/badge/Offline--First-Room_Outbox_+_WorkManager-FF6D00?style=for-the-badge)](./LOW_CONNECTIVITY.md)
[![Latest Release](https://img.shields.io/badge/Release-v1.6.0-brightgreen?style=for-the-badge)](https://github.com/alexansh/VitalSense/releases)

</div>

---

## 📌 Problem Context & SIH 26133 Overview

In rural and remote healthcare ecosystems across India, patients and frontline health workers face compounding barriers:
1. **Severe Geographic & Specialist Isolation:** Rural Primary Health Centers (PHCs) face acute specialist shortages; patients travel 30–100 km for basic consults, and 70%+ of informal referrals fail to reach tertiary care.
2. **Intermittent / Zero Connectivity:** Sub-centers and field survey sites operate under 2G or total cellular dead-zones where conventional web apps fail completely.
3. **Paper-Based Prescription Loss:** Paper records are damaged or lost, erasing longitudinal medical history.
4. **Low Digital Literacy:** Vernacular-only and non-literate patients struggle with text-dense, complex interfaces.
5. **Delayed Epidemic Response:** Village-level symptom surges go unnoticed until localized outbreaks become district emergencies.

**VitalSense (SehatSetu)** solves these challenges through an **offline-first, 4-in-1 role application** built natively on Android with Kotlin and Jetpack Compose.

---

## 👥 4-in-1 Role Architecture

VitalSense consolidates the entire primary healthcare pipeline into a single, unified codebase with role-gated navigation and permissions:

```
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                              VitalSense Role Architecture                              │
├──────────────────────┬──────────────────────┬──────────────────────┬───────────────────┤
│ 1. PATIENT           │ 2. ASHA WORKER       │ 3. DOCTOR            │ 4. DISTRICT ADMIN │
│ Rural Citizen        │ Grassroots Surveyor  │ PHC Medical Officer  │ Chief Medical Off.│
│ • Vernacular Voice/UI│ • Caseload Mgmt      │ • Live Clinic Queue  │ • Outbreak Maps   │
│ • Offline ABHA Card  │ • Proxy Patient Care │ • Specialist Referrals│ • IPD Bed Tracker│
│ • Specialist Tracking│ • High-Risk Triage   │ • Tele-Consultation  │ • OT Schedule     │
│ • SMS Fallback SOS   │ • Sunlight Mode      │ • Tele-Vitals HUD    │ • Equipment Health│
│ • Stress & Breathing │ • Offline Sync Queue │ • Digital Rx Engine  │ • Emergency Alerts│
└──────────────────────┴──────────────────────┴──────────────────────┴───────────────────┘
```

### 1. 🧑‍🌾 Patient Portal
- **Multilingual Support:** Instant runtime switching between **English, हिन्दी (Hindi), தமிழ் (Tamil), and मराठी (Marathi)**.
- **Offline ABHA Health Card:** Complete demographic profile, blood group, emergency contacts, and digital QR code readable without an internet connection.
- **Specialist Referrals Tracking:** Dedicated collapsible dashboard accordion displaying active referral status (`SENT`, `ACCEPTED`, `IN_PROGRESS`, `COMPLETED`), specialist notes, and appointment schedules.
- **One-Tap Emergency SOS:** Broadcasts GPS coordinates to emergency contacts with automatic fallback to **cellular SMS** and 1-tap **108 Ambulance** dispatch when mobile data is unavailable.
- **Mental Wellness:** Non-stigmatized, icon-driven stress check-ins and interactive guided breathing sessions.

### 2. 👩‍⚕️ ASHA Worker Portal (Frontline Care)
- **Proxy Patient Intake:** Community health workers can record symptoms, register vitals, and book tele-consultations on behalf of illiterate or elderly villagers.
- **Automated High-Risk Triage:** Real-time clinical decision checks (SpO2, blood pressure, fever thresholds) flag high-risk maternal and critical cases with color-coded alerts.
- **Sunlight High-Contrast Mode:** High-luminance palette toggle specifically designed for outdoor field surveys under harsh Indian sunlight.
- **Household & Caseload Registry:** Tracks village families, immunization schedules, and pending follow-ups with instant search and filtering.
- **Zero-Block Offline Surveying:** Uninterrupted household registration and vitals logging in cellular dead-zones, with automatic background sync when connectivity resumes.

### 3. 👨‍⚕️ Doctor Portal (Tele-Medicine, OPD & Specialist Referrals)
- **Live Clinic OPD Queue:** Prioritizes patients by triage severity with real-time wait-time estimation.
- **Closed-Loop Specialist Referral Network:**
  - Primary doctors can refer patients across **8 clinical specialties** (Cardiology, Pediatrics, Gynecology, Orthopedics, Neurology, General Surgery, Dermatology, Psychiatry).
  - Configurable triage urgency levels (`ROUTINE`, `URGENT`, `EMERGENCY`) with clinical inquiry notes and attached EHR records.
  - **Specialist Worklist & Triage Pool:** Dedicated triage queue allowing specialists to filter by status (`PENDING`, `ACTIVE`, `COMPLETED`), accept cases, request additional clinical history, submit findings, or initiate a direct specialist consult.
  - **Instant Patient History Action:** Direct `🩺 Refer to Specialist` button right inside Doctor's Patient Record History accordion cards with pre-filled patient info.
- **Integrated Tele-Consultation Room:** Live CameraX video feed with clinical telemedicine framing (attending Doctor in bottom-right PiP preview, patient primary in full canvas), real-time `AudioRecord` microphone visualization, and persistent **Tele-Vitals HUD** displaying SpO2, heart rate, and BP.
- **Digital Prescription Writer:** Structured diagnosis, medicine schedule (dosage, frequency, duration), and instant dispatch to local dispensaries.

### 4. 🏛️ District Admin Portal (Disease Surveillance & Hospital Operations)
- **Village Outbreak Heat Maps:** Spatial symptom cluster analysis (Dengue, Malaria, Typhoid, Diarrheal diseases) with threshold breach alerts and geographic grouping.
- **Hospital Infrastructure Operations:** Real-time monitoring of IPD bed occupancy, Operation Theatre (OT) schedules, blood bank reserves, and biomedical equipment uptime (ventilators, X-ray, dialysis units).
- **Directives & Broadcasts:** Broadcast instant emergency alerts, vaccination drives, and field protocols to all ASHA workers across the district.

---

## ⚡ Key Technical Innovations

### 🔄 Closed-Loop Specialist Referral Network (Doctor-to-Doctor Multi-Tier Triage)
- **Problem Addressed:** In traditional rural healthcare, over 70% of referred patients never reach secondary or tertiary specialists due to logistical friction, lost paper slips, and zero follow-up tracking.
- **VitalSense Solution:** Creates a secure digital paper trail connecting rural PHC Medical Officers to district/tertiary hospital specialists:
  - **Primary Doctor:** Initiates referral with clinical question, urgency level (`ROUTINE`, `URGENT`, `EMERGENCY`), and attached longitudinal records.
  - **Specialist Doctor:** Reviews the clinical inquiry in their dedicated "Specialist Referrals" queue, reviews patient vitals/history, and accepts, requests more info, or declines with clinical recommendations.
  - **Patient Dashboard:** Automatically displays referral progress, appointment details, and completed specialist consult summaries.
  - **Loop Closure:** Specialist findings are transmitted back to the referring doctor's patient history, ensuring complete continuity of care.

### 📶 Offline-First Engine (Room Outbox + WorkManager)
- **Zero-Block Local Operations:** Every read and write is served instantaneously from the local SQLite Room database with 0ms server latency.
- **Durable Transactional Outbox:** Offline mutations (patient intake, triage records, prescriptions, referrals) are persisted locally with `syncState = PENDING_SYNC` and queued in an atomic outbox table.
- **Real-Time Network Monitoring:** `NetworkMonitor` continuously samples connectivity state (`ONLINE` $\ge$ 1500 kbps, `SLOW_NETWORK` 1..1499 kbps, `OFFLINE` 0 kbps) using Android's `ConnectivityManager`.
- **Automatic Background Sync:** Android `WorkManager` detects network restoration and pushes batched mutations to Firebase Firestore with exponential backoff and zero data loss.
- **Zero-Internet Emergency Fallbacks:** SOS emergency alerts automatically fallback to direct cellular SMS with encoded GPS coordinates and 1-tap 108 emergency dialer.

### 📄 Intelligent Prescription Digitization & ML Kit OCR
- **Google Play Services Document Scanner API:** Automatic 4-corner document detection, perspective unwarping, shadow suppression, and finger-removal filters.
- **On-Device ML Kit Text Recognition:** Extracts raw text locally without sending patient images to external servers.
- **Levenshtein Fuzzy Matching & Shorthand Expander:** Automatically cleans OCR artifacts and expands clinical shorthand (`BD` → Twice Daily, `TDS` → Thrice Daily, `HS` → At Bedtime, `PCM` → Paracetamol).

### 📹 Real Camera & Microphone Tele-Consultation Room
- **Standardized Telemedicine Framing:** The patient's live video stream is prominently rendered as the primary view for diagnostic assessment, while the attending doctor is framed in the bottom-right Picture-in-Picture (PiP) inset preview.
- **Native CameraX `PreviewView`:** Hardware-accelerated front/rear camera switching with optimal aspect ratio scaling.
- **Live Audio Sampling:** Real-time audio waveform sampling via Android `AudioRecord` providing a pulsating microphone level indicator.
- **Floating Tele-Vitals HUD:** Persistent overlay displaying real-time SpO2, heart rate, and blood pressure during active calls.

### 🌐 Complete Multilingual Runtime Localization
- Seamless runtime language switching across **English, हिन्दी (Hindi), தமிழ் (Tamil), and मराठी (Marathi)**.
- 100% localized UI strings across all 4 role portals, triage alerts, prescription forms, referral workflows, and system dialogs without layout clipping.

---

## 🏛️ System Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    Jetpack Compose UI (BOM 2024.02)                     │
│  NagarSeva Tokens │ Sunlight High-Contrast │ 4-Language Runtime i18n    │
└────────────────────────────────────┬────────────────────────────────────┘
                                     │ StateFlow / Events
┌────────────────────────────────────▼────────────────────────────────────┐
│                        MVVM ViewModels (Hilt DI)                        │
│  PatientVM  │  AshaVM  │  DoctorVM  │  AdminVM  │  TeleVM  │ ReferralVM │
└────────────────────────────────────┬────────────────────────────────────┘
                                     │ Repositories
┌────────────────────────────────────▼────────────────────────────────────┐
│                    Data Layer (Offline-First Outbox)                    │
│  ┌─────────────────────────┐             ┌───────────────────────────┐  │
│  │   Android Room SQLite   │◄───────────►│  WorkManager Sync Engine  │  │
│  │   (Local Single Source) │             │  (Backoff + Queue Worker) │  │
│  └─────────────────────────┘             └─────────────┬─────────────┘  │
│               ▲                                        │                │
│               │ Network Monitor (Online/Slow/Offline)   │                │
└───────────────┼────────────────────────────────────────┼────────────────┘
                │                                        │ HTTPS / gRPC
                │                            ┌───────────▼───────────┐
                └────────────────────────────│   Cloud Firestore     │
                                             │   & Firebase Auth     │
                                             └───────────────────────┘
```

<div align="center">

### Full-Stack End-to-End Architecture
![VitalSense Full-Stack Architecture](./vitalsense_tech_stack.svg)

</div>

---

## 📂 Repository Structure

```
├── app/                        # Main Android Application module
│   ├── src/main/java/com/vitalsense/
│   │   ├── core/               # Outbox sync, network monitor, referral models
│   │   ├── data/               # Room entities, DAOs, repositories, Outbox sync
│   │   ├── di/                 # Dagger Hilt dependency injection modules
│   │   ├── domain/             # Business models, triage rules, use cases
│   │   ├── ui/                 # Jetpack Compose screens, themes, components
│   │   │   ├── admin/          # Outbreak maps, IPD beds, OT, equipment
│   │   │   ├── asha/           # Caseload, proxy mode, triage alerts
│   │   │   ├── doctor/         # Live OPD queue, specialist referrals, Rx engine
│   │   │   ├── patient/        # ABHA card, referral tracking, SOS, wellness
│   │   │   └── teleconsult/    # CameraX + AudioRecord tele-consultation room
│   │   └── util/               # ML Kit OCR, Document Scanner, Fuzzy Matcher
│   └── src/test/               # Unit tests (Room, Triage, OCR, ViewModels, Referrals)
├── release/                    # Production & Debug release APK builds (v1.6.0)
├── firestore.rules             # Secure role-based Firestore security rules
├── prd.md                      # Comprehensive Product Requirements Document
├── tech-stack.md               # Detailed Technical Architecture & Libraries
├── system-design.md            # Deep-dive System Design & Data Schemas
├── LOW_CONNECTIVITY.md         # Low-connectivity and offline-first architecture guide
├── TECHNICAL_DEEP_DIVE.md      # Technical deep-dive and judge presentation guide
└── README.md                   # This project overview & documentation
```

---

## 🚀 Getting Started & Installation

### Option 1: Direct APK Installation (Recommended for Testing)

Download the pre-compiled, release-ready APK directly from [GitHub Releases](https://github.com/alexansh/VitalSense/releases) or the local `release/` folder:

| Build Flavor | Target File | Direct Action | Size |
| :--- | :--- | :--- | :--- |
| **Release Build (Optimized)** | `release/VitalSense-v1.6.0-release.apk` | Production-ready, signed & R8-optimized | ~58.9 MB |
| **Debug Build** | `release/VitalSense-v1.6.0-debug.apk` | Includes live logcat logging & pre-seeded demonstration data | ~67.0 MB |

```bash
# Install directly via ADB
adb install -r release/VitalSense-v1.6.0-release.apk
```

<div align="center">
  <img src="./vitalsense_download_qr.png" alt="Scan to Download APK" width="160" />
  <p><i>Scan QR Code to quickly download or verify the VitalSense APK</i></p>
</div>

---

### Option 2: Build From Source

#### Prerequisites
- **JDK:** Java 21 LTS (`OpenJDK 21` or `Eclipse Temurin 21`)
- **Android SDK:** Platform API 34 / 35 (Min SDK: 26)
- **Build Tool:** Gradle 8.7 (wrapper included)

#### Build Commands
```bash
# Clone the repository
git clone https://github.com/alexansh/VitalSense.git
cd VitalSense

# Run local unit tests (30+ test suites)
./gradlew testDebugUnitTest

# Assemble Signed Release APK
./gradlew assembleRelease

# Output binary location:
# app/build/outputs/apk/release/app-release.apk
```

---

## 🆕 What's New in v1.6.0 (Changelog)

### 🩺 Closed-Loop Specialist Referral Network (PR #14 & Workflow Enhancements)
- **Doctor-to-Doctor Referral Pipeline:** Primary PHC medical officers can refer complex cases across 8 medical specialties with clinical inquiry questions and triage urgency (`ROUTINE`, `URGENT`, `EMERGENCY`).
- **Specialist Referrals Worklist & Triage Pool:** Dedicated specialist queue with filtering (`PENDING`, `ACTIVE`, `COMPLETED`), case acceptance, requesting additional diagnostic information, submitting clinical findings, and launching tele-consultations.
- **Patient Dashboard Referral Tracking:** Collapsible accordion item on patient home screen displaying referral status, assigned department, and specialist notes.
- **One-Tap Referral Action in Patient History:** Added a direct `🩺 Refer to Specialist` button inside the Doctor's "Patient Record History" accordion cards with pre-filled patient info.
- **Button Text Legibility Fix:** Fixed invisible text bug in Doctor's "Clinical History" button by switching to high-contrast `VS_OnPrimaryContainer` styling.

### 📶 Low-Connectivity & Offline-First Outbox Synchronization (PR #13)
- **Dual-State Real-Time Network Monitor:** Integrated `NetworkMonitor` observing active cellular and Wi-Fi networks to report `ONLINE`, `SLOW_NETWORK`, and `OFFLINE` status.
- **Zero-Block SQLite Room Persistence:** All clinical reads and writes execute synchronously against local SQLite with 0ms server latency.
- **Transactional Outbox & WorkManager Sync:** Offline mutations are durably queued in Room with `SyncState.PENDING_SYNC` and synchronized to Firebase Firestore with exponential backoff retry upon network recovery.
- **Zero-Internet Fallback Protocols:** Emergency SOS features fallback to automated cellular SMS with GPS coordinates and 1-tap 108 emergency dialer.

### 📹 Tele-Consultation CameraX & Audio Improvements
- **Standardized Framing:** Attending doctor is persistently positioned in the bottom-right Picture-in-Picture (PiP) inset preview, while the patient is prominently displayed in the main canvas for clinical examination.
- **Real-Time Audio Waveform:** Live audio level sampling using Android `AudioRecord` with a pulsating microphone indicator.
- **Floating Tele-Vitals HUD:** Persistent overlay displaying SpO2, heart rate, and blood pressure throughout tele-consultations.

### 🌐 Complete Multilingual Localization (i18n)
- Seamless dynamic runtime switching across **English, हिन्दी (Hindi), தமிழ் (Tamil), and मराठी (Marathi)**.
- Full string coverage across all 4 role portals and newly added specialist referral workflows with zero UI truncation.

### 🛡️ Stability & Platform Enhancements
- Fixed cold-start initialization crash by stabilizing `NetworkMonitor` lifecycle order and wrapping application context for Dagger Hilt injection.
- Upgraded Gradle configuration and validated clean builds on Java 21 LTS toolchain.

---

## 🧪 Verification & Testing

All core domains are validated with comprehensive automated test suites:
- **`ReferralSystemTest`**: Validates referral creation, entity conversion, status transitions (`SENT` → `ACCEPTED` / `DECLINED` / `INFO_REQUESTED`), and data mapping.
- **`PrescriptionOcrHelperTest`**: Validates ML Kit OCR parsing, fuzzy drug correction, and medical shorthand expansions (`BD`, `TDS`, `HS`, `1-0-1`).
- **`AshaTriageTest`**: Validates automated maternal and emergency SpO2/BP risk triage calculations.
- **`RoomSyncOutboxTest`**: Validates offline transaction queues and state reconciliation.

Run all tests:
```bash
./gradlew testDebugUnitTest
```

---

## 📄 Documentation Links

- 📋 [**Product Requirements Document (PRD)**](./prd.md) — Comprehensive functional requirements, persona user flows, and success metrics for SIH 26133.
- 🛠️ [**Technology Stack Specification**](./tech-stack.md) — Exhaustive specification of libraries, JVM toolchains, Android SDKs, and local persistence.
- 📐 [**System Design & Architecture**](./system-design.md) — Full database schemas, outbox state machine, offline sync sequence diagrams, and security architecture.
- 📶 [**Low-Connectivity & Offline-First Guide**](./LOW_CONNECTIVITY.md) — Deep-dive into zero-block local operations, Room transactional outbox, and adaptive bandwidth fallbacks.
- 🔬 [**Technical Deep-Dive & Judge Presentation Guide**](./TECHNICAL_DEEP_DIVE.md) — Architectural justifications, data models, and hackathon presentation walk-through.

---

## 🏆 Smart India Hackathon (SIH 26133)

Developed with pride for the **Smart India Hackathon 2024 / 2025** to deliver dignified, accessible, and resilient healthcare infrastructure to the most remote corners of India.


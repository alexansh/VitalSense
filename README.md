# 🏥 VitalSense (SehatSetu)

<div align="center">

**Smart India Hackathon (SIH 26133) — Telemedicine & Surveillance System for Rural Healthcare**  
*Connecting Frontline ASHA Workers, Rural Citizens, Doctors, and District Health Authorities in One Unified Ecosystem*

[![Platform](https://img.shields.io/badge/Platform-Android_Native-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin_1.9.22-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![UI Toolkit](https://img.shields.io/badge/UI-Jetpack_Compose_Material_3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean_Architecture_+_MVVM-00897B?style=for-the-badge)](./system-design.md)
[![Offline-First](https://img.shields.io/badge/Offline--First-Room_Outbox_+_WorkManager-FF6D00?style=for-the-badge)](./tech-stack.md)
[![Latest Release](https://img.shields.io/badge/Release-v1.5.0-brightgreen?style=for-the-badge)](https://github.com/alexansh/VitalSense/releases)

</div>

---

## 📌 Problem Context & SIH 26133 Overview

In rural and remote healthcare ecosystems across India, patients and frontline health workers face compounding barriers:
1. **Severe Geographic & Specialist Isolation:** Rural Primary Health Centers (PHCs) face acute specialist shortages; patients travel 30–100 km for basic consults.
2. **Intermittent / Zero Connectivity:** Sub-centers and field survey sites operate under 2G or total cellular dead-zones.
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
│ • Offline ABHA Card  │ • Proxy Patient Care │ • Tele-Consultation  │ • IPD Bed Tracker │
│ • SMS Fallback SOS   │ • High-Risk Triage   │ • Tele-Vitals HUD    │ • OT Schedule     │
│ • Stress & Breathing │ • Sunlight Mode      │ • Digital Rx Engine  │ • Emergency Alerts│
└──────────────────────┴──────────────────────┴──────────────────────┴───────────────────┘
```

### 1. 🧑‍🌾 Patient Portal
- **Multilingual Support:** Instant runtime switching between **English, हिन्दी (Hindi), தமிழ் (Tamil), and मराठी (Marathi)**.
- **Offline ABHA Health Card:** Complete demographic profile, blood group, emergency contacts, and digital QR code readable without an internet connection.
- **One-Tap Emergency SOS:** Broadcasts GPS coordinates to emergency contacts with automatic fallback to **cellular SMS** when mobile data is unavailable.
- **Mental Wellness:** Non-stigmatized, icon-driven stress check-ins and interactive guided breathing sessions.

### 2. 👩‍⚕️ ASHA Worker Portal (Frontline Care)
- **Proxy Patient Intake:** Community health workers can record symptoms, register vitals, and book tele-consultations on behalf of illiterate or elderly villagers.
- **Automated High-Risk Triage:** Real-time clinical decision checks (SpO2, blood pressure, fever thresholds) flag high-risk maternal and critical cases.
- **Sunlight High-Contrast Mode:** High-luminance palette toggle specifically designed for outdoor field surveys under harsh Indian sunlight.
- **Household & Caseload Registry:** Tracks village families, immunization schedules, and pending follow-ups.

### 3. 👨‍⚕️ Doctor Portal (Tele-Medicine & OPD)
- **Live Clinic OPD Queue:** Prioritizes patients by triage severity with wait-time estimation.
- **Integrated Tele-Consultation Room:** Live CameraX front-camera video preview, real-time AudioRecord microphone indicator, and persistent **Tele-Vitals HUD** displaying SpO2, heart rate, and BP.
- **Digital Prescription Writer:** Structured diagnosis, medicine schedule (dosage, frequency, duration), and instant dispatch to local dispensaries.

### 4. 🏛️ District Admin Portal (Disease Surveillance & Hospital Operations)
- **Village Outbreak Heat Maps:** Spatial symptom cluster analysis (Dengue, Malaria, Typhoid, Diarrheal diseases) with threshold breach alerts.
- **Hospital Infrastructure Desks:** Real-time IPD bed occupancy, Operation Theatre (OT) schedules, blood bank stock, and biomedical equipment uptime monitoring.
- **Directives & Broadcasts:** Broadcast instant emergency alerts and field protocols to all ASHA workers across the district.

---

## ⚡ Key Technical Innovations

### 📶 Offline-First Engine (Room Outbox + WorkManager)
- Every read is served instantaneously from the local SQLite Room database.
- Writes are persisted locally with `syncState = PENDING_SYNC` and queued in an outbox table.
- Android WorkManager detects network restoration and pushes batched mutations to Firebase Firestore with zero data loss.

### 📄 Intelligent Prescription Digitization & ML Kit OCR
- **Google Play Services Document Scanner API:** Automatic 4-corner document detection, perspective unwarping, shadow suppression, and finger-removal filters.
- **On-Device ML Kit Text Recognition:** Extracts raw text locally without sending patient images to external servers.
- **Levenshtein Fuzzy Matching & Shorthand Expander:** Automatically cleans OCR artifacts and expands clinical shorthand (`BD` → Twice Daily, `TDS` → Thrice Daily, `HS` → At Bedtime, `PCM` → Paracetamol).

### 📹 Real Camera & Microphone Tele-Consultation Room
- Native CameraX `PreviewView` integration with camera switching (Front/Rear).
- Live audio sampling via Android `AudioRecord` providing a pulsating microphone level indicator.
- Floating medical overlay with clinical history and vitals visible during the entire call.

---

## 🏛️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                 Jetpack Compose UI (BOM 2024.02)            │
│   Glume Tokens  │  Sunlight Mode  │  4-Language Localization│
└──────────────────────────────┬──────────────────────────────┘
                               │ StateFlow / Events
┌──────────────────────────────▼──────────────────────────────┐
│                    MVVM ViewModels (Hilt DI)                 │
│  PatientVM  │  AshaVM  │  DoctorVM  │  AdminVM  │  TeleVM   │
└──────────────────────────────┬──────────────────────────────┘
                               │ Repositories
┌──────────────────────────────▼──────────────────────────────┐
│               Data Layer (Offline-First Outbox)              │
│  ┌─────────────────────────┐     ┌───────────────────────┐  │
│  │   Android Room SQLite   │◄───►│  WorkManager Sync     │  │
│  │   (Local Single Source) │     │  (Backoff + Retry)    │  │
│  └─────────────────────────┘     └───────────┬───────────┘  │
└──────────────────────────────────────────────┼──────────────┘
                                               │ HTTPS / gRPC
                                   ┌───────────▼───────────┐
                                   │   Cloud Firestore     │
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
│   │   ├── data/               # Room entities, DAOs, repositories, Outbox sync
│   │   ├── di/                 # Dagger Hilt dependency injection modules
│   │   ├── domain/             # Business models, triage rules, use cases
│   │   ├── ui/                 # Jetpack Compose screens, themes, components
│   │   │   ├── admin/          # Outbreak maps, IPD beds, OT, equipment
│   │   │   ├── asha/           # Caseload, proxy mode, triage alerts
│   │   │   ├── doctor/         # Live OPD queue, digital prescription form
│   │   │   ├── patient/        # ABHA card, SOS dialog, wellness, appointments
│   │   │   └── teleconsult/    # CameraX + AudioRecord tele-consultation room
│   │   └── util/               # ML Kit OCR, Document Scanner, Fuzzy Matcher
│   └── src/test/               # Unit tests (Room, Triage, OCR, ViewModels)
├── release/                    # Production & Debug release APK builds (v1.4.2)
├── firestore.rules             # Secure role-based Firestore security rules
├── prd.md                      # Comprehensive Product Requirements Document
├── tech-stack.md               # Detailed Technical Architecture & Libraries
├── system-design.md            # Deep-dive System Design & Data Schemas
└── README.md                   # This project overview & documentation
```

---

## 🚀 Getting Started & Installation

### Option 1: Direct APK Installation (Recommended for Testing)

Download the pre-compiled, release-ready APK directly from [GitHub Releases](https://github.com/alexansh/VitalSense/releases) or the local `release/` folder:

| Build Flavor | Target File | Direct Action |
| :--- | :--- | :--- |
| **Release Build (Optimized)** | `release/VitalSense-v1.5.0-release.apk` | Production-ready, R8-optimized |
| **Debug Build** | `release/VitalSense-v1.5.0-debug.apk` | Includes debug logs and test seeds |

```bash
# Install directly via ADB
adb install -r release/VitalSense-v1.5.0-release.apk
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

# Run local unit tests (28+ test suites)
./gradlew testDebugUnitTest

# Assemble Release APK
./gradlew assembleRelease

# Output binary location:
# app/build/outputs/apk/release/app-release.apk
```

---

## 🧪 Verification & Testing

All core domains are validated with comprehensive automated test suites:
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

---

## 🏆 Smart India Hackathon (SIH 26133)

Developed with pride for the **Smart India Hackathon 2024 / 2025** to deliver dignified, accessible, and resilient healthcare infrastructure to the most remote corners of India.

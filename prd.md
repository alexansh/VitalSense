# 📋 VitalSense (SehatSetu) — Product Requirements Document (PRD)

**Problem Statement:** Smart India Hackathon (SIH) 26133  
**Theme:** MedTech / Rural Health Bridge  
**Version:** 2.0 (Production-Ready Prototype)  
**Target Platform:** Android Native (Smartphones & Tablets for Rural Field Workers & Clinics)

---

## 1. Problem Statement & Context

In rural and remote Indian healthcare ecosystems, patients and frontline health workers face acute, compounding systemic challenges:
1. **Severe Geographic & Specialist Isolation:** Rural primary health centers (PHCs) lack specialist doctors; patients must travel 30-100 km to district hospitals for routine consultations.
2. **Intermittent or Zero Connectivity:** Rural health centers and village households frequently operate under 2G or zero cellular data connectivity.
3. **Paper-Based Fragmentation:** Prescriptions, test slips, and maternal records are maintained on paper and frequently lost, leading to zero longitudinal health tracking.
4. **Low Digital & Health Literacy:** Rural patients struggle with text-dense apps; spoken guidance and visual status indicators in local dialects are critical.
5. **Community Triage Delays:** ASHA workers lack automated clinical decision support to rapidly triage critical vitals (SpO2, blood pressure) and escalate high-risk cases.

VitalSense (**SehatSetu**) resolves these issues through a **single, unified, 4-in-1 role application** operating on an **offline-first local-first database**.

---

## 2. Target User Personas & Roles

```
┌────────────────────────────────────────────────────────────────────────┐
│                        VitalSense Roles & Personas                     │
├─────────────────┬─────────────────┬─────────────────┬──────────────────┤
│ 1. PATIENT      │ 2. ASHA WORKER  │ 3. DOCTOR       │ 4. DISTRICT CMO  │
│ Rural resident, │ Frontline grass-│ PHC Attending & │ Chief Medical    │
│ non-literate or │ roots surveyor, │ Urban Specialist│ Officer, Command │
│ vernacular-only │ proxy caretaker │ Tele-consultant │ & Epidemics      │
└─────────────────┴─────────────────┴─────────────────┴──────────────────┘
```

### 2.1 Patient (Rural Resident / Household)
- Needs simple, visual, icon-driven access to healthcare.
- Must be able to switch instantly into **Hindi, Tamil, or Marathi**.
- Needs an offline digital **ABHA Health Card** displaying their ID, blood group, and care timeline.
- Requires instant one-tap **Emergency SOS** with cellular SMS fallback.

### 2.2 ASHA Worker (Community Health Field Worker)
- Manages an assigned caseload of 3-5 villages (300-800 citizens).
- Needs **Proxy Mode** to record symptoms, book doctor calls, and manage prescriptions on behalf of elderly or illiterate patients.
- Requires automated **High-Risk Triage Detection** to prioritize maternal and critical emergencies.
- Needs outdoor **Sunlight High-Contrast Mode** for field surveys.

### 2.3 Doctor (Medical Officer / Specialist)
- Reviews an intelligent **Live Clinic OPD Queue** with patient wait-time estimation.
- Conducts low-bandwidth **Tele-Consultations** with an in-call live Tele-Vitals HUD.
- Inspects **Longitudinal Medical History** and triage priority score cards.
- Issues structured digital prescriptions synchronized directly to local dispensaries.

### 2.4 District Admin (Chief Medical Officer / CMO)
- Real-time command dashboard tracking active caseloads across all district villages.
- **Disease Outbreak Surveillance Map** pin-pointing symptomatic clusters (e.g. Dengue, Cholera).
- **Hospital Infrastructure Desks**: IPD bed occupancy, Operation Theatre (OT) surgery schedules, blood bank stock, and biomedical equipment uptime.
- Issues district-wide emergency broadcast directives to ASHAs and clinics.

---

## 3. Core Functional Requirements & Feature Modules

### Module 1: Universal 4-Language Localization
- **Requirement:** Zero English string leakage. Every header, badge, dialog, button, and notification must adapt dynamically to:
  - English (`en`)
  - हिन्दी — Hindi (`hi`)
  - தமிழ் — Tamil (`ta`)
  - मराठी — Marathi (`mr`)
- Language picker must display native scripts (`English`, `हिन्दी`, `தமிழ்`, `मराठी`) for rapid recognition.

### Module 2: ABDM Health Identity & Care Timeline
- Simulation of **14-digit ABHA ID** (`14-XXXX-XXXX-XXXX`) and `@abdm` address.
- **Electronic Consent Management:** Granular consent grants (purpose, doctor ID, validity duration) conforming to ABDM M2/M3 standards.
- **Longitudinal Patient Timeline Dialog:** Chronological care journey aggregating all previous encounters, prescriptions, and diagnostic lab reports.

### Module 3: Hospital & Clinical Operations Hub
- **OPD Live Queue:** Real-time token generator, department selection (General, Maternal, Pediatrics), and live queue progression HUD.
- **District Blood Bank Registry:** Units tracking by group (A+, B+, O+, AB-, etc.) and one-tap emergency call action.
- **Diagnostic Lab Reports:** Test parameter indicators, normal reference range chips, and offline PDF downloading.
- **In-Patient (IPD) Ward Bed Matrix:** Total, occupied, and available bed allocations across General, ICU, and Oxygen wards.
- **Operation Theatre (OT) Scheduler:** Emergency OT booking, pre-anesthetic check (PAC) status, and surgeon rosters.
- **Bio-Medical Equipment Registry:** Operational status metrics and maintenance fault logging for ventilators, defibrillators, and dialysis machines.

### Module 4: Doctor Telemedicine & Clinical Workstation
- In-call **Live Tele-Vitals HUD** during video/audio consultations.
- **Prescription Composer:** Structured medicine dosage, frequency, duration, and local dispensary stock cross-checking.
- **Patient Medical History Dialog:** Multi-encounter longitudinal records with triage severity scores.

### Module 5: ASHA Field Ops & Proxy Care
- Household caseload overview with village-level segmentation.
- Seamless **Proxy Mode** activation allowing ASHA workers to act on behalf of any patient.
- Dedicated ASHA identity badge with QR code for patient pairing.

### Module 6: District Epidemic Surveillance & Emergency SOS
- Village cluster health telemetry displaying population-adjusted risk ratios.
- One-tap district-wide health directives dispatched to field workers.
- **Emergency SOS:** Dispatches GPS coordinates via cellular SMS when internet is offline.

---

## 4. Non-Functional Requirements (NFRs)

| NFR Domain | Specification & Standard |
| :--- | :--- |
| **Offline Performance** | 100% of read operations and local writes succeed with zero internet connectivity. |
| **Sync Latency** | Background sync via WorkManager executes within 60 seconds of network restoration. |
| **Memory Footprint** | Peak RAM consumption kept below 150MB for smooth performance on 2GB RAM Android devices. |
| **Battery Consumption** | Battery drain constrained to < 3% per hour during active field use. |
| **Data Privacy & Security**| Local database encrypted with AES-256; patient records protected by revocable consent artifacts. |
| **Compliance** | Full alignment with Ayushman Bharat Digital Mission (ABDM) standards and modern digital health infrastructure. |\n
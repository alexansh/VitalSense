# VitalSense — Low-Connectivity & Offline-First Architecture Guide
**Smart India Hackathon 2026 | Problem Statement: SIH26133**  
*Comprehensive Technical Architecture, Durable Outbox Specification, and Zero-Internet Fallback Protocols*

---

## 1. Executive Summary & SIH26133 Problem Alignment

Rural and underserved healthcare delivery in India is fundamentally constrained by **severe, intermittent, or absent network connectivity**, geographic remoteness, and low digital literacy. Frontline healthcare workers (ASHA, ANM) and rural citizens frequently operate in areas with zero cellular reception or severely degraded 2G/EDGE networks.

VitalSense solves this crisis through a **guaranteed Offline-First Architecture**:
1. **Zero-Block Local Operations**: All medical workflows (patient intake, daily rounds, offline health cards, digital prescriptions, triage, lab reports, OPD tokens, and referrals) execute synchronously against local SQLite (Room) with 0ms server dependency.
2. **Durable Transactional Outbox**: Mutations are durably captured in an atomic Room Outbox table with entity deduplication and synced asynchronously when connectivity returns.
3. **Adaptive Link Degradation**: Tele-consultation and UI assets gracefully degrade based on link bandwidth (`ONLINE` $\ge$ 1500 kbps, `SLOW_NETWORK` 1..1499 kbps, `OFFLINE` 0 kbps).
4. **Zero-Internet Emergency Fallbacks**: Emergency SOS alerts never block or claim false server success when offline; they immediately present direct cellular fallbacks (1-tap dial 108 ambulance and automated SMS to assigned ASHA worker with GPS coordinates).

---

## 2. System Architecture & Data Flow

```mermaid
flowchart TD
    subgraph UI_Layer [Presentation & Interaction Layer]
        A[ASHA / Patient / Doctor UI Action] --> B[ViewModel]
    end

    subgraph Core_Repository [VitalSense Repository & Network Monitor]
        B --> C[VitalSenseRepositoryImpl]
        C --> D{Network Monitor Quality Check}
        D -- ONLINE (>= 1500 kbps) --> E[Push Firestore Immediately + Write Room]
        D -- SLOW / OFFLINE (0..1499 kbps) --> F[Write Local Room + Enqueue OutboxEntity]
    end

    subgraph Local_Storage [Local Persistence - Single Source of Truth]
        F --> G[(Room SQLite Database v12)]
        G --> H[(OutboxEntity Table)]
        G --> I[Reactive StateFlow UI Observation]
    end

    subgraph Sync_Engine [Background Synchronization Engine]
        J[Network Change Callback / Periodic Worker] --> K[SyncManager]
        K --> L[SyncWorker (WorkManager)]
        L --> M[Read PENDING Outbox Records]
        M --> N[Firestore Remote Upload]
        N -- Success --> O[DELETE Outbox Record / Mark SYNCED]
        N -- Network Failure --> P[Increment retryCount + Exponential Backoff]
    end
```

---

## 3. Network Quality Classification (`NetworkMonitor.kt`)

VitalSense uses Android's `ConnectivityManager.NetworkCallback` with active bandwidth capabilities (`LinkProperties` and `NetworkCapabilities`) to categorize network quality:

| State | Condition / Bandwidth | Adaptive System Behavior |
| :--- | :--- | :--- |
| **`ONLINE`** 🟢 | Downstream $\ge$ 1500 kbps, full internet validation | High-res image uploads, HD video tele-consultation, immediate Firestore sync. |
| **`SLOW_NETWORK`** 🟡 | Downstream 1..1499 kbps (2G / rural EDGE) | Audio-only consultation fallback, compressed thumbnails, deferred non-critical telemetry sync. |
| **`OFFLINE`** 🔴 | 0 kbps, airplane mode, or manual simulation | 100% offline local reads/writes, durable outbox queuing, 1-tap dial 108 & SMS fallbacks. |

### Network Monitor Implementation Highlights
- **Reactive StateFlow**: `NetworkMonitor.connectivityState` streams real-time updates to all ViewModels and UI banners.
- **Auto-Flush Trigger**: Transition from `OFFLINE` $\rightarrow$ `ONLINE` immediately invokes `syncManager.triggerImmediateSync()`.
- **Manual Simulation Toggle**: In-app 1-tap toggle in `TopRoleSwitcherBar` allows developers and judges to simulate offline mode instantly without toggling device airplane mode.

---

## 4. Durable Outbox Pattern (`OutboxEntity.kt` & `SyncWorker.kt`)

### Database Schema (Room Migration 11 $\rightarrow$ 12)
```sql
CREATE TABLE IF NOT EXISTS outbox_records (
    id TEXT PRIMARY KEY NOT NULL,
    actionType TEXT NOT NULL,
    payload TEXT NOT NULL,
    createdAt INTEGER NOT NULL,
    retryCount INTEGER NOT NULL DEFAULT 0,
    syncStatus TEXT NOT NULL DEFAULT 'PENDING',
    lastAttemptAt INTEGER,
    errorMessage TEXT
);
CREATE INDEX IF NOT EXISTS index_outbox_records_syncStatus ON outbox_records(syncStatus);
```

### Supported Mutation Types
The Outbox pattern handles all 20+ mutations across the platform:
1. `CREATE_PATIENT` / `UPDATE_PATIENT`
2. `CREATE_PRESCRIPTION`
3. `CREATE_CONDITION`
4. `CREATE_DAILY_ROUND`
5. `CREATE_IMMUNIZATION`
6. `CREATE_ASHA_MEDICINE` / `RESTOCK_ASHA_MEDICINE`
7. `DISPENSARY_STOCK_UPDATE`
8. `CREATE_REFERRAL` / `UPDATE_REFERRAL`
9. `CREATE_LAB_REPORT`
10. `BOOK_OPD_TOKEN` / `UPDATE_OPD_TOKEN`
11. `CREATE_MEDICAL_CERTIFICATE`
12. `CREATE_EXTERNAL_REFERRAL`
13. `LOG_CALL`
14. `LOG_AUDIT`
15. `CREATE_DISEASE_TREND`
16. `SOS_ALERT`

### Exponential Backoff Retry Formula
When network errors occur during sync, `SyncWorker` applies an exponential backoff formula:
$$\text{delayMs} = \min\left(2^{\text{retryCount}} \times 1000\text{ ms}, 30000\text{ ms}\right)$$

---

## 5. Conflict Resolution & Idempotency Strategy

1. **Deterministic Idempotency Keys**: Each outbox entry uses entity-specific IDs (e.g., `outbox_patient_${patient.id}`, `outbox_rx_${rx.id}`). Repeated offline edits to the same entity update the existing outbox payload rather than creating duplicate queued records.
2. **Client-Side UUID Generation**: All entity IDs are generated client-side at creation time (`UUID.randomUUID().toString()`), guaranteeing zero collision across offline devices.
3. **Last-Write-Wins (LWW) with Timestamp Precedence**: Remote Firestore uploads use timestamps (`updatedAt`, `timestamp`) to ensure the latest clinical entry prevails in the event of multi-device synchronization.

---

## 6. Emergency SOS Zero-Internet Safety Protocol

> [!IMPORTANT]
> **Clinical Safety Mandate**: In a life-threatening rural medical emergency, an application must **NEVER** falsely display "Emergency alert sent to hospital" when the device is disconnected from the internet.

```
+-------------------------------------------------------------------------+
|                       EMERGENCY SOS TRIGGERED                           |
+-------------------------------------------------------------------------+
                                    |
                        Is Device Online? (NetworkMonitor)
                                    |
                 +------------------+------------------+
                 |                                     |
               YES                                     NO
                 |                                     |
    [Push to Cloud Server]                 [Queue in Local Outbox]
    [Dispatch Server Queue]                            +
    [Play Audio Confirmation]              [Display Offline Warning]
                 |                         [Play Audio Guidance: Offline]
                 |                                     +
                 |                         +---------------------------+
                 |                         | Direct Zero-Internet      |
                 |                         | 1-Tap Fallback Options:   |
                 |                         |  1. Call 108 Ambulance   |
                 |                         |  2. SMS ASHA Worker + GPS |
                 +-------------------------+---------------------------+
```

1. **Immediate Outbox Persist**: SOS notice is saved to Room and queued in the durable outbox (`actionType = "SOS_ALERT"`).
2. **Zero-Internet Fallbacks**:
   - **Direct 108 Dial**: Launches `Intent.ACTION_DIAL` with `tel:108` for instant ambulance dispatch.
   - **Direct SMS to ASHA**: Launches `Intent.ACTION_SENDTO` with pre-filled patient name, village, contact, and active condition.

---

## 7. Comprehensive SIH26133 Feature Area Capability Matrix

| Feature Area | Offline Local Operations (Room) | Online Sync Behavior (Firestore) | Fallback / Degradation |
| :--- | :--- | :--- | :--- |
| **Patient Registration & Caseload** | Full CRUD, search, filter, risk badges | Background sync via WorkManager | 100% offline functional |
| **Offline Health Cards & QR** | Render profile, blood group, allergies, QR | Syncs card edits on reconnect | Instant offline rendering |
| **Digital Prescriptions & OCR** | On-device ML Kit OCR & manual entry | Syncs Rx records to cloud | Works with zero internet |
| **ASHA Daily Village Rounds** | Full intake, vitals, high-risk flags | Pushed to outbox, auto-syncs | Uninterrupted field work |
| **Immunization Tracking** | Schedule checks, dose status, reminders | Synced to child health registry | Offline schedule calculation |
| **ASHA Medicine Kit Inventory** | Log dispensed drugs, stock tracking | Restock requests pushed to outbox | Local ledger preserved |
| **Dispensary Drug Stock** | Cached stock view, low-stock warnings | Live stock count sync | Freshness badge displayed |
| **OPD Queue & Token Slips** | Generate tokens, estimated wait time | Queue position sync | Local token slips validated |
| **Specialist Referrals** | Create referral, attach history/vitals | Specialist review on reconnect | Referral queued in outbox |
| **Diagnostic Lab Reports** | Cached pathology reports, CBC, lipid profile | New test orders queued | PDF generation offline |
| **Tele-Consultation Calls** | Local call logs, history, appointment sync | WebRTC audio/video | Degrades to audio on 2G |
| **District Outbreak Telemetry** | Cached disease maps & village statistics | Outbreak heatmap sync | Cached data timestamp banner |
| **Emergency SOS Dispatch** | Queues alert in Outbox | Dispatches to district queue | Direct 108 Call + ASHA SMS |

---

## 8. Developer Verification & Step-by-Step Testing Guide

### Test Suite Execution
Run the automated test suite verifying all network quality, outbox, and emergency SOS safety contracts:
```powershell
.\gradlew.bat testDebugUnitTest
```

### Manual Verification Steps
1. **Offline Simulation Test**:
   - Tap the top-right connectivity pill (🟢 Online) to switch to manual simulation mode.
   - Verify the pill changes to 🔴 **Offline** and the pending badge appears.
2. **Offline Data Creation Test**:
   - In ASHA portal, register a new patient or log a daily round.
   - Notice the data appears immediately in the UI (0ms latency).
   - Observe the pending sync badge increments (e.g. `🔄 1`).
3. **Reconnection & Sync Test**:
   - Tap the connectivity pill again to restore online mode.
   - Observe the spinning sync indicator ("Syncing changes…").
   - Confirm the outbox flushes and the badge resets to 0.
4. **Emergency SOS Zero-Internet Test**:
   - Set app to Offline mode.
   - Tap **Emergency SOS** on the Patient home screen.
   - Verify the clear offline notice is shown, spoken guidance advises offline status, and 1-tap **Call 108** and **SMS ASHA** buttons are immediately ready.

---

## 9. Performance, Battery & Future Scalability

- **WorkManager Constraints**: Periodic sync tasks use `NetworkType.CONNECTED` and `setRequiresBatteryNotLow(true)` to prevent battery drain.
- **Payload Compression**: Outbox payloads are stored as compact JSON strings and compressed during bulk sync over metered connections.
- **Zero Memory Leaks**: Room DAO queries use Kotlin Coroutines `Flow` with lifecycle-aware collection in Jetpack Compose (`collectAsStateWithLifecycle`).

package com.vitalsense.app.core.state

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.vitalsense.app.core.data.local.VitalSenseDatabase
import com.vitalsense.app.core.data.local.seed.SeedDataProvider
import com.vitalsense.app.core.data.model.*
import com.vitalsense.app.core.network.ConnectivityState
import com.vitalsense.app.core.network.NetworkMonitor
import com.vitalsense.app.core.sync.SyncManager
import com.vitalsense.app.core.ui.theme.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStateHolder @Inject constructor(
    @ApplicationContext private val context: Context,
    val networkMonitor: NetworkMonitor,
    val syncManager: SyncManager,
    private val database: VitalSenseDatabase
) {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val prefs by lazy {
        context.getSharedPreferences("vitalsense_prefs", Context.MODE_PRIVATE)
    }

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.PATIENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentLanguage: MutableStateFlow<AppLanguage>
    val currentLanguage: StateFlow<AppLanguage>

    init {
        val savedLangCode = prefs?.getString("selected_language", AppLanguage.ENGLISH.code)
        val initialLang = AppLanguage.values().firstOrNull { it.code == savedLangCode } ?: AppLanguage.ENGLISH
        _currentLanguage = MutableStateFlow(initialLang)
        currentLanguage = _currentLanguage.asStateFlow()

        try {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(initialLang.code))
        } catch (_: Exception) {}
    }

    private val _isPresentationLightMode = MutableStateFlow(true)
    val isPresentationLightMode: StateFlow<Boolean> = _isPresentationLightMode.asStateFlow()

    private val _activePatient = MutableStateFlow(SeedDataProvider.initialPatients.first())
    val activePatient: StateFlow<Patient> = _activePatient.asStateFlow()

    private val _activeAsha = MutableStateFlow(SeedDataProvider.initialAshaWorkers.first())
    val activeAsha: StateFlow<AshaWorker> = _activeAsha.asStateFlow()

    private val _activeDoctor = MutableStateFlow(SeedDataProvider.initialDoctors.first())
    val activeDoctor: StateFlow<Doctor> = _activeDoctor.asStateFlow()

    private val _activeProxyPatient = MutableStateFlow<Patient?>(null)
    val activeProxyPatient: StateFlow<Patient?> = _activeProxyPatient.asStateFlow()

    // Real-time network & sync state flows (SIH26133)
    val connectivityState: StateFlow<ConnectivityState> = networkMonitor.connectivityState
    val isOffline: StateFlow<Boolean> = networkMonitor.isManualOfflineForced
    val isSyncing: StateFlow<Boolean> = syncManager.isSyncing
    val lastSyncTimestamp: StateFlow<Long> = networkMonitor.lastSyncTimestamp
    val pendingOutboxCount: Flow<Int> = database.vitalSenseDao().getPendingOutboxCount()

    fun login(role: UserRole) {
        _currentRole.value = role
        _isLoggedIn.value = true
    }

    fun loginAsPatient(patient: Patient) {
        _activePatient.value = patient
        _currentRole.value = UserRole.PATIENT
        _isLoggedIn.value = true
    }

    fun loginAsAsha(asha: AshaWorker) {
        _activeAsha.value = asha
        _currentRole.value = UserRole.ASHA
        _isLoggedIn.value = true
    }

    fun loginAsDoctor(doctor: Doctor) {
        _activeDoctor.value = doctor
        _currentRole.value = UserRole.DOCTOR
        _isLoggedIn.value = true
    }

    fun loginAsAdmin() {
        _currentRole.value = UserRole.ADMIN
        _isLoggedIn.value = true
    }

    fun logout() {
        _isLoggedIn.value = false
        _activeProxyPatient.value = null
    }

    fun switchRole(newRole: UserRole) {
        _currentRole.value = newRole
    }

    fun setLanguage(language: AppLanguage) {
        prefs?.edit()?.putString("selected_language", language.code)?.apply()
        _currentLanguage.value = language
        try {
            java.util.Locale.setDefault(java.util.Locale(language.code))
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.code))
        } catch (_: Exception) {}
    }

    fun toggleLanguage() {
        val languages = AppLanguage.values()
        val nextIndex = (languages.indexOf(_currentLanguage.value) + 1) % languages.size
        setLanguage(languages[nextIndex])
    }

    fun togglePresentationTheme() {
        _isPresentationLightMode.value = !_isPresentationLightMode.value
    }

    fun selectPatient(patient: Patient) {
        _activePatient.value = patient
    }

    fun selectAsha(asha: AshaWorker) {
        _activeAsha.value = asha
    }

    fun selectDoctor(doctor: Doctor) {
        _activeDoctor.value = doctor
    }

    fun setProxyPatient(patient: Patient?) {
        _activeProxyPatient.value = patient
    }

    fun clearProxy() {
        _activeProxyPatient.value = null
    }

    fun toggleOffline() {
        networkMonitor.toggleManualOffline()
    }

    fun triggerSync() {
        syncManager.triggerImmediateSync()
    }
}

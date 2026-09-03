package com.vitalsense.app.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalsense.app.core.data.model.BroadcastNotice
import com.vitalsense.app.core.data.model.UserRole
import com.vitalsense.app.core.data.repository.VitalSenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: VitalSenseRepository
) : ViewModel() {

    val villages = repository.getVillages()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val notices = repository.getNotices()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val dispensaryStock = repository.getDispensaryStock()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allDepartments = repository.getDepartments()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleDepartmentStatus(department: com.vitalsense.app.core.data.model.Department) {
        viewModelScope.launch {
            repository.saveDepartment(department.copy(isActive = !department.isActive))
            _uiEvent.emit("Department ${department.name} is now ${if (!department.isActive) "Active" else "Inactive"}")
        }
    }

    private val _uiEvent = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun sendBroadcast(title: String, message: String, targetVillage: String? = null) {
        viewModelScope.launch {
            try {
                val notice = BroadcastNotice(
                    id = UUID.randomUUID().toString(),
                    senderRole = UserRole.ADMIN,
                    senderName = "District CMO",
                    targetRole = "ALL", // Simplified for prototype
                    targetVillage = targetVillage,
                    title = title,
                    message = message,
                    timestamp = System.currentTimeMillis(),
                    isUrgent = true
                )
                repository.sendNotice(notice)
                _uiEvent.emit("Broadcast sent successfully")
            } catch (e: Exception) {
                // Log and emit error safely
                _uiEvent.emit("Error sending broadcast: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }
}

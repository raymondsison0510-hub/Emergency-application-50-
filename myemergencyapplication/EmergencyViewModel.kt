package com.example.myemergencyapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EmergencyViewModel : ViewModel() {

    private val _user = MutableStateFlow(User.DemoUser)
    val user: StateFlow<User> = _user.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: SOS, 1: Report, 2: Dispatch, 3: Alerts
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _currentReport = MutableStateFlow(IncidentReport())
    val currentReport: StateFlow<IncidentReport> = _currentReport.asStateFlow()

    private val _isGpsActive = MutableStateFlow(true)
    val isGpsActive: StateFlow<Boolean> = _isGpsActive.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _isVoiceRecording = MutableStateFlow(false)
    val isVoiceRecording: StateFlow<Boolean> = _isVoiceRecording.asStateFlow()

    private val _isAdjustingPin = MutableStateFlow(false)
    val isAdjustingPin: StateFlow<Boolean> = _isAdjustingPin.asStateFlow()

    // Login methods
    fun loginDemoUser() {
        _user.value = User.DemoUser
    }

    fun loginUser(name: String, phone: String, address: String, emergencyContactName: String, emergencyContactPhone: String) {
        _user.value = User(
            fullName = name.ifBlank { "Resident User" },
            phone = phone.ifBlank { "09165975558" },
            address = address.ifBlank { "Brgy. Salitran II, Dasmariñas" },
            emergencyContactName = emergencyContactName,
            emergencyContactPhone = emergencyContactPhone,
            isLoggedIn = true
        )
    }

    fun logout() {
        _user.value = User(isLoggedIn = false)
    }

    // Navigation
    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun toggleGps() {
        _isGpsActive.value = !_isGpsActive.value
    }

    fun toggleOnline() {
        _isOnline.value = !_isOnline.value
    }

    // Report modification
    fun selectCategoryAndNavigateToReport(category: String) {
        _currentReport.update { current ->
            val descriptionText = when (category) {
                "Medical" -> "Medical Emergency reported at Salitran II. Immediate ambulance assistance requested."
                "Fire" -> "Fire incident reported nearby. Thick smoke visible. Urgent BFP response needed."
                "Crime / Accident" -> "Road incident/accident reported near Salitran Public Market. Requires barangay traffic enforcers and rescue."
                "Flood / Baha" -> "Rising flood waters reported in low-lying area of Salitran II. Assistance required."
                else -> current.description
            }
            val aiSug = when (category) {
                "Medical" -> "Medical Response Required"
                "Fire" -> "BFP Emergency Dispatch"
                "Crime / Accident" -> "Road Traffic Accident"
                "Flood / Baha" -> "Flood Rescue Request"
                else -> current.aiSuggestion
            }
            current.copy(
                category = category,
                description = descriptionText,
                aiSuggestion = aiSug
            )
        }
        _selectedTab.value = 1 // Switch to Report tab
    }

    fun updateDescription(text: String) {
        _currentReport.update { it.copy(description = text) }
    }

    fun setVoiceRecording(recording: Boolean) {
        _isVoiceRecording.value = recording
    }

    fun appendTranscribedVoiceText(text: String) {
        _currentReport.update { current ->
            val updated = if (current.description.isBlank()) text else "${current.description} $text"
            current.copy(description = updated, voiceTranscribed = true)
        }
    }

    fun attachPhoto(photoPath: String) {
        _currentReport.update { it.copy(attachedPhotoPath = photoPath) }
    }

    fun removeAttachedPhoto() {
        _currentReport.update { it.copy(attachedPhotoPath = null) }
    }

    fun updateSeverity(severity: SeverityLevel) {
        _currentReport.update { it.copy(severity = severity, severityConfirmed = false) }
    }

    fun setSeverityConfirmed(confirmed: Boolean) {
        _currentReport.update { it.copy(severityConfirmed = confirmed) }
    }

    fun updateLandmark(landmark: String) {
        _currentReport.update { it.copy(locationLandmark = landmark) }
    }

    fun updateInjuredCount(delta: Int) {
        _currentReport.update { current ->
            val newCount = (current.injuredCount + delta).coerceAtLeast(0)
            current.copy(injuredCount = newCount)
        }
    }

    fun setAdjustingPin(adjusting: Boolean) {
        _isAdjustingPin.value = adjusting
    }

    fun updateLocationCoords(lat: Double, lng: Double) {
        _currentReport.update { it.copy(latitude = lat, longitude = lng) }
    }

    // Actions
    fun triggerSosHoldComplete() {
        // Create an urgent SOS report
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val randomId = "DS-2025-" + (100..999).random()
        
        _currentReport.value = IncidentReport(
            id = randomId,
            category = "SOS Alert",
            description = "DIRECT SOS EMERGENCY TRIGGERED by user press-and-hold action. Instant location GPS payload transmitted.",
            voiceTranscribed = false,
            aiSuggestion = "Critical SOS Emergency",
            aiResponseAdvice = "Immediate Tanod & CDRRMO dispatch initiated.",
            severity = SeverityLevel.CRITICAL,
            severityConfirmed = true,
            currentStepIndex = 2, // En Route
            timestamp = currentTime,
            etaMinutes = 2,
            distanceKm = 0.3
        )
        _selectedTab.value = 2 // Switch to Dispatch tab immediately
    }

    fun submitIncidentReport() {
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        _currentReport.update { current ->
            current.copy(
                currentStepIndex = 2, // En Route
                timestamp = currentTime,
                severityConfirmed = true
            )
        }
        _selectedTab.value = 2 // Switch to Live Tracking / Dispatch tab
    }

    fun advanceSimulationStep() {
        _currentReport.update { current ->
            val nextStep = (current.currentStepIndex + 1) % 5
            val updatedEta = when (nextStep) {
                0 -> 5
                1 -> 4
                2 -> 3
                3 -> 0 // Arrived
                4 -> 0 // Resolved
                else -> 0
            }
            val updatedDist = when (nextStep) {
                0 -> 1.2
                1 -> 0.8
                2 -> 0.4
                3 -> 0.0
                4 -> 0.0
                else -> 0.0
            }
            current.copy(
                currentStepIndex = nextStep,
                etaMinutes = updatedEta,
                distanceKm = updatedDist
            )
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val userMsg = ChatMessage("You", text, timeNow)
        _currentReport.update { current ->
            val list = current.chatMessages + userMsg
            current.copy(chatMessages = list)
        }
    }

    fun addNoteForResponders(note: String) {
        if (note.isBlank()) return
        _currentReport.update { current ->
            val list = current.updatesAndNotes + note
            current.copy(updatesAndNotes = list)
        }
    }
}

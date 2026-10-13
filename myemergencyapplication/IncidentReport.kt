package com.example.myemergencyapplication

enum class SeverityLevel(val label: String) {
    MILD("Mild"),
    URGENT("Urgent"),
    CRITICAL("Critical")
}

data class ChatMessage(
    val sender: String, // "You" or "Responder"
    val text: String,
    val timestamp: String
)

data class IncidentReport(
    val id: String = "DS-2025-084",
    val category: String = "Crime / Accident",
    val description: String = "Two motorcycles collided in front of Salitran Public Market along Aguinaldo Highway. One rider is bleeding and unable to stand; traffic is starting to build up.",
    val voiceTranscribed: Boolean = true,
    val attachedPhotoPath: String? = null,
    val aiSuggestion: String = "Road Traffic Accident",
    val aiResponseAdvice: String = "Preparing ambulance and barangay traffic enforcers.",
    val severity: SeverityLevel = SeverityLevel.URGENT,
    val severityConfirmed: Boolean = true,
    val locationLandmark: String = "In front of Salitran Public Market",
    val locationArea: String = "Salitran II",
    val injuredCount: Int = 1,
    val latitude: Double = 14.3283,
    val longitude: Double = 120.9383,
    val currentStepIndex: Int = 2, // 0..4 (2 = En Route)
    val responderName: String = "Tanod Leader R. Macaranas",
    val responderUnit: String = "Unit-02 Rescue Van • Brgy. Salitran II",
    val responderPhone: String = "09171234567",
    val etaMinutes: Int = 3,
    val distanceKm: Double = 0.4,
    val timestamp: String = "10:24 AM",
    val updatesAndNotes: List<String> = listOf(
        "Initial report received via voice transcription.",
        "Dispatched Unit-02 Patrol Team."
    ),
    val chatMessages: List<ChatMessage> = listOf(
        ChatMessage("Responder", "Hello! Unit-02 Patrol is en route to Salitran II. Please stay calm.", "10:27 AM")
    )
)

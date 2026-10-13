package com.example.myemergencyapplication

data class EmergencyAlert(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val category: String, // "Typhoon / Rain", "Road Operations", "Security", "General"
    val severity: SeverityLevel
)

object SampleAlerts {
    val alerts = listOf(
        EmergencyAlert(
            id = "ALT-001",
            title = "Heavy Rainfall Warning: Yellow Level",
            description = "Moderate to heavy rains expected over Dasmariñas, Cavite. Residents in flood-prone areas of Brgy. Salitran II are advised to stay alert.",
            date = "Today, 09:30 AM",
            category = "Typhoon / Rain",
            severity = SeverityLevel.URGENT
        ),
        EmergencyAlert(
            id = "ALT-002",
            title = "Road Maintenance along Aguinaldo Highway",
            description = "Partial lane closure near Salitran Public Market for ongoing drainage repairs. Traffic enforcers deployed.",
            date = "Today, 08:00 AM",
            category = "Road Operations",
            severity = SeverityLevel.MILD
        ),
        EmergencyAlert(
            id = "ALT-003",
            title = "Barangay Peace & Security Advisory",
            description = "Increased tanod patrols scheduled around Salitran II residential subdivisions between 10:00 PM and 5:00 AM.",
            date = "Yesterday, 06:00 PM",
            category = "Security",
            severity = SeverityLevel.MILD
        )
    )
}

package com.example.myemergencyapplication

data class User(
    val fullName: String = "",
    val phone: String = "",
    val address: String = "Brgy. Salitran II, Dasmariñas",
    val emergencyContactName: String = "",
    val emergencyContactPhone: String = "",
    val isLoggedIn: Boolean = false
) {
    companion object {
        val DemoUser = User(
            fullName = "Raymond A Sison",
            phone = "09165975558",
            address = "Block 12 Lot 5, Salitran II, Dasmariñas, Cavite",
            emergencyContactName = "Maria Sison (Wife)",
            emergencyContactPhone = "0918 777 8899",
            isLoggedIn = true
        )
    }
}

package com.example.myemergencyapplication

data class EmergencyHotline(
    val name: String,
    val number: String,
    val iconType: HotlineType
)

enum class HotlineType {
    CDRRMO,
    OUTPOST,
    FIRE,
    RED_CROSS
}

object HotlineData {
    val defaultHotlines = listOf(
        EmergencyHotline("Dasmariñas CDRRMO", "(046) 416-0267", HotlineType.CDRRMO),
        EmergencyHotline("Brgy. Salitran II Outpost", "(046) 416-1111", HotlineType.OUTPOST),
        EmergencyHotline("BFP Fire Station Dasma", "(046) 416-0294", HotlineType.FIRE),
        EmergencyHotline("Philippine Red Cross Dasma", "(046) 484-3180", HotlineType.RED_CROSS)
    )
}

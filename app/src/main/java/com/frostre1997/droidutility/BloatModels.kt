package com.frostre1997.droidutility

enum class BloatCategory {
    GOOGLE, HUAWEI, LENOVO, MOTOROLA, ONEPLUS, OPPO, REALME, SAMSUNG, XIAOMI,
    OEM_BLOATWARE, CARRIER_APPS, SOCIAL_MEDIA, GAMES, PRODUCTIVITY_BLOAT,
    TRACKING_SPYWARE, ADVERTISING, CLOUD_SERVICES, REDUNDANT_APPS, PRIVACY_CONCERNING,
    GENERAL
}

enum class RiskLevel {
    SAFE,
    CAUTION,
    PRIVACY_CONCERNING,
    ADVANCED,
    UNSAFE
}

data class BloatApp(
    val packageName: String,
    val name: String,
    val description: String,
    val category: BloatCategory,
    val risklevel: RiskLevel,
    val alternatives: List<String>? = null
)

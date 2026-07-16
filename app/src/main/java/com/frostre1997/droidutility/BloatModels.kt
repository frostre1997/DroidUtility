package com.frostre1997.droidutility

enum class BloatCategory {
    OEM_BLOATWARE,
    CARRIER_APPS,
    SOCIAL_MEDIA,
    GAMES,
    PRODUCTIVITY_BLOAT,
    TRACKING_SPYWARE,
    ADVERTISING,
    CLOUD_SERVICES,
    REDUNDANT_APPS,
    PRIVACY_CONCERNING
}

enum class BloatRisk {
    SAFE,
    CAUTION,
    ADVANCED
}

data class BloatApp(
    val packageName: String,
    val name: String,
    val description: String,
    val category: BloatCategory,
    val risk: BloatRisk = BloatRisk.SAFE,
    val alternatives: String = ""
)

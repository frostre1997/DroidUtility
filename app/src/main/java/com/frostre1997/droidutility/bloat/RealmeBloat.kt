package com.frostre1997.droidutility

object RealmeBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.heytap.browser",
            name = "Realme Browser",
            description = "Built-in web browser (shared with OPPO)",
            category = BloatCategory.REDUNDANT_APPS,
            risk = BloatRisk.SAFE,
            alternatives = "Chrome, Firefox"
        ),
        BloatApp(
            packageName = "com.heytap.cloud",
            name = "HeyTap Cloud",
            description = "Cloud backup service (shared with OPPO)",
            category = BloatCategory.CLOUD_SERVICES,
            risk = BloatRisk.SAFE,
            alternatives = "Google Drive"
        ),
        BloatApp(
            packageName = "com.coloros.gamespace",
            name = "Game Space",
            description = "Gaming performance optimizer (shared with OPPO)",
            category = BloatCategory.GAMES,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.heytap.market",
            name = "GetApps",
            description = "Realme app store (shared with OPPO)",
            category = BloatCategory.REDUNDANT_APPS,
            risk = BloatRisk.SAFE,
            alternatives = "Google Play Store"
        ),
        BloatApp(
            packageName = "com.realme.screenrecorder",
            name = "Screen Recorder",
            description = "Built-in screen recording tool",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.realme.phonemanager",
            name = "Phone Manager",
            description = "System cleanup and optimization tool",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.realme.oshare",
            name = "Realme Share",
            description = "File sharing between Realme devices",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        )
    )
}

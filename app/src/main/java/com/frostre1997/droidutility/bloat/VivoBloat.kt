package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.RiskLevel

object VivoBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.iqoo.gamecenter",
            name = "iQOO Game Center",
            description = "Gaming hub and performance manager",
            category = BloatCategory.GAMES,
            riskLevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.bbk.cloud",
            name = "vivo Cloud",
            description = "Cloud backup and sync service",
            category = BloatCategory.CLOUD_SERVICES,
            riskLevel = RiskLevel.SAFE,
            alternatives = listOf("Google Drive")
        ),
        BloatApp(
            packageName = "com.vivo.weather",
            name = "vivo Weather",
            description = "Default weather application",
            category = BloatCategory.REDUNDANT_APPS,
            riskLevel = RiskLevel.SAFE,
            alternatives = listOf("Google Weather")
        ),
        BloatApp(
            packageName = "com.vivo.browser",
            name = "vivo Browser",
            description = "Built-in web browser",
            category = BloatCategory.REDUNDANT_APPS,
            riskLevel = RiskLevel.SAFE,
            alternatives = listOf("Chrome", "Firefox")
        ),
        BloatApp(
            packageName = "com.vivo.easyshare",
            name = "EasyShare",
            description = "File transfer between vivo devices",
            category = BloatCategory.OEM_BLOATWARE,
            riskLevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.vivo.thememanager",
            name = "Theme Manager",
            description = "vivo theme and wallpaper store",
            category = BloatCategory.OEM_BLOATWARE,
            riskLevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.vivo.appstore",
            name = "vivo App Store",
            description = "vivo's proprietary app store",
            category = BloatCategory.REDUNDANT_APPS,
            riskLevel = RiskLevel.SAFE,
            alternatives = listOf("Google Play Store")
        ),
        BloatApp(
            packageName = "com.vivo.push",
            name = "vivo Push",
            description = "Background push notification service",
            category = BloatCategory.TRACKING_SPYWARE,
            riskLevel = RiskLevel.CAUTION
        )
    )
}

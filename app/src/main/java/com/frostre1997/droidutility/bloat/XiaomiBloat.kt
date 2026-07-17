package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.BloatRisk

object XiaomiBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.miui.cleanmaster",
            name = "Clean Master",
            description = "MIUI junk file cleaner and optimizer",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.miui.msa.global",
            name = "MSA (MIUI System Ads)",
            description = "System-level advertising service",
            category = BloatCategory.ADVERTISING,
            risk = BloatRisk.PRIVACY_CONCERNING
        ),
        BloatApp(
            packageName = "com.miui.daemon",
            name = "MIUI Daemon",
            description = "Background telemetry and diagnostic service",
            category = BloatCategory.TRACKING_SPYWARE,
            risk = BloatRisk.PRIVACY_CONCERNING
        ),
        BloatApp(
            packageName = "com.miui.analytics",
            name = "MIUI Analytics",
            description = "Usage analytics collection service",
            category = BloatCategory.TRACKING_SPYWARE,
            risk = BloatRisk.PRIVACY_CONCERNING
        ),
        BloatApp(
            packageName = "com.miui.securityadd",
            name = "Security Add-on",
            description = "Additional security module for MIUI Security",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.CAUTION
        ),
        BloatApp(
            packageName = "com.miui.hybrid",
            name = "MIUI Quick Apps",
            description = "Instant apps framework for MIUI",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.miui.contentcatcher",
            name = "Content Catcher",
            description = "Captures and organizes content from apps",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.miui.weather2",
            name = "MIUI Weather",
            description = "Built-in weather app with ads",
            category = BloatCategory.REDUNDANT_APPS,
            risk = BloatRisk.SAFE,
            alternatives = "Google Weather, Weather.com"
        ),
        BloatApp(
            packageName = "com.miui.compass",
            name = "MIUI Compass",
            description = "Built-in compass application",
            category = BloatCategory.REDUNDANT_APPS,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.miui.player",
            name = "MIUI Music",
            description = "Built-in music player with online streaming",
            category = BloatCategory.REDUNDANT_APPS,
            risk = BloatRisk.SAFE,
            alternatives = "Poweramp, Musicolet"
        ),
        BloatApp(
            packageName = "com.miui.gallery",
            name = "MIUI Gallery",
            description = "Default gallery app with cloud sync ads",
            category = BloatCategory.REDUNDANT_APPS,
            risk = BloatRisk.SAFE,
            alternatives = "Google Photos, Simple Gallery"
        ),
        BloatApp(
            packageName = "com.miui.bugreport",
            name = "MIUI Bug Report",
            description = "Sends bug reports and diagnostics to Xiaomi",
            category = BloatCategory.TRACKING_SPYWARE,
            risk = BloatRisk.CAUTION
        )
    )
}

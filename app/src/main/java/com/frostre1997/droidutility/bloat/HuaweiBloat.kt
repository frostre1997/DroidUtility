package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.RiskLevel

object HuaweiBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.huawei.health",
            name = "Huawei Health",
            description = "Health and fitness tracking companion app",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.huawei.hwid",
            name = "Huawei Mobile Services",
            description = "Core Huawei services framework (GMS replacement)",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.CAUTION
        ),
        BloatApp(
            packageName = "com.huawei.systemmanager",
            name = "Phone Manager",
            description = "System optimization and cleanup tool",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.huawei.appmarket",
            name = "AppGallery",
            description = "Huawei's alternative app store",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Google Play Store", "Aurora Store")
        ),
        BloatApp(
            packageName = "com.huawei.cloud",
            name = "Huawei Cloud",
            description = "Cloud backup and sync service",
            category = BloatCategory.CLOUD_SERVICES,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Google Drive")
        ),
        BloatApp(
            packageName = "com.huawei.ads",
            name = "Huawei Ads",
            description = "Advertising framework for Huawei apps",
            category = BloatCategory.ADVERTISING,
            risklevel = RiskLevel.PRIVACY_CONCERNING
        ),
        BloatApp(
            packageName = "com.huawei.trustagent",
            name = "Trust Agent",
            description = "Device trust and smart unlock features",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.CAUTION
        ),
        BloatApp(
            packageName = "com.huawei.hwpush",
            name = "Huawei Push",
            description = "Background push notification service",
            category = BloatCategory.TRACKING_SPYWARE,
            risklevel = RiskLevel.CAUTION
        )
    )
}

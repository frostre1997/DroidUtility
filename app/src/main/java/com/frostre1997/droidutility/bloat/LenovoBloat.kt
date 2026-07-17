package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.BloatRisk

object LenovoBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.lenovo.safecenter",
            name = "Lenovo Security",
            description = "Security and optimization suite",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.lenovo.lpe.agent",
            name = "Lenovo Experience Hub",
            description = "Device tips and feature discovery",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.lenovo.anyshare.gps",
            name = "SHAREit",
            description = "File sharing app (pre-installed bloatware)",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = BloatRisk.PRIVACY_CONCERNING,
            alternatives = listOf("Nearby Share", "Snapdrop")
        ),
        BloatApp(
            packageName = "com.lenovo.leos.cloud.sync",
            name = "Lenovo Cloud",
            description = "Cloud backup and sync service",
            category = BloatCategory.CLOUD_SERVICES,
            risklevel = BloatRisk.SAFE,
            alternatives = listOf("Google Drive")
        ),
        BloatApp(
            packageName = "com.lenovo.gamecenter",
            name = "Lenovo Game Center",
            description = "Gaming hub and optimization",
            category = BloatCategory.GAMES,
            risklevel = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.lenovo.launcher",
            name = "Lenovo Launcher",
            description = "Default Lenovo home screen launcher",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = BloatRisk.SAFE,
            alternatives = listOf("Nova Launcher", "Lawnchair")
        )
    )
}

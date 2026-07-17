package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.BloatRisk
import com.frostre1997.droidutility.data.BloatApp
import com.frostre1997.droidutility.data.BloatCategory

object MotorolaBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.motorola.actions",
            name = "Actions",
            description = "Motorola gesture and shortcut actions",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.motorola.gamemode",
            name = "Game Time",
            description = "Gaming performance mode manager",
            category = BloatCategory.GAMES,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.motorola.display",
            name = "Moto Display",
            description = "Ambient display and notification features",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.motorola.attentivescreen",
            name = "Attentive Display",
            description = "Keeps screen on while user is looking at it",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.motorola.moto",
            name = "Moto App",
            description = "Motorola tips, support, and device info hub",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.motorola.fmplayer",
            name = "FM Radio",
            description = "Built-in FM radio player",
            category = BloatCategory.REDUNDANT_APPS,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.motorola.bugreportsender",
            name = "Bug Report Sender",
            description = "Sends diagnostic reports to Motorola",
            category = BloatCategory.TRACKING_SPYWARE,
            risk = BloatRisk.CAUTION
        )
    )
}

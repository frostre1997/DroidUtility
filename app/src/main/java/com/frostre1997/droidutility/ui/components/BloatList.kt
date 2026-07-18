package com.frostre1997.droidutility.ui.components

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.bloat.GoogleBloat
import com.frostre1997.droidutility.bloat.HuaweiBloat
import com.frostre1997.droidutility.bloat.LenovoBloat
import com.frostre1997.droidutility.bloat.MotorolaBloat
import com.frostre1997.droidutility.bloat.OnePlusBloat
import com.frostre1997.droidutility.bloat.OppoBloat
import com.frostre1997.droidutility.bloat.RealmeBloat
import com.frostre1997.droidutility.bloat.SamsungBloat
import com.frostre1997.droidutility.bloat.XiaomiBloat

object BloatList {
    val ALL: List<BloatApp> = buildList {
        addAll(SamsungBloat.BLOATWARE)
        addAll(XiaomiBloat.BLOATWARE)
        addAll(OnePlusBloat.BLOATWARE)
        addAll(OppoBloat.BLOATWARE)
        addAll(RealmeBloat.BLOATWARE)
        addAll(HuaweiBloat.BLOATWARE)
        addAll(LenovoBloat.BLOATWARE)
        addAll(MotorolaBloat.BLOATWARE)
        addAll(GoogleBloat.BLOATWARE)
    }
}

package com.frostre1997.droidutility

import com.frostre1997.droidutility.bloat.GoogleBloat
import com.frostre1997.droidutility.bloat.HuaweiBloat
import com.frostre1997.droidutility.bloat.LenovoBloat
import com.frostre1997.droidutility.bloat.MotorolaBloat
import com.frostre1997.droidutility.bloat.OnePlusBloat
import com.frostre1997.droidutility.bloat.OppoBloat
import com.frostre1997.droidutility.bloat.RealmeBloat
import com.frostre1997.droidutility.bloat.SamsungBloat
import com.frostre1997.droidutility.bloat.VivoBloat
import com.frostre1997.droidutility.bloat.XiaomiBloat
import com.frostre1997.droidutility.data.BloatApp

object BloatList {
    val ALL: List<BloatApp> = buildList {
        addAll(SamsungBloat.BLOATWARE)
        addAll(XiaomiBloat.BLOATWARE)
        addAll(OnePlusBloat.BLOATWARE)
        addAll(HuaweiBloat.BLOATWARE)
        addAll(OppoBloat.BLOATWARE)
        addAll(VivoBloat.BLOATWARE)
        addAll(RealmeBloat.BLOATWARE)
        addAll(MotorolaBloat.BLOATWARE)
        addAll(GoogleBloat.BLOATWARE)
        addAll(LenovoBloat.BLOATWARE)
    }
}

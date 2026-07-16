package com.frostre1997.droidutility

object BloatList {
    val ALL = buildList {
        addAll(SamsungBloat.BLOATWARE)
        addAll(XiaomiBloat.BLOATWARE)
        addAll(OnePlusBloat.BLOATWARE)
        addAll(HuaweiBloat.BLOATWARE)
        addAll(OppoBloat.BLOATWARE)
        addAll(VivoBloat.BLOATWARE)
        addAll(RealmeBloat.BLOATWARE)
        addAll(MotorolaBloat.BLOATWARE)
        addAll(GoogleBloat.BLOATWARE)
    }
}

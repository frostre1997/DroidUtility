package com.frostre1997.droidutility.data

data class BloatApp(
    val packageName: String,
    val name: String,
    val category: BloatCategory,
    val description: String = "",
    val isSystem: Boolean = false
)

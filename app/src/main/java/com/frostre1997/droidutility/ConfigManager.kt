package com.frostre1997.droidutility

import android.content.Context
import org.json.JSONObject

data class Tweak(val name: String, val command: String)

object ConfigManager {
    fun loadTweaks(context: Context): List<Tweak> {
        val jsonString = context.assets.open("configs/tweaks.json")
            .bufferedReader().use { it.readText() }
        
        val json = JSONObject(jsonString)
        val array = json.getJSONArray("tweaks")
        val list = mutableListOf<Tweak>()
        
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            list.add(Tweak(item.getString("name"), item.getString("command")))
        }
        return list
    }
}


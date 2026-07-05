package com.frostre1997.droidutility

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import rikka.shizuku.Shizuku

class MainActivity : ComponentActivity() {
    
    // Listener per il risultato della richiesta permessi
    private val REQUEST_CODE_SHIZUKU = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Verifica permessi all'avvio
        checkShizukuPermission()
        
        setContent {
            // Qui richiamerai la tua Dashboard (come definito prima)
            CommandDashboard()
        }
    }

    private fun checkShizukuPermission() {
        if (Shizuku.pingBinder()) {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                if (Shizuku.shouldShowRequestPermissionRationale()) {
                    // Qui potresti mostrare un avviso all'utente
                } else {
                    Shizuku.requestPermission(REQUEST_CODE_SHIZUKU)
                }
            }
        }
    }
}

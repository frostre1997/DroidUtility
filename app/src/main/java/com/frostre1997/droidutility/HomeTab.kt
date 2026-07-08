package com.frostre1997.droidutility

import android.app.Activity
import android.content.Context
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun HomeTab() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Shizuku state using simple mutable state
    var shizukuAvailable by remember { mutableStateOf(false) }
    var shizukuPermission by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    fun refreshState() {
        scope.launch {
            isRefreshing = true
            // Force ShizukuShellManager to refresh its internal state
            ShizukuShellManager.refreshState()
            shizukuAvailable = ShizukuShellManager.checkAvailability()
            shizukuPermission = ShizukuShellManager.hasPermission()
            isRefreshing = false
        }
    }

    // Load on first composition
    LaunchedEffect(Unit) {
        refreshState()
    }

    // App info
    val appVersion = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) { "1.0.0" }
    val packageName = context.packageName

    // Device info (safe)
    val androidVersion = Build.VERSION.RELEASE
    val sdkVersion = Build.VERSION.SDK_INT
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    val cpuArch = Build.SUPPORTED_ABIS.firstOrNull() ?: "unknown"
    val batteryLevel = try {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    } catch (e: Exception) { 0 }
    val batteryHealth = try {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val health = bm.getIntProperty(4) // BATTERY_PROPERTY_HEALTH = 4
        when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Failure"
            else -> "Unknown"
        }
    } catch (e: Exception) { "Unknown" }

    val storageInfo = try {
        val stat = StatFs(Environment.getDataDirectory().path)
        val totalGB = stat.totalBytes / (1024.0 * 1024.0 * 1024.0)
        val availGB = stat.availableBytes / (1024.0 * 1024.0 * 1024.0)
        String.format("%.1f GB / %.1f GB", availGB, totalGB)
    } catch (e: Exception) { "Unknown" }

    val ramInfo = try {
        val mem = File("/proc/meminfo").readText()
        val total = Regex("MemTotal:\\s+(\\d+)\\s+kB").find(mem)?.groupValues?.get(1)?.toIntOrNull()?.div(1024) ?: 0
        val avail = Regex("MemAvailable:\\s+(\\d+)\\s+kB").find(mem)?.groupValues?.get(1)?.toIntOrNull()?.div(1024) ?: 0
        "${total} MB" to "${avail} MB"
    } catch (e: Exception) { "Unknown" to "Unknown" }

    val uptime = try {
        val sec = android.os.SystemClock.elapsedRealtime() / 1000
        val d = sec / 86400
        val h = (sec % 86400) / 3600
        val m = (sec % 3600) / 60
        when {
            d > 0 -> "${d}d ${h}h ${m}m"
            h > 0 -> "${h}h ${m}m"
            else -> "${m}m"
        }
    } catch (e: Exception) { "Unknown" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("DroidUtility", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("v$appVersion • $packageName", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
            Row {
                IconButton(onClick = { refreshState() }) {
                    if (isRefreshing) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    else Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
                IconButton(onClick = { refreshState() }) {
                    Icon(Icons.Default.Sync, contentDescription = "Sync Shizuku")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Shizuku Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (shizukuAvailable && shizukuPermission)
                    MaterialTheme.colorScheme.tertiaryContainer
                else if (shizukuAvailable && !shizukuPermission)
                    MaterialTheme.colorScheme.secondaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (shizukuAvailable && shizukuPermission) Icons.Default.CheckCircle
                            else if (shizukuAvailable && !shizukuPermission) Icons.Default.Warning
                            else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (shizukuAvailable && shizukuPermission) Color(0xFF4CAF50)
                            else if (shizukuAvailable && !shizukuPermission) Color(0xFFFFA726)
                            else Color(0xFFEF5350),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (shizukuAvailable && shizukuPermission) "Shizuku Ready ✅"
                            else if (shizukuAvailable && !shizukuPermission) "Permission Required"
                            else "Shizuku Not Running",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (shizukuAvailable && !shizukuPermission) {
                        Button(
                            onClick = {
                                if (context is Activity) {
                                    ShizukuShellManager.requestPermission(context)
                                    Toast.makeText(context, "Check Shizuku app", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Grant", fontSize = 11.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                val shizukuVersion = try { rikka.shizuku.Shizuku.getVersion() } catch (e: Exception) { -1 }
                if (shizukuVersion != -1) {
                    Text("Shizuku v$shizukuVersion", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                if (!shizukuAvailable) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            try {
                                val intent = context.packageManager.getLaunchIntentForPackage("moe.shizuku.manager")
                                if (intent != null) context.startActivity(intent)
                                else Toast.makeText(context, "Shizuku not installed", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Cannot open Shizuku", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Shizuku")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // System Status Section
        Text("System Status", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 8.dp))

        val statusItems = listOf(
            "Android Version" to androidVersion,
            "Manufacturer" to manufacturer,
            "Device Model" to model,
            "CPU Architecture" to cpuArch,
            "SDK Level" to sdkVersion.toString(),
            "Battery Level" to "$batteryLevel%",
            "Battery Health" to batteryHealth,
            "Total RAM" to ramInfo.first,
            "Available RAM" to ramInfo.second,
            "Storage (data)" to storageInfo,
            "Uptime" to uptime
        )

        for (i in statusItems.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusCard(statusItems[i].first, statusItems[i].second, Modifier.weight(1f))
                if (i + 1 < statusItems.size) {
                    StatusCard(statusItems[i + 1].first, statusItems[i + 1].second, Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun StatusCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value.takeIf { it.isNotBlank() } ?: "Unknown", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, maxLines = 2)
        }
    }
}

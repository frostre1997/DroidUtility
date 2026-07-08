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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun HomeTab() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val shizukuState by ShizukuShellManager.shizukuState.collectAsState()
    
    val appVersion = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) { "1.0.0" }
    val packageName = context.packageName

    val androidVersion = Build.VERSION.RELEASE
    val sdkVersion = Build.VERSION.SDK_INT
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    val cpuArch = Build.SUPPORTED_ABIS.firstOrNull() ?: "unknown"

    val batteryLevel = getBatteryLevel(context)
    val batteryHealth = getBatteryHealth(context)

    val storageInfo = getStorageInfo()
    val ramInfo = getRamInfo()
    val uptime = getUptime()

    var isRefreshing by remember { mutableStateOf(false) }

    fun refresh() {
        scope.launch {
            isRefreshing = true
            delay(500)
            isRefreshing = false
        }
    }

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
                Text(
                    text = "DroidUtility",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "v$appVersion • $packageName",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            IconButton(
                onClick = { refresh() },
                enabled = !isRefreshing
            ) {
                if (isRefreshing) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Shizuku Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (shizukuState) {
                    ShizukuShellManager.ShizukuState.AVAILABLE_GRANTED -> MaterialTheme.colorScheme.tertiaryContainer
                    ShizukuShellManager.ShizukuState.AVAILABLE_NO_PERMISSION -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.errorContainer
                }
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
                            when (shizukuState) {
                                ShizukuShellManager.ShizukuState.AVAILABLE_GRANTED -> Icons.Default.CheckCircle
                                ShizukuShellManager.ShizukuState.AVAILABLE_NO_PERMISSION -> Icons.Default.Warning
                                else -> Icons.Default.Error
                            },
                            contentDescription = null,
                            tint = when (shizukuState) {
                                ShizukuShellManager.ShizukuState.AVAILABLE_GRANTED -> Color(0xFF4CAF50)
                                ShizukuShellManager.ShizukuState.AVAILABLE_NO_PERMISSION -> Color(0xFFFFA726)
                                else -> Color(0xFFEF5350)
                            },
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            when (shizukuState) {
                                ShizukuShellManager.ShizukuState.AVAILABLE_GRANTED -> "Shizuku Ready ✅"
                                ShizukuShellManager.ShizukuState.AVAILABLE_NO_PERMISSION -> "Permission Required"
                                else -> "Shizuku Not Running"
                            },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (shizukuState == ShizukuShellManager.ShizukuState.AVAILABLE_NO_PERMISSION) {
                        Button(
                            onClick = {
                                if (context is Activity) {
                                    ShizukuShellManager.requestPermission(context)
                                    Toast.makeText(context, "Check Shizuku app", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Grant", fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                val shizukuVersion = try {
                    rikka.shizuku.Shizuku.getVersion()
                } catch (e: Exception) { -1 }

                if (shizukuVersion != -1) {
                    Text(
                        "Shizuku v$shizukuVersion",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // System Status Section
        Text(
            "System Status",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusCard(
                    label = statusItems[i].first,
                    value = statusItems[i].second,
                    modifier = Modifier.weight(1f)
                )
                if (i + 1 < statusItems.size) {
                    StatusCard(
                        label = statusItems[i + 1].first,
                        value = statusItems[i + 1].second,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun StatusCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                label,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value.takeIf { it.isNotBlank() } ?: "Unknown",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )
        }
    }
}

// ─── Helper functions ──────────────────────────────────────────────────────

fun getBatteryLevel(context: Context): Int {
    return try {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    } catch (e: Exception) { 0 }
}

fun getBatteryHealth(context: Context): String {
    return try {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val health = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_HEALTH)
        when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Failure"
            else -> "Unknown"
        }
    } catch (e: Exception) { "Unknown" }
}

fun getStorageInfo(): String {
    return try {
        val stat = StatFs(Environment.getDataDirectory().path)
        val totalBytes = stat.totalBytes
        val availableBytes = stat.availableBytes
        val totalGB = totalBytes / (1024.0 * 1024.0 * 1024.0)
        val availableGB = availableBytes / (1024.0 * 1024.0 * 1024.0)
        String.format("%.1f GB / %.1f GB", availableGB, totalGB)
    } catch (e: Exception) { "Unknown" }
}

fun getRamInfo(): Pair<String, String> {
    return try {
        val memInfo = File("/proc/meminfo").readText()
        val totalMatch = Regex("MemTotal:\\s+(\\d+)\\s+kB").find(memInfo)
        val availMatch = Regex("MemAvailable:\\s+(\\d+)\\s+kB").find(memInfo)
        val totalMB = totalMatch?.groupValues?.get(1)?.toIntOrNull()?.div(1024) ?: 0
        val availMB = availMatch?.groupValues?.get(1)?.toIntOrNull()?.div(1024) ?: 0
        "${totalMB} MB" to "${availMB} MB"
    } catch (e: Exception) { "Unknown" to "Unknown" }
}

fun getUptime(): String {
    return try {
        val uptimeSeconds = android.os.SystemClock.elapsedRealtime() / 1000
        val days = uptimeSeconds / 86400
        val hours = (uptimeSeconds % 86400) / 3600
        val minutes = (uptimeSeconds % 3600) / 60
        when {
            days > 0 -> "${days}d ${hours}h ${minutes}m"
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    } catch (e: Exception) { "Unknown" }
}

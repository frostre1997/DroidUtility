package com.frostre1997.droidutility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.input.key.KeyEvent
import android.view.KeyEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.Executors

@Composable
fun ConsoleTab() {
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }
    var lines by remember { mutableStateOf(listOf("$ ") + getMotd()) }
    val listState = rememberLazyListState()
    var process: Process? by remember { mutableStateOf(null) }
    var isRunning by remember { mutableStateOf(false) }
    val isShizukuReady = remember { ShizukuShellManager.checkAvailability() && ShizukuShellManager.hasPermission() }

    // Start the shell
    fun startShell() {
        try {
            if (!isShizukuReady) {
                lines = lines + "Error: Shizuku not ready. Start Shizuku and grant permission."
                return
            }
            if (process != null) {
                lines = lines + "Shell already running."
                return
            }

            val newProcess = ShizukuShellManager.startPersistentShell()
            if (newProcess == null) {
                lines = lines + "Failed to start shell via Shizuku."
                return
            }
            process = newProcess
            isRunning = true
            lines = lines + "Shell started successfully (Shizuku). Type 'exit' to close."

            // Read output in background
            Executors.newSingleThreadExecutor().execute {
                try {
                    val reader = BufferedReader(InputStreamReader(process!!.inputStream))
                    while (true) {
                        val line = reader.readLine() ?: break
                        scope.launch {
                            lines = lines + line
                            listState.animateScrollToItem(lines.size - 1)
                        }
                    }
                } catch (_: Exception) {
                    // Process closed
                } finally {
                    scope.launch {
                        isRunning = false
                        process = null
                        lines = lines + "Shell closed."
                    }
                }
            }
        } catch (e: Exception) {
            lines = lines + "Error: ${e.message}"
        }
    }

    // Execute a command
    fun executeCommand(cmd: String) {
        if (process == null) {
            lines = lines + "Error: Shell not running. Tap 'Start' first."
            return
        }
        lines = lines + "$ $cmd"
        ShizukuShellManager.writeCommand(process!!, cmd)
        inputText = ""
        scope.launch {
            listState.animateScrollToItem(lines.size - 1)
        }
    }

    // Auto‑start on Shizuku ready
    LaunchedEffect(isShizukuReady) {
        if (isShizukuReady && process == null && !isRunning) {
            startShell()
        }
    }

    // Clean up
    DisposableEffect(Unit) {
        onDispose {
            try { process?.destroy() } catch (_: Exception) { }
        }
    }

    // ─── UI ──────────────────────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when {
                    isRunning -> "🟢 Shell Active"
                    isShizukuReady -> "⚪ Shizuku Ready"
                    else -> "🔴 Shizuku Not Ready"
                },
                color = when {
                    isRunning -> Color.Green
                    isShizukuReady -> Color.Yellow
                    else -> Color.Red
                },
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Row {
                Button(
                    onClick = { startShell() },
                    enabled = isShizukuReady && !isRunning,
                    modifier = Modifier.height(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Start Shell", fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    onClick = {
                        try {
                            process?.destroy()
                            process = null
                            isRunning = false
                            lines = lines + "Shell closed by user."
                        } catch (_: Exception) { }
                    },
                    enabled = isRunning,
                    modifier = Modifier.height(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("Close", fontSize = 10.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Terminal output
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(Color.Black)
                .fillMaxWidth(),
            state = listState,
            reverseLayout = false
        ) {
            items(lines) { line ->
                Text(
                    text = line,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Input row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Type command...", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF90CAF9),
                    unfocusedBorderColor = Color(0xFF444444),
                ),
                textStyle = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                ),
                onKeyEvent = { keyEvent ->
                    if (keyEvent.nativeKeyEvent?.action == KeyEvent.ACTION_DOWN &&
                        keyEvent.nativeKeyEvent?.keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (inputText.isNotBlank()) {
                            if (inputText.trim() == "exit") {
                                try {
                                    process?.destroy()
                                    process = null
                                    isRunning = false
                                    lines = lines + "Shell closed. Tap 'Start Shell' to reconnect."
                                } catch (_: Exception) { }
                                inputText = ""
                            } else {
                                executeCommand(inputText)
                            }
                        }
                        true
                    } else false
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        if (inputText.trim() == "exit") {
                            try {
                                process?.destroy()
                                process = null
                                isRunning = false
                                lines = lines + "Shell closed. Tap 'Start Shell' to reconnect."
                            } catch (_: Exception) { }
                            inputText = ""
                        } else {
                            executeCommand(inputText)
                        }
                    }
                },
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color(0xFF1976D2) else Color(0xFF444444)
                )
            ) {
                Text("⏎", fontSize = 18.sp)
            }
        }
    }
}

// Helper: MOTD
private fun getMotd(): List<String> {
    return listOf(
        "DroidUtility Console v1.0",
        "Persistent shell with Shizuku.",
        "Type 'exit' to close the shell.",
        "---"
    )
}

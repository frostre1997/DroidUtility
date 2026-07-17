package com.frostre1997.droidutility.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TerminalScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current

    var command by remember { mutableStateOf("") }
    var output by remember {
        mutableStateOf(
            """
            DroidUtility Terminal
            ---------------------
            Ready.
            """.trimIndent()
        )
    }
    var isRunning by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Terminal",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { output += "
[New tab placeholder]" }) {
                Icon(Icons.Default.Add, contentDescription = "New tab")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0F0F10)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF161618))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Windows Terminal style",
                        color = Color(0xFFDDDDDD),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        clipboard.setText(AnnotatedString(output))
                    }) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy output",
                            tint = Color(0xFFDDDDDD)
                        )
                    }
                    IconButton(onClick = { output = "" }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear output",
                            tint = Color(0xFFDDDDDD)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    SelectionContainer {
                        Text(
                            text = output,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            color = Color(0xFFD7D7D7)
                        )
                    }
                }

                Surface(
                    color = Color(0xFF161618),
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "❯",
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF7CFC98),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = command,
                            onValueChange = { command = it },
                            placeholder = {
                                Text("Enter command", fontFamily = FontFamily.Monospace)
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1B1B1D),
                                unfocusedContainerColor = Color(0xFF1B1B1D),
                                disabledContainerColor = Color(0xFF1B1B1D),
                                focusedTextColor = Color(0xFFD7D7D7),
                                unfocusedTextColor = Color(0xFFD7D7D7),
                                focusedBorderColor = Color(0xFF5AD56A),
                                unfocusedBorderColor = Color(0xFF303033)
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    isRunning = true
                                    val result = withContext(Dispatchers.IO) {
                                        ShizukuShellManager.executeCommand(command)
                                    }
                                    output += "
❯ $command
${result.displayText()}"
                                    isRunning = false
                                }
                            },
                            enabled = command.isNotBlank() && !isRunning,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

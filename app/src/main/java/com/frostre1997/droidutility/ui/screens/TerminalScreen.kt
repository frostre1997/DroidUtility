package com.frostre1997.droidutility.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frostre1997.droidutility.ShizukuShellManager
import com.frostre1997.droidutility.displayText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TerminalScreen(onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current

    var command by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }

    val outputLines = remember {
        mutableStateListOf(
            "DroidUtility Terminal",
            "---------------------",
            "Ready."
        )
    }

    fun addLine(line: String) {
        outputLines.add(line)
    }

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

            IconButton(onClick = { addLine("[New tab placeholder]") }) {
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
                        val copiedText = buildString {
                            outputLines.forEachIndexed { index, line ->
                                if (index > 0) appendLine()
                                append(line)
                            }
                        }
                        clipboard.setText(AnnotatedString(copiedText))
                    }) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy output",
                            tint = Color(0xFFDDDDDD)
                        )
                    }

                    IconButton(onClick = {
                        outputLines.clear()
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear output",
                            tint = Color(0xFFDDDDDD)
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(outputLines) { line ->
                        SelectionContainer {
                            Text(
                                text = line,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = Color(0xFFD7D7D7)
                            )
                        }
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

                                    outputLines.add("❯ $command")
                                    outputLines.addAll(result.displayText().lines())
                                    command = ""
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

package com.frostre1997.droidutility.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConsoleTab(onBack: () -> Unit) {
    var command by remember { mutableStateOf("pm list packages") }
    var output by remember { mutableStateOf(listOf("Ready")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Console", style = MaterialTheme.typography.headlineMedium)
        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Command: $command")
                Button(onClick = {
                    output = output + "Executed: $command"
                }) {
                    Text("Run")
                }
                Button(onClick = onBack) {
                    Text("Back")
                }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(output) { line ->
                Card { Text(line, modifier = Modifier.padding(12.dp)) }
            }
        }
    }
}

package com.frostre1997.droidutility.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeTab(onOpenConsole: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("DroidUtility", style = MaterialTheme.typography.headlineMedium)
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Debloat, shell commands, and utility tools in one app.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = onOpenConsole) {
                    Text("Open Console")
                }
            }
        }
    }
}

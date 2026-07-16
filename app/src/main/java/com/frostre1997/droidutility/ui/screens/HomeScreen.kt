package com.frostre1997.droidutility.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.frostre1997.droidutility.ui.components.InfoCard
import com.frostre1997.droidutility.ui.components.TipCard

@Composable
fun HomeScreen(
    onOpenTerminal: () -> Unit,
    onOpenDebloat: () -> Unit,
    onOpenConsole: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "DroidUtility",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "A powerful non-root utility suite for Android.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        TipCard(
            title = "Welcome",
            text = "Use the tools below to open Terminal, Debloat, Console, or Settings."
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            InfoCard(
                title = "Terminal",
                subtitle = "Run shell commands",
                icon = Icons.Default.Code,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenTerminal() }
            )
            InfoCard(
                title = "Debloat",
                subtitle = "Manage installed apps",
                icon = Icons.Default.Storage,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenDebloat() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            InfoCard(
                title = "Console",
                subtitle = "Advanced tools",
                icon = Icons.Default.Build,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenConsole() }
            )
            InfoCard(
                title = "Settings",
                subtitle = "Theme and app options",
                icon = Icons.Default.Settings,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenSettings() }
            )
        }
    }
}

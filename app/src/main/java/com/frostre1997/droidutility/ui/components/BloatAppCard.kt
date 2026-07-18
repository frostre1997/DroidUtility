package com.frostre1997.droidutility.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.RiskLevel

@Composable
fun BloatAppCard(
    app: BloatApp,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    imageVector = Icons.Default.Android,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = app.packageName,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (app.description.isNotBlank()) {
                Text(
                    text = app.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = true,
                    onClick = {},
                    label = { Text(app.category.name.replace('_', ' ')) }
                )

                AssistChip(
                    onClick = {},
                    label = { Text(app.risklevel.name.replace('_', ' ')) }
                )
            }

            app.alternatives?.takeIf { it.isNotEmpty() }?.let { alternatives ->
                Text(
                    text = "Alternatives: ${alternatives.joinToString(separator = ", ")}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

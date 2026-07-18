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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.RiskLevel

@Composable
fun BloatDetailCard(
    app: BloatApp?,
    onAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val current = app ?: return

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    imageVector = Icons.Default.Android,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = current.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = current.packageName,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Text(
                text = current.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = true,
                    onClick = {},
                    label = { Text(current.category.name.replace('_', ' ')) }
                )
                AssistChip(
                    onClick = onAction,
                    label = { Text(current.risklevel.name.replace('_', ' ')) }
                )
            }

            if (!current.alternatives.isNullOrEmpty()) {
                Text(
                    text = "Alternatives",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = current.alternatives.joinToString(separator = ", "),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

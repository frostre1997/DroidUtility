package com.frostre1997.droidutility.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frostre1997.droidutility.data.BloatApp

@Composable
fun BloatDetailCard(
    app: BloatApp?,
    onClick: () -> Unit = {}
) {
    if (app == null) return

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Icon(
                    imageVector = Icons.Default.Android,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(start = 12.dp))
                Column {
                    Text(
                        text = app.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = app.packageName,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = "Category: ${app.category}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (app.description.isNotBlank()) {
                Text(
                    text = app.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (app.isSystem) {
                FilterChip(
                    selected = true,
                    onClick = {},
                    label = { Text("System app") },
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

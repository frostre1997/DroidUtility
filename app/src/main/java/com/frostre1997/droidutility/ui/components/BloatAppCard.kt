package com.frostre1997.droidutility.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BloatAppCard(
    app: BloatApp,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(app.name, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            Text(app.packageName, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            Text(app.description, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(app.category.name) })
                AssistChip(onClick = {}, label = { Text(app.risk.name) })
            }
        }
    }
}

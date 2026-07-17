package com.frostre1997.droidutility.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BloatDetailCard(
    modifier: Modifier = Modifier,
    app: BloatApp?,
    onAction: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("App Details", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            if (app == null) {
                Text("Select an app")
                return
            }

            Text(app.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(app.packageName)
            Spacer(Modifier.height(4.dp))
            Text(app.description)
            Spacer(Modifier.height(12.dp))
            Text("Category: ${app.category.name}")
            Text("Risk: ${app.risk.name}")
            if (app.alternatives.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text("Alternatives: ${app.alternatives}")
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onAction,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DeleteSweep, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Disable / Uninstall")
            }
        }
    }
}

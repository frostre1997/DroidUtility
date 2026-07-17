package com.frostre1997.droidutility.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frostre1997.droidutility.BloatCategory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryChips(
    selectedCategory: BloatCategory?,
    onCategorySelected: (BloatCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AssistChip(
            onClick = { onCategorySelected(null) },
            label = { Text("All") },
            colors = if (selectedCategory == null) {
                AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            } else {
                AssistChipDefaults.assistChipColors()
            }
        )
        BloatCategory.entries.forEach { category ->
            AssistChip(
                onClick = { onCategorySelected(category) },
                label = { Text(formatCategoryName(category.name)) },
                colors = if (selectedCategory == category) {
                    AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                } else {
                    AssistChipDefaults.assistChipColors()
                }
            )
        }
    }
}

private fun formatCategoryName(name: String): String {
    return name.replace("_", " ").lowercase()
        .replaceFirstChar { it.uppercase() }
}

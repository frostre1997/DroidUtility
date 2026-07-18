package com.frostre1997.droidutility.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.ui.components.BloatAppCard
import com.frostre1997.droidutility.ui.components.BloatDetailCard
import com.frostre1997.droidutility.ui.components.BloatList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DebloatScreen(onBack: () -> Unit) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val isCompact = screenWidthDp < 600

    if (isCompact) {
        DebloatPhoneScreen(onBack = onBack)
    } else {
        DebloatTabletScreen(onBack = onBack)
    }
}

private data class UiState(
    val allApps: List<BloatApp> = emptyList(),
    val query: String = "",
    val category: BloatCategory? = null,
    val selected: BloatApp? = null,
    val loading: Boolean = true
)

@Composable
private fun DebloatPhoneScreen(onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(UiState()) }

    LaunchedEffect(Unit) {
        scope.launch {
            uiState = uiState.copy(loading = true)
            val apps = withContext(Dispatchers.Default) { BloatList.ALL }
            uiState = uiState.copy(
                allApps = apps,
                selected = uiState.selected ?: apps.firstOrNull(),
                loading = false
            )
        }
    }

    val filtered = remember(uiState.allApps, uiState.query, uiState.category) {
        uiState.allApps.filter { app ->
            val matchesQuery = uiState.query.isBlank() ||
                app.name.contains(uiState.query, ignoreCase = true) ||
                app.packageName.contains(uiState.query, ignoreCase = true) ||
                app.description.contains(uiState.query, ignoreCase = true)

            val matchesCategory = uiState.category == null || app.category == uiState.category
            matchesQuery && matchesCategory
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar(onBack = onBack, title = "Debloat Manager")

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            SearchAndFilters(
                query = uiState.query,
                onQueryChange = { uiState = uiState.copy(query = it) },
                category = uiState.category,
                onCategoryChange = { uiState = uiState.copy(category = it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { app ->
                    BloatAppCard(
                        app = app,
                        selected = uiState.selected?.packageName == app.packageName,
                        onClick = { uiState = uiState.copy(selected = app) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            uiState.selected?.let { app ->
                BloatDetailCard(
                    app = app,
                    onAction = { }
                )
            }
        }
    }
}

@Composable
private fun DebloatTabletScreen(onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(UiState()) }

    LaunchedEffect(Unit) {
        uiState = uiState.copy(loading = true)
        val apps = withContext(Dispatchers.Default) { BloatList.ALL }
        uiState = uiState.copy(
                allApps = apps,
                selected = apps.firstOrNull(),
                loading = false
            )
        }
    }

    val filtered = remember(uiState.allApps, uiState.query, uiState.category) {
        uiState.allApps.filter { app ->
            val matchesQuery = uiState.query.isBlank() ||
                app.name.contains(uiState.query, ignoreCase = true) ||
                app.packageName.contains(uiState.query, ignoreCase = true) ||
                app.description.contains(uiState.query, ignoreCase = true)

            val matchesCategory = uiState.category == null || app.category == uiState.category
            matchesQuery && matchesCategory
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopBar(onBack = onBack, title = "Debloat Manager")
        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.weight(0.44f).fillMaxHeight(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Text(
                            text = "Installed Bloat",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SearchAndFilters(
                            query = uiState.query,
                            onQueryChange = { uiState = uiState.copy(query = it) },
                            category = uiState.category,
                            onCategoryChange = { uiState = uiState.copy(category = it) }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(filtered) { app ->
                                BloatAppCard(
                                    app = app,
                                    selected = uiState.selected?.packageName == app.packageName,
                                    onClick = { uiState = uiState.copy(selected = app) }
                                )
                            }
                        }
                    }
                }

                BloatDetailCard(
                    modifier = Modifier.weight(0.56f).fillMaxHeight(),
                    app = uiState.selected,
                    onAction = { }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onBack: () -> Unit, title: String) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
private fun SearchAndFilters(
    query: String,
    onQueryChange: (String) -> Unit,
    category: BloatCategory?,
    onCategoryChange: (BloatCategory?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Search app, package, or description") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        val chips = listOf(
            null to "All",
            BloatCategory.GOOGLE to "Google",
            BloatCategory.HUAWEI to "Huawei",
            BloatCategory.LENOVO to "Lenovo",
            BloatCategory.MOTOROLA to "Motorola",
            BloatCategory.ONEPLUS to "OnePlus",
            BloatCategory.OPPO to "Oppo",
            BloatCategory.REALME to "Realme",
            BloatCategory.SAMSUNG to "Samsung",
            BloatCategory.XIAOMI to "Xiaomi",
            BloatCategory.OEM_BLOATWARE to "OEM",
            BloatCategory.CARRIER_APPS to "Carrier",
            BloatCategory.SOCIAL_MEDIA to "Social",
            BloatCategory.GAMES to "Games",
            BloatCategory.PRODUCTIVITY_BLOAT to "Productivity",
            BloatCategory.TRACKING_SPYWARE to "Tracking",
            BloatCategory.ADVERTISING to "Ads",
            BloatCategory.CLOUD_SERVICES to "Cloud",
            BloatCategory.REDUNDANT_APPS to "Duplicate",
            BloatCategory.PRIVACY_CONCERNING to "Privacy",
            BloatCategory.GENERAL to "General"
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(chips) { (cat, label) ->
                FilterChip(
                    selected = category == cat,
                    onClick = { onCategoryChange(cat) },
                    label = { Text(label) }
                )
            }
        }
    }
}

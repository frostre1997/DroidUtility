package com.frostre1997.droidutility.ui.screens

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.frostre1997.droidutility.ui.components.BloatAppCard
import com.frostre1997.droidutility.ui.components.BloatDetailCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DebloatScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val windowSizeClass = calculateWindowSizeClass(context as ComponentActivity)
    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    if (isCompact) {
        DebloatPhoneScreen(onBack)
    } else {
        DebloatTabletScreen(onBack)
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(UiState()) }

    fun loadData() {
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

    LaunchedEffect(Unit) { loadData() }

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

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TopBar(onBack = onBack, title = "Debloat Manager")

        Spacer(Modifier.height(12.dp))

        if (uiState.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else { 
             SearchAndFilters(
                 query = uiState.query,
                 onQueryChange = { uiState = uiState.copy(query = it) },
                 category = uiState.category,
                 onCategoryChange = { uiState = uiState.copy(category = it) }
        )

        Spacer(Modifier.height(12.dp))

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

        Spacer(Modifier.height(12.dp))

        uiState.selected?.let { app ->
            BloatDetailCard(
                app = app,
                onAction = { }
            )
        }
    }
}

@Composable
private fun DebloatTabletScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(UiState()) }

    fun loadData() {
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

    LaunchedEffect(Unit) { loadData() }

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

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TopBar(onBack = onBack, title = "Debloat Manager")
        Spacer(Modifier.height(12.dp))

        if (uiState.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                   Column(Modifier.fillMaxSize().padding(16.dp)) {
                       Text(
                           "Installed Bloat", 
                           style = MaterialTheme.typography.titleLarge, 
                           fontWeight = FontWeight.Bold
                           
                        )
                        Spacer(Modifier.height(12.dp))

                        SearchAndFilters(
                            query = uiState.query,
                            onQueryChange = { uiState = uiState.copy(query = it) },
                            category = uiState.category,
                            onCategoryChange = { uiState = uiState.copy(category = it) }
                    )

                    Spacer(Modifier.height(12.dp))

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
                onAction = { /* hook Shizuku/PM disable action here */ }
            )
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    }
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
            BloatCategory.OEM_BLOATWARE to "OEM",
            BloatCategory.CARRIER_APPS to "Carrier",
            BloatCategory.SOCIAL_MEDIA to "Social",
            BloatCategory.GAMES to "Games",
            BloatCategory.PRODUCTIVITY_BLOAT to "Productivity",
            BloatCategory.TRACKING_SPYWARE to "Tracking",
            BloatCategory.ADVERTISING to "Ads",
            BloatCategory.CLOUD_SERVICES to "Cloud",
            BloatCategory.REDUNDANT_APPS to "Duplicate",
            BloatCategory.PRIVACY_CONCERNING to "Privacy"
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(chips) { item ->
                val selected = category == item.first
                FilterChip(
                    selected = selected,
                    onClick = { onCategoryChange(item.first) },
                    label = { Text(item.second) }
                )
            }
        }
    }
}

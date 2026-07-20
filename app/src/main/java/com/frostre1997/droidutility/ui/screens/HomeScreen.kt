package com.frostre1997.droidutility.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "DroidUtility – your non‑root tool suite",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Two stat cards using weight inside Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.weight(1f),
                color = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Core", color = Color.Gray, fontSize = 14.sp)
                    Text("Stopped", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
            Surface(
                modifier = Modifier.weight(1f),
                color = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total Workflows", color = Color.Gray, fontSize = 14.sp)
                    Text("2", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Permission health card
        Surface(
            color = Color(0xFF1A1A1A),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Permission Health Check",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Status: 2 missing permissions",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recent logs section
        Text(
            text = "Recent Logs",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(3) { index ->
                LogItem(
                    title = if (index == 0) "宏胖胖签到" else if (index == 1) "宏胖胖签到" else "Hello vFlow",
                    date = if (index == 0) "Jul 10, 2026" else if (index == 1) "Jun 29, 2026" else "Jun 28, 2026",
                    status = if (index == 0) "Failed at step #1" else if (index == 1) "Failed at step #1" else "Execution completed"
                )
            }
        }
    }
}

@Composable
fun LogItem(title: String, date: String, status: String) {
    Surface(
        color = Color(0xFF1A1A1A),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.Medium)
                Text(status, color = Color.LightGray, fontSize = 12.sp)
            }
            Text(date, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

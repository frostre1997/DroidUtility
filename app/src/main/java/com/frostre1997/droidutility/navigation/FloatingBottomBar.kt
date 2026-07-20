package com.frostre1997.droidutility.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frostre1997.droidutility.Screen

@Composable
fun FloatingBottomBar(
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    val screens = listOf(
        Screen.Home,
        Screen.Terminal,
        Screen.Debloat,
        Screen.Shell,
        Screen.Settings
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .padding(horizontal = 16.dp),
        color = Color.Black.copy(alpha = 0.7f), // semi‑transparent black for glass effect
        shape = RoundedCornerShape(34.dp),
        shadowElevation = 8.dp, // soft shadow instead of blur
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                val selected = currentRoute == screen.route
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onItemClick(screen.route) }
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        screen.icon,
                        contentDescription = screen.title,
                        tint = if (selected) Color.White else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        screen.title,
                        fontSize = 10.sp,
                        color = if (selected) Color.White else Color.Gray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

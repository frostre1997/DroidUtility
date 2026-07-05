package com.frostre1997.droidutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CommandDashboard()
                }
            }
        }
    }
}

@Composable
fun CommandDashboard() {
    var command by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("Output will be displayed here...") }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = command,
            onValueChange = { command = it },
            label = { Text("Insert command (es. pm list packages)") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(onClick = {
            scope.launch {
                val result = withContext(Dispatchers.IO) {
                    ShizukuShellManager.executeCommand(command)
                }
                output = result
            }
        }) {
            Text("Run Command")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = output, modifier = Modifier.fillVertical())
    }
}

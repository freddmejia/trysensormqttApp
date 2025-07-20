package com.example.trysensormqtt.ui.dialogs

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.trysensormqtt.R
import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.data.local.entity.SensorTypeEntity

@Composable
fun AppInfoDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "How the App Works",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    IconButton(
                        onClick = onDismiss,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close info",
                            tint = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                //                        • All sensor data is stored locally on your device.
                Text(
                    text = """
                       • From Configuration you can *create*, *edit* or *delete* sensors.  
                        • On the Sensor screen the app starts an MQTT listener in the background  
                          and subscribes to every configured topic to receive live values.  
                        
                        🔹 How to fill in Host / IP  
                            – Enter only the domain or IP (e.g. `broker.hivemq.com` or `11.111.11.11`).  
                            – Do not prepend `tcp://`, `ssl://`, `ws://`, … — the app adds the
                              proper prefix automatically when building the broker URI (`tcp://<host>:<port>`).  
                            – If the port is left blank, 1883 (plain MQTT) is used by default.
                        
                        🔹 Expected JSON payload for each MQTT message
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF5F5F5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
                        {
                            "data": "value"
                        }
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF3E3E3E)
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("OK")
                }
            }
        }
    }
}

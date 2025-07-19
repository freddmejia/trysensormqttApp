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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.trysensormqtt.R
import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.data.local.entity.SensorTypeEntity

@Composable
fun AddSensorDialog(
    sensor: SensorEntity? = null,
    availableTypes: List<SensorTypeEntity>,
    onDismiss: () -> Unit,
    onSave: (isHost: Boolean, host: String, port: String, user: String?, password: String?, topic: String, name: String, typeId: Long) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var isHostSelected by remember { mutableStateOf(true) }

    var host by remember { mutableStateOf(sensor?.host ?: "") }
    var port by remember { mutableStateOf(sensor?.port?.toString() ?: "") }
    var user by remember { mutableStateOf(sensor?.user ?: "") }
    var password by remember { mutableStateOf(sensor?.password ?: "") }
    var topic by remember { mutableStateOf(sensor?.topic ?: "") }
    var ipParts by remember { mutableStateOf(listOf("", "", "", "")) }

    var sensorName by remember { mutableStateOf(sensor?.name ?: "") }
    var selectedType by remember {
        mutableStateOf(
            availableTypes.find { it.id == sensor?.sensorTypeId }
        )
    }

    var showError by remember { mutableStateOf(false) }

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Add Sensor",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                        )
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Red
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TabButton("Configuration", selectedTab == 0) { selectedTab = 0 }
                    TabButton("Chart", selectedTab == 1) { selectedTab = 1 }
                }

                if (selectedTab == 0) {
                    HostIpSelector(
                        isHostSelected = isHostSelected,
                        onHostSelected = { isHostSelected = true },
                        onIpSelected = { isHostSelected = false },
                        hostValid = null
                    )

                    HostIpInputSection(
                        isHostSelected = isHostSelected,
                        host = host,
                        onHostChange = { host = it },
                        ipParts = ipParts,
                        onIpPartChange = { index, value ->
                            ipParts = ipParts.toMutableList().also { it[index] = value }
                            host = ipParts.joinToString(".")
                        }
                    )

                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = port,
                        onValueChange = { port = it },
                        label = { Text("Port") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = user,
                        onValueChange = { user = it },
                        label = { Text("User (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Topic") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    ChartSection(
                        sensorName = sensorName,
                        onNameChange = { sensorName = it },
                        selectedType = selectedType,
                        onTypeSelected = { selectedType = it },
                        availableTypes = availableTypes
                    )
                }

                if (showError) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Please fill all required fields.",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (host.isEmpty() || topic.isEmpty() || sensorName.isEmpty() || selectedType == null) {
                            showError = true
                        } else {
                            onSave(
                                isHostSelected,
                                host,
                                port,
                                user.ifEmpty { null },
                                password.ifEmpty { null },
                                topic,
                                sensorName,
                                selectedType!!.id
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(if (sensor == null) "CREATE" else "UPDATE")
                }
            }
        }
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF004D40) else Color.LightGray,
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .height(36.dp)
            .padding(horizontal = 4.dp)
    ) {
        Text(text)
    }
}


@Composable
fun ChartPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Chart Preview")
    }
}

@Composable
fun ToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF4CAF50) else Color.LightGray,
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .height(36.dp)
            .padding(horizontal = 4.dp)
    ) {
        Text(text)
    }
}

@Composable
fun HostIpSelector(
    isHostSelected: Boolean,
    onHostSelected: () -> Unit,
    onIpSelected: () -> Unit,
    hostValid: Boolean?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HostIpButton(
            text = "Host",
            isSelected = isHostSelected,
            onClick = onHostSelected
        )
        Spacer(modifier = Modifier.width(8.dp))

        HostIpButton(
            text = "IP",
            isSelected = !isHostSelected,
            onClick = onIpSelected
        )

        Spacer(modifier = Modifier.width(8.dp))

        when (hostValid) {
            true -> Icon(
                painter = painterResource(R.drawable.check_icon),
                contentDescription = "Valid",
                tint = Color.Green
            )
            false -> Icon(
                painter = painterResource(R.drawable.error_icon),
                contentDescription = "Error",
                tint = Color.Red
            )
            null -> {}
        }
    }
}

@Composable
fun HostIpInputSection(
    isHostSelected: Boolean,
    host: String,
    onHostChange: (String) -> Unit,
    ipParts: List<String>,
    onIpPartChange: (Int, String) -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    if (isHostSelected) {
        OutlinedTextField(
            value = host,
            onValueChange = onHostChange,
            placeholder = { Text("mqtt.myserver") },
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ipParts.forEachIndexed { index, part ->
                OutlinedTextField(
                    value = part,
                    onValueChange = { onIpPartChange(index, it) },
                    placeholder = { Text("0") },
                    singleLine = true,
                    modifier = Modifier.width(60.dp)
                )
            }
        }
    }
}


/*
@Composable
fun HostIpSelector(
    isHostSelected: Boolean,
    onHostSelected: () -> Unit,
    onIpSelected: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HostIpButton(
            text = "Host",
            isSelected = isHostSelected,
            onClick = onHostSelected
        )
        Spacer(modifier = Modifier.width(8.dp))
        HostIpButton(
            text = "IP",
            isSelected = !isHostSelected,
            onClick = onIpSelected
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(R.drawable.check_icon),
            contentDescription = "Valid",
            tint = Color.Green
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(R.drawable.error_icon),
            contentDescription = "Error",
            tint = Color.Red
        )
    }
}
@Composable
fun HostIpInputSection(isHostSelected: Boolean) {
    Spacer(modifier = Modifier.height(16.dp))
    if (isHostSelected) {
        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("mqtt.myserver") },
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(4) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = { Text(".") },
                    modifier = Modifier.width(60.dp)
                )
            }
        }
    }
}
*/
@Composable
fun HostIpButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFF7B7B3F) else Color.White
    val textColor = if (isSelected) Color.White else Color.Black
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(50),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Text(text)
    }
}



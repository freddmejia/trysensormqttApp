package com.example.trysensormqtt.ui.dialogs

import android.R.attr.name
import androidx.compose.runtime.Composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.domain.result.SensorResult
import com.example.trysensormqtt.ui.viewmodel.SensorViewModel
import com.example.trysensormqtt.R
import com.example.trysensormqtt.data.local.entity.SensorTypeEntity
import com.example.trysensormqtt.ui.viewmodel.SensorTypeViewModel


@Composable
fun ChartSection(
    sensorName: String,
    onNameChange: (String) -> Unit,
    selectedType: SensorTypeEntity?,
    onTypeSelected: (SensorTypeEntity) -> Unit,
    sensorTypeViewModel: SensorTypeViewModel = hiltViewModel(),
    availableTypes: List<SensorTypeEntity>
) {
    val sensorTypesResult by sensorTypeViewModel.types.observeAsState(SensorResult.Success(emptyList()))
    val types = (sensorTypesResult as SensorResult.Success).data

    LaunchedEffect(Unit) {
        sensorTypeViewModel.fetchAllTypes()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = sensorName,
            onValueChange = onNameChange,
            label = { Text("Sensor Name") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (availableTypes.isNotEmpty()) {
            SensorTypeDropdown(
                types = availableTypes,
                selectedType = selectedType,
                onTypeSelected = onTypeSelected
            )
        } else {
            Text("No sensor types available.", color = Color.Red)
        }

        /*when (sensorTypesResult) {
            is SensorResult.Success -> {
                val types = (sensorTypesResult as SensorResult.Success).data
                if (types.isNotEmpty()) {

                    SensorTypeDropdown(
                        types = types,
                        selectedType = selectedType,
                        onTypeSelected = onTypeSelected
                    )
                } else {
                    Text(
                        text = "No sensor types found. Please insert initial data.",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            else -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }*/

        Spacer(Modifier.height(16.dp))

        selectedType?.let {
            when (it.name.lowercase()) {
                "temperature" -> SensorPreview(
                    name = "Temperature Sensor",
                    value = "38°C",
                    imageRes = R.drawable.temperature_icon
                )
                "humidity" -> SensorPreview(
                    name = "Humidity Sensor",
                    value = "38%",
                    imageRes = R.drawable.humidity_icon
                )
                "water" -> SensorPreview(
                    name = "Water Sensor",
                    value = "2L",
                    imageRes = R.drawable.water_icon
                )
                "pressure" -> SensorPreview(
                    name = "Pressure Sensor",
                    value = "1.2bar",
                    imageRes = R.drawable.pressure_icon
                )
                else -> SensorPreview(
                    name = "Data Sensor",
                    value = "--",
                    imageRes = R.drawable.data_icon
                )
            }
        }
    }
}

@Composable
fun SensorTypeItem(
    typeName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF4CAF50) else Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = typeName,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}


/*
@Composable
fun ChartSection(
    sensorTypeViewModel: SensorTypeViewModel = hiltViewModel()
) {
    val sensorTypesResult by sensorTypeViewModel.types.observeAsState(SensorResult.Success(emptyList()))
    var selectedType by remember { mutableStateOf<SensorTypeEntity?>(null) }
    var sensorName by remember { mutableStateOf("My Sensor") }

    LaunchedEffect(Unit) {
        sensorTypeViewModel.fetchAllTypes()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (sensorTypesResult) {
                is SensorResult.Success -> {
                    val types = (sensorTypesResult as SensorResult.Success).data

                    if (types.isNotEmpty()) {
                        SensorTypeDropdown(
                            types = types,
                            selectedType = selectedType,
                            onTypeSelected = { selectedType = it }
                        )
                    } else {
                        Text("No sensor types available.")
                    }

                }

                else -> Text("Loading sensor types...")
            }
            OutlinedTextField(
                value = sensorName,
                onValueChange = { sensorName = it },
                label = { Text("Sensor Name") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }


        Spacer(Modifier.height(16.dp))

        selectedType?.let {
            when (it.name.lowercase()) {
                "temperature" -> SensorPreview(
                    name = "Temperature Sensor",
                    value = "38°C",
                    imageRes = R.drawable.temperature_icon
                )
                "humidity" -> SensorPreview(
                    name = "Humidity Sensor",
                    value = "38%",
                    imageRes = R.drawable.humidity_icon
                )
                "water" -> SensorPreview(
                    name = "Water Sensor",
                    value = "2L",
                    imageRes = R.drawable.water_icon
                )
                "pressure" -> SensorPreview(
                    name = "Pressure Sensor",
                    value = "1.2bar",
                    imageRes = R.drawable.pressure_icon
                )
                else -> SensorPreview(
                    name = "Data Sensor",
                    value = "--",
                    imageRes = R.drawable.data_icon
                )
            }
        }
    }
} */

@Composable
fun SensorTypeDropdown(
    types: List<SensorTypeEntity>,
    selectedType: SensorTypeEntity?,
    onTypeSelected: (SensorTypeEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedType?.name ?: "Select Type")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}



@Composable
fun SensorPreview(
    name: String,
    value: String,
    imageRes: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Imagen a la izquierda y valor a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = name,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

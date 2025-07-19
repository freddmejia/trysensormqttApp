package com.example.trysensormqtt.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.domain.result.SensorResult
import com.example.trysensormqtt.domain.result.SensorUIState
import com.example.trysensormqtt.ui.dialogs.AddSensorDialog
import com.example.trysensormqtt.ui.viewmodel.SensorViewModel
import com.example.trysensormqtt.R
import com.example.trysensormqtt.ui.viewmodel.SensorTypeViewModel

@Composable
fun SensorScreen(
    viewModel: SensorViewModel = hiltViewModel(),
    sensorTypeViewModel: SensorTypeViewModel = hiltViewModel()
) {
    val sensorsResult by viewModel.sensors.observeAsState(SensorResult.Success(emptyList()))
    val sensorTypesResult by sensorTypeViewModel.types.observeAsState(SensorResult.Success(emptyList()))
    var showAddSensorDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val uiEvent = viewModel.uiEvent.collectAsState(initial = SensorUIState.Idle)

    LaunchedEffect(uiEvent.value) {
        when (uiEvent.value) {
            is SensorUIState.Success -> viewModel.fetchAllSensors()
            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAllSensors()
        sensorTypeViewModel.fetchAllTypes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        when (sensorsResult) {
            is SensorResult.Success -> {
                val sensors = (sensorsResult as SensorResult.Success).data

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sensors.size) { index ->
                        SensorCard(sensor = sensors[index])
                    }
                    item {
                        DefaultAddSensorCard(onAddClick = { showAddSensorDialog = true })
                    }
                }
            }
            else -> Text("Loading sensors...")
        }
    }

    if (showAddSensorDialog) {
        val sensorTypes = (sensorTypesResult as? SensorResult.Success)?.data ?: emptyList()

        AddSensorDialog(
            availableTypes = sensorTypes,
            onDismiss = { showAddSensorDialog = false },
            onSave = { isHost, host, port, user, password, topic, name, typeId ->
                viewModel.createSensor(
                    name = name,
                    host = host,
                    port = port,
                    user = user,
                    password = password,
                    topic = topic,
                    typeId = typeId
                )
                showAddSensorDialog = false
            }
        )
    }
}
@Composable
fun DefaultAddSensorCard(
    modifier: Modifier = Modifier,
    value: Int = 50,
    onAddClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "My sensor",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            GaugeIndicator(value = value)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(0.6f)
            ) {
                Text("ADD", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun GaugeIndicator(value: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(70.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color.LightGray,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )

            drawArc(
                color = Color.Gray,
                startAngle = 180f,
                sweepAngle = (value / 100f) * 180f,
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
        }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
    }
}


@Composable
fun SensorCard(
    sensor: SensorEntity,
    value: String? = null
) {
    val displayValue = value ?: "50"


    // Determinar imagen y nombre según el tipo de sensor
    val (title, imageRes) = when (sensor.sensorTypeId.toInt()) {
        1 -> sensor.name to R.drawable.temperature_icon
        2 -> sensor.name to R.drawable.humidity_icon
        3 -> sensor.name to R.drawable.water_icon
        4 -> sensor.name to R.drawable.pressure_icon
        else -> sensor.name to R.drawable.data_icon
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre del sensor
            Text(
                text = sensor.name.ifEmpty { title },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Imagen y valor
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = displayValue,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }


        }
    }
}

package fredd.dev.trysensormqtt.ui.menu

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
import fredd.dev.trysensormqtt.data.local.entity.SensorEntity
import fredd.dev.trysensormqtt.domain.result.SensorResult
import fredd.dev.trysensormqtt.domain.result.SensorUIState
import fredd.dev.trysensormqtt.ui.dialogs.AddSensorDialog
import fredd.dev.trysensormqtt.ui.viewmodel.SensorViewModel
import fredd.dev.trysensormqtt.R
import fredd.dev.trysensormqtt.domain.repository.MqttState
import fredd.dev.trysensormqtt.ui.viewmodel.MqttViewModel
import fredd.dev.trysensormqtt.ui.viewmodel.SensorTypeViewModel

@Composable
fun SensorScreen(
    viewModel: SensorViewModel = hiltViewModel(),
    sensorTypeViewModel: SensorTypeViewModel = hiltViewModel(),
    mqttViewModel:  MqttViewModel   = hiltViewModel()
    ) {
    val sensorsResult by viewModel.sensors.observeAsState(SensorResult.Success(emptyList()))
    val sensorTypesResult by sensorTypeViewModel.types.observeAsState(SensorResult.Success(emptyList()))
    var showAddSensorDialog by remember { mutableStateOf(false) }
    val uiEvent = viewModel.uiEvent.collectAsState(initial = SensorUIState.Idle)

    val sensorData = remember { mutableStateMapOf<Long, String>() }

    val incoming by mqttViewModel.messages.collectAsState()
    val incomingState by mqttViewModel.states.collectAsState()
    val sensorStates = remember { mutableStateMapOf<Long, MqttState>() }


    LaunchedEffect(incomingState) {
        incomingState?.let { (id, st) -> sensorStates[id] = st }
    }

    LaunchedEffect(incoming) {
        incoming?.let { (id, payload) ->
            sensorData[id] = payload
        }
    }

    LaunchedEffect(sensorsResult) {
        if (sensorsResult is SensorResult.Success) {
            val sensors = (sensorsResult as SensorResult.Success).data
            sensors.forEach { mqttViewModel.startSensor(it) }

            val liveIds = sensors.map { it.id }.toSet()

            (sensorData.keys + sensorStates.keys)
                .filter { it !in liveIds }
                .forEach { id ->
                    mqttViewModel.stopSensor(id)
                    sensorData.remove(id)
                    sensorStates.remove(id)
                }
        }
    }

    DisposableEffect(Unit) {
        onDispose { mqttViewModel.stopAll() }
    }

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
                        val sensor = sensors[index]
                        val value  = sensorData[sensor.id]
                        val state  = sensorStates[sensor.id]

                        SensorCard(sensor = sensor, value = value, state = state)
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
    value: String? = null,
    state: MqttState? = null
) {
    val displayValue = value ?: "--"

    val (title, imageRes) = when (sensor.sensorTypeId.toInt()) {
        1 -> sensor.name to R.drawable.temperature_icon
        2 -> sensor.name to R.drawable.humidity_icon
        3 -> sensor.name to R.drawable.water_icon
        4 -> sensor.name to R.drawable.pressure_icon
        else -> sensor.name to R.drawable.data_icon
    }

    val iconRes = when (state) {
        MqttState.Connected      -> R.drawable.connected_icon
        MqttState.Connecting     -> R.drawable.connecting_icon
        MqttState.ConnectionLost     -> R.drawable.lost_connection_icon
        else                     -> R.drawable.error_icon
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sensor.name.ifEmpty { title },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color       = Color.Gray
                    ),
                    modifier = Modifier.weight(1f),
                    maxLines = 2
                )

                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = state?.name ?: "unknown",
                    modifier = Modifier.size(20.dp)
                )
            }


            Spacer(modifier = Modifier.height(12.dp))

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
                    ),
                    maxLines = 2
                )
            }


        }
    }
}


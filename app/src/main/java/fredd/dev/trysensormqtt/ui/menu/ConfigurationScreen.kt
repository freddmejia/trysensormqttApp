package fredd.dev.trysensormqtt.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fredd.dev.trysensormqtt.data.local.entity.SensorEntity
import fredd.dev.trysensormqtt.domain.result.SensorResult
import fredd.dev.trysensormqtt.ui.viewmodel.SensorViewModel
import fredd.dev.trysensormqtt.R
import fredd.dev.trysensormqtt.ui.dialogs.AddSensorDialog
import fredd.dev.trysensormqtt.ui.viewmodel.SensorTypeViewModel

@Composable
fun ConfigurationScreen(
    viewModel: SensorViewModel = hiltViewModel(),
    sensorTypeViewModel: SensorTypeViewModel = hiltViewModel()

) {
    val sensorsResult by viewModel.sensors.observeAsState(SensorResult.Success(emptyList()))
    var showDeleteDialog by remember { mutableStateOf(false) }
    var sensorToDelete by remember { mutableStateOf<Long?>(null) }
    var sensorToEdit by remember { mutableStateOf<SensorEntity?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    val sensorTypesResult by sensorTypeViewModel.types.observeAsState(SensorResult.Success(emptyList()))

    LaunchedEffect(Unit) {
        viewModel.fetchAllSensors()
        sensorTypeViewModel.fetchAllTypes()
    }

    when (sensorsResult) {
        is SensorResult.Success -> {
            val sensors = (sensorsResult as SensorResult.Success).data
            if (sensors.isEmpty()) {
                EmptyConfigurationView()
            } else {
                SensorListConfiguration(
                    sensors = sensors,
                    onDelete = { sensor ->
                        sensorToDelete = sensor.id
                        showDeleteDialog = true
                    },
                    onEdit = { sensor ->
                        sensorToEdit = sensor
                        showEditDialog = true
                    }
                )
            }
        }

        else -> Text("Loading...")
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Sensor") },
            text = { Text("Are you sure you want to delete this sensor?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        sensorToDelete?.let { id ->
                            viewModel.deleteSensor(id)
                        }
                        showDeleteDialog = false
                    }
                ) {
                    Text("OK", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showEditDialog && sensorToEdit != null) {
        val sensorTypes = (sensorTypesResult as? SensorResult.Success)?.data ?: emptyList()

        AddSensorDialog(
            sensor = sensorToEdit,
            availableTypes  = sensorTypes,
            onDismiss = { showEditDialog = false },
            onSave = { isHost, host, port, user, password, topic, name, typeId ->
                val updatedSensor = sensorToEdit!!.copy(
                    name = name,
                    host = host,
                    port = port.toIntOrNull() ?: 1883,
                    user = user,
                    password = password,
                    topic = topic,
                    sensorTypeId = typeId
                )
                viewModel.updateSensor(updatedSensor)
                sensorToEdit = null
                showEditDialog = false
            },

        )
    }

}

@Composable
fun EmptyConfigurationView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_data_placeholder),
            contentDescription = "Empty sensors",
            modifier = Modifier
                .size(350.dp)
                .padding(bottom = 24.dp)
        )
        Text("You have no sensors added.")
    }
}

@Composable
fun SensorListConfiguration(
    sensors: List<SensorEntity>,
    onDelete: (SensorEntity) -> Unit,
    onEdit: (SensorEntity) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(sensors) { sensor ->
            SensorConfigItem(
                sensor,
                onDelete = { onDelete(sensor) },
                onEdit = { onEdit(sensor) }
            )
        }
    }
}

@Composable
fun SensorConfigItem(
    sensor: SensorEntity,
    onDelete: () -> Unit,
    onEdit: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = sensor.name,
                    style = MaterialTheme.typography.bodyLarge
                )

            }
            Row {
                IconButton(
                    onClick = onEdit,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(0xFF4C729D)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Edit Sensor",

                    )
                }
                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(0xFFBA1717)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Sensor"
                    )
                }
            }
        }
    }
}

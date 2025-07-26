package fredd.dev.trysensormqtt.data.remote.implementation

import android.content.Context
import android.util.Log
import fredd.dev.trysensormqtt.data.local.entity.SensorEntity
import fredd.dev.trysensormqtt.domain.repository.MqttState
import fredd.dev.trysensormqtt.domain.repository.SensorMqttRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MqttRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SensorMqttRepository {

    private val managers      = mutableMapOf<Long, MqttSensorManager>()

    private val _sensorMsgs   = MutableSharedFlow<Pair<Long, String>>(extraBufferCapacity = 64)
    private val _sensorStates = MutableSharedFlow<Pair<Long, MqttState>>(extraBufferCapacity = 64)

    override val sensorMessages = _sensorMsgs.asSharedFlow()
    override val sensorStates   = _sensorStates.asSharedFlow()
    private var tried = false
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun startSensor(sensor: SensorEntity) {
        if (managers.containsKey(sensor.id)) return

        val mgr = MqttSensorManager(
            host = sensor.host,
            port = sensor.port,
            topic = sensor.topic,
            user = sensor.user,
            pwd  = sensor.password,
            onMessage = { _sensorMsgs.tryEmit(sensor.id to it) },
            onState   = { state, err ->
                _sensorStates.tryEmit(sensor.id to state)
                err?.let { Log.e("MQTTDDDD", "[$state] $it") }
            }
        )
        managers[sensor.id] = mgr
        scope.launch {
            mgr.start()
        }
    }

    override fun stopSensor(sensorId: Long) {
        managers.remove(sensorId)?.let { mgr ->
            scope.launch { mgr.stop() }
        }
    }

    override fun stopAll() {
        scope.launch {
            managers.values.forEach { it.stop() }
            managers.clear()
        }
    }
}
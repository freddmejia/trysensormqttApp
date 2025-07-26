package fredd.dev.trysensormqtt.domain.use_case

import fredd.dev.trysensormqtt.data.local.entity.SensorEntity
import fredd.dev.trysensormqtt.domain.repository.SensorRepository
import fredd.dev.trysensormqtt.domain.result.SensorResult
import javax.inject.Inject

class UpdateSensorUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend fun execute(sensor: SensorEntity): SensorResult<SensorEntity> {
        return repository.updateSensor(sensor)
    }
}

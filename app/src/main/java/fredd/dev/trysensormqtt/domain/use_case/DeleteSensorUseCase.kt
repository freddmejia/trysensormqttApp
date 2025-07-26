package fredd.dev.trysensormqtt.domain.use_case

import fredd.dev.trysensormqtt.domain.repository.SensorRepository
import fredd.dev.trysensormqtt.domain.result.SensorResult
import javax.inject.Inject

class DeleteSensorUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend fun execute(sensorId: Long): SensorResult<Boolean> {
        return repository.deleteSensor(sensorId)
    }
}

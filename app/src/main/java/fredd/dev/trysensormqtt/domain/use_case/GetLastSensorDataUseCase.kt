package fredd.dev.trysensormqtt.domain.use_case
import javax.inject.Inject
import fredd.dev.trysensormqtt.data.local.entity.SensorDataEntity
import fredd.dev.trysensormqtt.domain.repository.SensorDataRepository
import fredd.dev.trysensormqtt.domain.result.SensorResult

class GetLastSensorDataUseCase @Inject constructor(
    private val repository: SensorDataRepository
) {
    suspend fun execute(sensorId: Int): SensorResult<SensorDataEntity> {
        return repository.getLastData(sensorId)
    }
}

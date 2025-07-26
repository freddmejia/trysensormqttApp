package fredd.dev.trysensormqtt.domain.use_case
import javax.inject.Inject
import fredd.dev.trysensormqtt.data.local.entity.SensorDataEntity
import fredd.dev.trysensormqtt.domain.repository.SensorDataRepository
import fredd.dev.trysensormqtt.domain.result.SensorResult

class GetSensorDataHistoryUseCase @Inject constructor(
    private val repository: SensorDataRepository
) {
    suspend fun execute(sensorId: Int, limit: Int, offset: Int): SensorResult<List<SensorDataEntity>> {
        return repository.getDataHistory(sensorId, limit, offset)
    }
}

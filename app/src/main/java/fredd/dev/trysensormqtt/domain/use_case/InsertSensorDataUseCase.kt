package fredd.dev.trysensormqtt.domain.use_case
import javax.inject.Inject
import fredd.dev.trysensormqtt.data.local.entity.SensorDataEntity
import fredd.dev.trysensormqtt.domain.repository.SensorDataRepository
import fredd.dev.trysensormqtt.domain.result.SensorResult

class InsertSensorDataUseCase @Inject constructor(
    private val repository: SensorDataRepository
) {
    suspend fun execute(data: SensorDataEntity): SensorResult<SensorDataEntity> {
        return repository.insertData(data)
    }
}

package fredd.dev.trysensormqtt.domain.use_case
import javax.inject.Inject
import fredd.dev.trysensormqtt.data.local.entity.SensorTypeEntity
import fredd.dev.trysensormqtt.domain.repository.SensorTypeRepository
import fredd.dev.trysensormqtt.domain.result.SensorResult

class GetAllSensorTypesUseCase @Inject constructor(
    private val repository: SensorTypeRepository
) {
    suspend fun execute(): SensorResult<List<SensorTypeEntity>> {
        return repository.fetchAllTypes()
    }
}

package fredd.dev.trysensormqtt.domain.use_case

import fredd.dev.trysensormqtt.data.local.entity.SensorEntity
import fredd.dev.trysensormqtt.domain.repository.SensorRepository
import fredd.dev.trysensormqtt.domain.result.SensorResult
import javax.inject.Inject

class GetAllSensorsUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend fun execute(limit: Int, offset: Int): SensorResult<List<SensorEntity>> {
        return repository.fetchAllSensors(limit = limit, offset = offset)
    }
}

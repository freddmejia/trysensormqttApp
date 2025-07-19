package com.example.trysensormqtt.domain.use_case
import javax.inject.Inject
import com.example.trysensormqtt.data.local.entity.SensorTypeEntity
import com.example.trysensormqtt.domain.repository.SensorTypeRepository
import com.example.trysensormqtt.domain.result.SensorResult

class GetAllSensorTypesUseCase @Inject constructor(
    private val repository: SensorTypeRepository
) {
    suspend fun execute(): SensorResult<List<SensorTypeEntity>> {
        return repository.fetchAllTypes()
    }
}

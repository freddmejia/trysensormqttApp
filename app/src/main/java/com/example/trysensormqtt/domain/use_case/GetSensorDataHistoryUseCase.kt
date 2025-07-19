package com.example.trysensormqtt.domain.use_case
import javax.inject.Inject
import com.example.trysensormqtt.data.local.entity.SensorDataEntity
import com.example.trysensormqtt.domain.repository.SensorDataRepository
import com.example.trysensormqtt.domain.result.SensorResult

class GetSensorDataHistoryUseCase @Inject constructor(
    private val repository: SensorDataRepository
) {
    suspend fun execute(sensorId: Int, limit: Int, offset: Int): SensorResult<List<SensorDataEntity>> {
        return repository.getDataHistory(sensorId, limit, offset)
    }
}

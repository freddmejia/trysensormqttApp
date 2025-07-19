package com.example.trysensormqtt.domain.use_case
import javax.inject.Inject
import com.example.trysensormqtt.data.local.entity.SensorDataEntity
import com.example.trysensormqtt.domain.repository.SensorDataRepository
import com.example.trysensormqtt.domain.result.SensorResult

class GetLastSensorDataUseCase @Inject constructor(
    private val repository: SensorDataRepository
) {
    suspend fun execute(sensorId: Int): SensorResult<SensorDataEntity> {
        return repository.getLastData(sensorId)
    }
}

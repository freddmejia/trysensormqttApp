package com.example.trysensormqtt.domain.use_case
import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.domain.repository.SensorRepository
import com.example.trysensormqtt.domain.result.SensorResult
import javax.inject.Inject

class CreateSensorUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend fun execute(sensor: SensorEntity): SensorResult<SensorEntity> {
        return repository.createSensor(sensor)
    }
}

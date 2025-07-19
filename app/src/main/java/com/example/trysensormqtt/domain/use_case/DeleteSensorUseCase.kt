package com.example.trysensormqtt.domain.use_case

import com.example.trysensormqtt.domain.repository.SensorRepository
import com.example.trysensormqtt.domain.result.SensorResult
import javax.inject.Inject

class DeleteSensorUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend fun execute(sensorId: Long): SensorResult<Boolean> {
        return repository.deleteSensor(sensorId)
    }
}

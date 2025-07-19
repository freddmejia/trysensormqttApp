package com.example.trysensormqtt.domain.use_case

import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.domain.repository.SensorRepository
import com.example.trysensormqtt.domain.result.SensorResult
import javax.inject.Inject

class GetAllSensorsUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend fun execute(limit: Int, offset: Int): SensorResult<List<SensorEntity>> {
        return repository.fetchAllSensors(limit = limit, offset = offset)
    }
}

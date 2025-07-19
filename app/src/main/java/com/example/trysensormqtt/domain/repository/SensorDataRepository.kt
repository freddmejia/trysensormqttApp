package com.example.trysensormqtt.domain.repository

import com.example.trysensormqtt.data.local.entity.SensorDataEntity
import com.example.trysensormqtt.domain.result.SensorResult

interface SensorDataRepository {
    suspend fun insertData(data: SensorDataEntity): SensorResult<SensorDataEntity>
    suspend fun getLastData(sensorId: Int): SensorResult<SensorDataEntity>
    suspend fun getDataHistory(sensorId: Int, limit: Int, offset: Int): SensorResult<List<SensorDataEntity>>
}

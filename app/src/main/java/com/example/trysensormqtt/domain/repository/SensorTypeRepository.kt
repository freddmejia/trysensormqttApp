package com.example.trysensormqtt.domain.repository

import com.example.trysensormqtt.data.local.entity.SensorTypeEntity
import com.example.trysensormqtt.domain.result.SensorResult

interface SensorTypeRepository {
    suspend fun createType(type: SensorTypeEntity): SensorResult<SensorTypeEntity>
    suspend fun deleteType(type: SensorTypeEntity): SensorResult<Boolean>
    suspend fun fetchAllTypes(): SensorResult<List<SensorTypeEntity>>
}

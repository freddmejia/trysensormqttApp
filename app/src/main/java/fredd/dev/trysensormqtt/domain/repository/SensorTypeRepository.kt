package fredd.dev.trysensormqtt.domain.repository

import fredd.dev.trysensormqtt.data.local.entity.SensorTypeEntity
import fredd.dev.trysensormqtt.domain.result.SensorResult

interface SensorTypeRepository {
    suspend fun createType(type: SensorTypeEntity): SensorResult<SensorTypeEntity>
    suspend fun deleteType(type: SensorTypeEntity): SensorResult<Boolean>
    suspend fun fetchAllTypes(): SensorResult<List<SensorTypeEntity>>
}

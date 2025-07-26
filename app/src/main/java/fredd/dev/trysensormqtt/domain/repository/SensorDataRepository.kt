package fredd.dev.trysensormqtt.domain.repository

import fredd.dev.trysensormqtt.data.local.entity.SensorDataEntity
import fredd.dev.trysensormqtt.domain.result.SensorResult

interface SensorDataRepository {
    suspend fun insertData(data: SensorDataEntity): SensorResult<SensorDataEntity>
    suspend fun getLastData(sensorId: Int): SensorResult<SensorDataEntity>
    suspend fun getDataHistory(sensorId: Int, limit: Int, offset: Int): SensorResult<List<SensorDataEntity>>
}

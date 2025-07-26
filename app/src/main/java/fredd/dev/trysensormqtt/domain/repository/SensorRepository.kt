package fredd.dev.trysensormqtt.domain.repository

import fredd.dev.trysensormqtt.data.local.entity.SensorEntity
import fredd.dev.trysensormqtt.domain.result.SensorResult

interface SensorRepository {
    suspend fun createSensor(sensor: SensorEntity): SensorResult<SensorEntity>
    suspend fun updateSensor(sensor: SensorEntity): SensorResult<SensorEntity>
    suspend fun deleteSensor(sensorId: Long): SensorResult<Boolean>
    suspend fun fetchAllSensors(limit: Int, offset: Int): SensorResult<List<SensorEntity>>


}

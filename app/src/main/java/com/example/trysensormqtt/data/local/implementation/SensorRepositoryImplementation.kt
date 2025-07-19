package com.example.trysensormqtt.data.local.implementation

import com.example.trysensormqtt.data.local.dao.SensorDao
import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.domain.repository.SensorRepository
import com.example.trysensormqtt.domain.result.SensorResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SensorRepositoryImplementation @Inject constructor(
    private val sensorDao: SensorDao
) : SensorRepository {

    override suspend fun createSensor(sensor: SensorEntity): SensorResult<SensorEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val id = sensorDao.insertSensor(sensor)
                val newSensor = sensorDao.fetchSensorById(id = id)
                SensorResult.Success(newSensor!!)
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }

    override suspend fun updateSensor(sensor: SensorEntity): SensorResult<SensorEntity> {
        return withContext(Dispatchers.IO) {
            try {
                sensorDao.updateSensor(
                    id = sensor.id.toInt(),
                    name = sensor.name,
                    sensorTypeId = sensor.sensorTypeId,
                    topic = sensor.topic,
                    host = sensor.host,
                    port = sensor.port,
                    user = sensor.user,
                    password = sensor.password
                )
                val updated = sensorDao.fetchSensorById(id = sensor.id)
                SensorResult.Success(updated!!)
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }

    override suspend fun deleteSensor(sensorId: Long): SensorResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                sensorDao.deleteSensor(sensorId)
                SensorResult.Success(true)
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }

    override suspend fun fetchAllSensors(limit: Int, offset: Int): SensorResult<List<SensorEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val sensors = sensorDao.fetchAllSensors(limit, offset)
                SensorResult.Success(sensors)
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }
}

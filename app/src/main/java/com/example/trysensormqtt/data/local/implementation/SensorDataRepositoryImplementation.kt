package com.example.trysensormqtt.data.local.implementation
import com.example.trysensormqtt.data.local.dao.SensorDataDao
import com.example.trysensormqtt.data.local.entity.SensorDataEntity
import com.example.trysensormqtt.domain.repository.SensorDataRepository
import com.example.trysensormqtt.domain.result.SensorResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SensorDataRepositoryImplementation @Inject constructor(
    private val sensorDataDao: SensorDataDao
) : SensorDataRepository {

    override suspend fun insertData(data: SensorDataEntity): SensorResult<SensorDataEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val id = sensorDataDao.insertData(data).toInt()
                val newData = sensorDataDao.fetchLastData(data.sensorId)
                if (newData != null) SensorResult.Success(newData)
                else SensorResult.ErrorEmpty
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }

    override suspend fun getLastData(sensorId: Int): SensorResult<SensorDataEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val last = sensorDataDao.fetchLastData(sensorId)
                if (last != null) SensorResult.Success(last)
                else SensorResult.ErrorEmpty
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }

    override suspend fun getDataHistory(sensorId: Int, limit: Int, offset: Int): SensorResult<List<SensorDataEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val dataList = sensorDataDao.fetchDataBySensor(sensorId, limit, offset)
                if (dataList.isNotEmpty()) SensorResult.Success(dataList)
                else SensorResult.ErrorEmpty
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }
}

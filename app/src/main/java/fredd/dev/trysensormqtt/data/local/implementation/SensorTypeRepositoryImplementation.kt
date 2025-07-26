package fredd.dev.trysensormqtt.data.local.implementation
import fredd.dev.trysensormqtt.data.local.dao.SensorTypeDao
import fredd.dev.trysensormqtt.data.local.entity.SensorTypeEntity
import fredd.dev.trysensormqtt.domain.repository.SensorTypeRepository
import fredd.dev.trysensormqtt.domain.result.SensorResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SensorTypeRepositoryImplementation @Inject constructor(
    private val sensorTypeDao: SensorTypeDao
) : SensorTypeRepository {

    override suspend fun createType(type: SensorTypeEntity): SensorResult<SensorTypeEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val id = sensorTypeDao.insertType(type).toInt()
                val newType = sensorTypeDao.fetchTypeById(id)
                if (newType != null) SensorResult.Success(newType)
                else SensorResult.ErrorEmpty
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }

    override suspend fun deleteType(type: SensorTypeEntity): SensorResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                sensorTypeDao.deleteType(type)
                SensorResult.Success(true)
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }

    override suspend fun fetchAllTypes(): SensorResult<List<SensorTypeEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val types = sensorTypeDao.fetchAllTypes()
                if (types.isNotEmpty()) SensorResult.Success(types)
                else SensorResult.ErrorEmpty
            } catch (e: Exception) {
                SensorResult.Error(e)
            }
        }
    }
}

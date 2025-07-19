package com.example.trysensormqtt.data.local.dao
import androidx.room.*
import com.example.trysensormqtt.data.local.entity.SensorDataEntity


@Dao
interface SensorDataDao {
    @Query("SELECT * FROM sensor_data WHERE sensorId = :sensorId ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun fetchDataBySensor(sensorId: Int, limit: Int, offset: Int): List<SensorDataEntity>

    @Query("SELECT * FROM sensor_data WHERE sensorId = :sensorId ORDER BY timestamp DESC LIMIT 1")
    suspend fun fetchLastData(sensorId: Int): SensorDataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: SensorDataEntity): Long

    @Delete
    suspend fun deleteData(data: SensorDataEntity)
}

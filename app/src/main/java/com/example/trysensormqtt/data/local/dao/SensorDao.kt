package com.example.trysensormqtt.data.local.dao

import androidx.room.*
import com.example.trysensormqtt.data.local.entity.SensorEntity

@Dao
interface SensorDao {
    @Query("SELECT * FROM sensors LIMIT :limit OFFSET :offset")
    suspend fun fetchAllSensors(limit: Int, offset: Int): List<SensorEntity>

    @Query("SELECT * FROM sensors WHERE id = :id")
    suspend fun fetchSensorById(id: Long): SensorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSensor(sensor: SensorEntity): Long

    @Query("DELETE FROM sensors WHERE id = :id")
    suspend fun deleteSensor(id: Long)

    @Query("""
        UPDATE sensors 
        SET name = :name, sensorTypeId = :sensorTypeId, topic = :topic, host = :host, 
            port = :port, user = :user, password = :password
        WHERE id = :id
    """)
    suspend fun updateSensor(
        id: Int,
        name: String,
        sensorTypeId: Long,
        topic: String,
        host: String,
        port: Int,
        user: String?,
        password: String?
    )
}
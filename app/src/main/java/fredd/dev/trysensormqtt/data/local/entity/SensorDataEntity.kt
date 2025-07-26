package fredd.dev.trysensormqtt.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensor_data")
data class SensorDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sensorId: Int,
    val value: String,
    val timestamp: Long
)

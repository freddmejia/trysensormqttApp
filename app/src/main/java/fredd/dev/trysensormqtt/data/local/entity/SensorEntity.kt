package fredd.dev.trysensormqtt.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensors")
data class SensorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val sensorTypeId: Long,
    val topic: String,
    val host: String,
    val port: Int,
    val user: String? = null,
    val password: String? = null
)
package fredd.dev.trysensormqtt.data.local.dao
import androidx.room.*
import fredd.dev.trysensormqtt.data.local.entity.SensorTypeEntity


@Dao
interface SensorTypeDao {
    @Query("SELECT * FROM sensor_types")
    suspend fun fetchAllTypes(): List<SensorTypeEntity>

    @Query("SELECT * FROM sensor_types WHERE id = :id")
    suspend fun fetchTypeById(id: Int): SensorTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertType(type: SensorTypeEntity): Long


    @Delete
    suspend fun deleteType(type: SensorTypeEntity)

    @Transaction
    fun insertInitialSensorTypes() {
        val initialTypes = listOf(
            SensorTypeEntity(name = "Temperature"),
            SensorTypeEntity(name = "Humidity"),
            SensorTypeEntity(name = "Water"),
            SensorTypeEntity(name = "Pressure"),
            SensorTypeEntity(name = "Data")
        )
        initialTypes.forEach { insertBlocking(it) }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBlocking(sensorType: SensorTypeEntity)
}

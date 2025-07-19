package com.example.trysensormqtt.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trysensormqtt.data.local.dao.SensorDao
import com.example.trysensormqtt.data.local.dao.SensorDataDao
import com.example.trysensormqtt.data.local.dao.SensorTypeDao
import com.example.trysensormqtt.data.local.entity.SensorDataEntity
import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.data.local.entity.SensorTypeEntity
import java.util.concurrent.Executors

@Database(
    entities = [SensorEntity::class, SensorDataEntity::class, SensorTypeEntity::class],
    version = 1,
    exportSchema = false
)

abstract  class DataModelDatabase: RoomDatabase() {
    abstract fun sensorDao(): SensorDao
    abstract fun sensorDataDao(): SensorDataDao
    abstract fun sensorTypeDao(): SensorTypeDao

    companion object {
        @Volatile private var instance: DataModelDatabase? = null
        private const val DB_NAME = "trySensorMqtt.db"

        fun getDatabase(context: Context): DataModelDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, DataModelDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor().execute {
                            getDatabase(appContext).sensorTypeDao().insertInitialSensorTypes()
                        }
                    }
                })
                .build()

    }
}
package com.example.trysensormqtt.di

import android.content.Context
import com.example.trysensormqtt.data.local.dao.SensorDao
import com.example.trysensormqtt.data.local.dao.SensorDataDao
import com.example.trysensormqtt.data.local.dao.SensorTypeDao
import com.example.trysensormqtt.data.local.db.DataModelDatabase
import com.example.trysensormqtt.data.local.implementation.SensorDataRepositoryImplementation
import com.example.trysensormqtt.data.local.implementation.SensorRepositoryImplementation
import com.example.trysensormqtt.data.local.implementation.SensorTypeRepositoryImplementation
import com.example.trysensormqtt.data.remote.implementation.MqttRepositoryImpl
import com.example.trysensormqtt.domain.repository.SensorDataRepository
import com.example.trysensormqtt.domain.repository.SensorMqttRepository
import com.example.trysensormqtt.domain.repository.SensorRepository
import com.example.trysensormqtt.domain.repository.SensorTypeRepository
import com.example.trysensormqtt.domain.use_case.CreateSensorTypeUseCase
import com.example.trysensormqtt.domain.use_case.CreateSensorUseCase
import com.example.trysensormqtt.domain.use_case.DeleteSensorTypeUseCase
import com.example.trysensormqtt.domain.use_case.DeleteSensorUseCase
import com.example.trysensormqtt.domain.use_case.GetAllSensorTypesUseCase
import com.example.trysensormqtt.domain.use_case.GetAllSensorsUseCase
import com.example.trysensormqtt.domain.use_case.GetLastSensorDataUseCase
import com.example.trysensormqtt.domain.use_case.GetSensorDataHistoryUseCase
import com.example.trysensormqtt.domain.use_case.InsertSensorDataUseCase
import com.example.trysensormqtt.domain.use_case.SensorDataUseCases
import com.example.trysensormqtt.domain.use_case.SensorTypeUseCases
import com.example.trysensormqtt.domain.use_case.SensorUseCases
import com.example.trysensormqtt.domain.use_case.UpdateSensorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    //Db, dao's
    @Singleton
    @Provides
    fun provideDataModelDatabase(@ApplicationContext appContext: Context) = DataModelDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideSensorDao(db: DataModelDatabase) = db.sensorDao()

    @Singleton
    @Provides
    fun provideSensorTypeDao(db: DataModelDatabase) = db.sensorTypeDao()

    @Singleton
    @Provides
    fun provideSensorDataDao(db: DataModelDatabase) = db.sensorDataDao()

    // --- Repositories ---
    @Singleton
    @Provides
    fun provideSensorRepository(sensorDao: SensorDao): SensorRepository {
        return SensorRepositoryImplementation(sensorDao)
    }

    @Singleton
    @Provides
    fun provideSensorTypeRepository(sensorTypeDao: SensorTypeDao): SensorTypeRepository {
        return SensorTypeRepositoryImplementation(sensorTypeDao)
    }

    @Singleton
    @Provides
    fun provideSensorDataRepository(sensorDataDao: SensorDataDao): SensorDataRepository {
        return SensorDataRepositoryImplementation(sensorDataDao)
    }

    // --- UseCases grouped ---
    @Singleton
    @Provides
    fun provideSensorDataUseCases(repository: SensorDataRepository): SensorDataUseCases {
        return SensorDataUseCases(
            insertData = InsertSensorDataUseCase(repository),
            getLastData = GetLastSensorDataUseCase(repository),
            getHistory = GetSensorDataHistoryUseCase(repository)
        )
    }


    @Singleton
    @Provides
    fun provideSensorTypeUseCases(repository: SensorTypeRepository): SensorTypeUseCases {
        return SensorTypeUseCases(
            createType = CreateSensorTypeUseCase(repository),
            deleteType = DeleteSensorTypeUseCase(repository),
            getAllTypes = GetAllSensorTypesUseCase(repository)
        )
    }

    @Singleton
    @Provides
    fun provideSensorUseCases(repository: SensorRepository): SensorUseCases {
        return SensorUseCases(
            createSensorUseCase = CreateSensorUseCase(repository),
            updateSensorUseCase = UpdateSensorUseCase(repository),
            deleteSensorUseCase = DeleteSensorUseCase(repository),
            getAllSensorsUseCase = GetAllSensorsUseCase(repository)
        )
    }

    @Singleton
    @Provides
    fun provideMqttRepository(@ApplicationContext ctx: Context): SensorMqttRepository =
        MqttRepositoryImpl(ctx)

}
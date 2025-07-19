package com.example.trysensormqtt.domain.use_case

data class SensorDataUseCases(
    val insertData: InsertSensorDataUseCase,
    val getLastData: GetLastSensorDataUseCase,
    val getHistory: GetSensorDataHistoryUseCase
)

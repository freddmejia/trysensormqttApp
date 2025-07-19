package com.example.trysensormqtt.domain.use_case

data class SensorTypeUseCases(
    val createType: CreateSensorTypeUseCase,
    val deleteType: DeleteSensorTypeUseCase,
    val getAllTypes: GetAllSensorTypesUseCase
)

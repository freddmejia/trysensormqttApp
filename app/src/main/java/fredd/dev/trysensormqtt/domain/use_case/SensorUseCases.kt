package fredd.dev.trysensormqtt.domain.use_case

data class SensorUseCases(
    val createSensorUseCase: CreateSensorUseCase,
    val updateSensorUseCase: UpdateSensorUseCase,
    val deleteSensorUseCase: DeleteSensorUseCase,
    val getAllSensorsUseCase: GetAllSensorsUseCase
)

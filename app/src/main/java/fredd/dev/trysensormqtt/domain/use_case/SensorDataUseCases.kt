package fredd.dev.trysensormqtt.domain.use_case

data class SensorDataUseCases(
    val insertData: InsertSensorDataUseCase,
    val getLastData: GetLastSensorDataUseCase,
    val getHistory: GetSensorDataHistoryUseCase
)

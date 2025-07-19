package com.example.trysensormqtt.domain.result

sealed class SensorUIState {
    object Idle : SensorUIState()
    data class Success(val message: String) : SensorUIState()
    data class Error(val message: String) : SensorUIState()
}
/*
sealed class SensorUIState {
    object Idle : SensorUIState()
    object Success : SensorUIState()
    object Error : SensorUIState()
}
 */
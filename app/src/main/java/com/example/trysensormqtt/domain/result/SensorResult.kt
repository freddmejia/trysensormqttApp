package com.example.trysensormqtt.domain.result

sealed class SensorResult<out T> {
    data class Success<out T>(val data: T) : SensorResult<T>()
    data class Error(val exception: Throwable) : SensorResult<Nothing>()
    object ErrorEmpty : SensorResult<Nothing>()
}

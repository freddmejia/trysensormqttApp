package com.example.trysensormqtt.domain.repository

import com.example.trysensormqtt.data.local.entity.SensorEntity
import kotlinx.coroutines.flow.Flow


import kotlinx.coroutines.flow.SharedFlow

interface SensorMqttRepository {
    val sensorMessages : SharedFlow<Pair<Long, String>>
    val sensorStates   : SharedFlow<Pair<Long, MqttState>>

    fun startSensor(sensor: SensorEntity)
    fun stopSensor(sensorId: Long)
    fun stopAll()
}

enum class MqttState { Connecting, Connected, ConnectionLost, Error }

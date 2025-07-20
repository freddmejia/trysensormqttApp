package com.example.trysensormqtt.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.domain.repository.MqttState
import com.example.trysensormqtt.domain.repository.SensorMqttRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MqttViewModel @Inject constructor(
    private val repo: SensorMqttRepository
) : ViewModel() {

    val messages: StateFlow<Pair<Long, String>?> =
        repo.sensorMessages
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val states: StateFlow<Pair<Long, MqttState>?> =
        repo.sensorStates
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun startSensor(sensor: SensorEntity)   = repo.startSensor(sensor)
    fun stopSensor(sensorId: Long)          = repo.stopSensor(sensorId)
    fun stopAll()                           = repo.stopAll()
}

package com.example.trysensormqtt.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trysensormqtt.data.local.entity.SensorEntity
import com.example.trysensormqtt.domain.result.SensorResult
import com.example.trysensormqtt.domain.result.SensorUIState
import com.example.trysensormqtt.domain.use_case.SensorUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val useCases: SensorUseCases
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _uiEvent = MutableSharedFlow<SensorUIState>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _sensors = MutableLiveData<SensorResult<List<SensorEntity>>>()
    val sensors: LiveData<SensorResult<List<SensorEntity>>> = _sensors


    private var currentOffset = 0
    private val limitPerPage = 5
    private val maxStored = 50

    /*fun createSensor(sensor: SensorEntity) = viewModelScope.launch {
        _loading.value = true
        val result = useCases.createSensorUseCase.execute(sensor)
        val event = when (result) {
            is SensorResult.Success -> SensorUIState.Success
            is SensorResult.Error -> SensorUIState.Error
            SensorResult.ErrorEmpty -> SensorUIState.Error
        }
        _uiEvent.emit(event)
        delay(500)
        _loading.value = false
        fetchAllSensors()
    }*/

    fun fetchAllSensors() = viewModelScope.launch {
        _loading.value = true
        try {
            val result = useCases.getAllSensorsUseCase.execute(limit = 100, offset = 0)

            _sensors.value = if (result is SensorResult.Success) {
                SensorResult.Success(result.data)
            } else {
                SensorResult.ErrorEmpty
            }
        } catch (e: Exception) {
            _sensors.value = SensorResult.Error(e)
        } finally {
            _loading.value = false
        }
    }


    /*fun fetchAllSensors() = viewModelScope.launch {
        val previousList = (_sensors.value as? SensorResult.Success)?.data ?: emptyList()
        val result = useCases.getAllSensorsUseCase.execute(
            limit = limitPerPage,
            offset = currentOffset
        )

        if (result is SensorResult.Success) {
            val combined = (previousList + result.data)
                .distinctBy { it.id }
                .takeLast(maxStored)
            _sensors.value = SensorResult.Success(combined)
            currentOffset += limitPerPage
        } else if (previousList.isNotEmpty()) {
            _sensors.value = SensorResult.Success(previousList)
        } else {
            _sensors.value = SensorResult.ErrorEmpty
        }
    }*/

    fun updateSensor(sensor: SensorEntity) = viewModelScope.launch {
        val updateResult = useCases.updateSensorUseCase.execute(sensor)
        if (updateResult is SensorResult.Success<*>) {
            val currentList = (_sensors.value as? SensorResult.Success)?.data ?: return@launch
            val updatedList = currentList.map {
                if (it.id == sensor.id) sensor else it
            }
            _sensors.value = SensorResult.Success(updatedList)
        }
    }

    fun deleteSensor(sensorId: Long) = viewModelScope.launch {
        try {
            _loading.value = true
            val deletedResult = useCases.deleteSensorUseCase.execute(sensorId)
            when (deletedResult) {
                is SensorResult.Success -> {
                    _uiEvent.emit(SensorUIState.Success("Sensor deleted successfully"))
                    fetchAllSensors()
                }
                else -> _uiEvent.emit(SensorUIState.Error("Failed to delete sensor"))
            }
        } catch (e: Exception) {
            _uiEvent.emit(SensorUIState.Error("Exception: ${e.message}"))
        } finally {
            _loading.value = false
        }
    }



    /*fun deleteSensor(sensorId: Long) = viewModelScope.launch {
        try {
            _loading.value = true
            val deletedResult = useCases.deleteSensorUseCase.execute(sensorId)
            when (deletedResult) {
                is SensorResult.Success -> {
                    _uiEvent.emit(SensorUIState.Success("Sensor deleted successfully"))

                    //val currentList = (_sensors.value as? SensorResult.Success)?.data ?: emptyList()
                    //val updatedList = currentList.filter { it.id != sensorId }
                    //_sensors.value = SensorResult.Success(updatedList)
                    fetchAllSensors()
                }
                is SensorResult.Error, SensorResult.ErrorEmpty -> {
                    _uiEvent.emit(SensorUIState.Error("Failed to delete sensor"))
                }
            }
        } catch (e: Exception) {
            _uiEvent.emit(SensorUIState.Error("Exception: ${e.message}"))
        } finally {
            _loading.value = false
        }
    }*/


    fun resetSensors() {
        _sensors.value = SensorResult.Success(emptyList())
        currentOffset = 0
    }

    fun createSensor(
        name: String,
        host: String,
        port: String,
        user: String?,
        password: String?,
        topic: String,
        typeId: Long
    ) = viewModelScope.launch {
        if (name.isBlank() || host.isBlank() || port.isBlank() || topic.isBlank()) {
            _uiEvent.emit(SensorUIState.Error("Required fields missing"))
            return@launch
        }

        try {
            _loading.value = true
            val sensor = SensorEntity(
                name = name,
                host = host,
                port = port.toIntOrNull() ?: 1883,
                user = user,
                password = password,
                topic = topic,
                sensorTypeId = typeId
            )
            val result = useCases.createSensorUseCase.execute(sensor)
            if (result is SensorResult.Success) {
                _uiEvent.emit(SensorUIState.Success("Sensor created successfully"))
                fetchAllSensors()
            } else {
                _uiEvent.emit(SensorUIState.Error("Error creating sensor"))
            }
        } catch (e: Exception) {
            _uiEvent.emit(SensorUIState.Error("Exception: ${e.message}"))
        } finally {
            _loading.value = false
        }
    }

    suspend fun isHostReachable(host: String): Boolean {
        return try {
            val address = java.net.InetAddress.getByName(host)
            address.isReachable(1000)
        } catch (e: Exception) {
            false
        }
    }
}

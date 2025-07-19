package com.example.trysensormqtt.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trysensormqtt.data.local.entity.SensorDataEntity
import com.example.trysensormqtt.domain.result.SensorResult
import com.example.trysensormqtt.domain.result.SensorUIState
import com.example.trysensormqtt.domain.use_case.SensorDataUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorDataViewModel @Inject constructor(
    private val useCases: SensorDataUseCases
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _uiEvent = MutableSharedFlow<SensorUIState>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _lastData = MutableLiveData<SensorResult<SensorDataEntity>>()
    val lastData: LiveData<SensorResult<SensorDataEntity>> = _lastData

    private val _history = MutableLiveData<SensorResult<List<SensorDataEntity>>>()
    val history: LiveData<SensorResult<List<SensorDataEntity>>> = _history

    private var currentOffset = 0
    private val limitPerPage = 10
    private val maxStored = 100

    fun insertData(data: SensorDataEntity) = viewModelScope.launch {
        /*_loading.value = true
        val result = useCases.insertData.execute(data)
        val event = when (result) {
            is SensorResult.Success -> SensorUIState.Success
            is SensorResult.Error -> SensorUIState.Error
            SensorResult.ErrorEmpty -> SensorUIState.Error
        }
        _uiEvent.emit(event)
        delay(300)
        _loading.value = false
        fetchLastData(data.sensorId)*/
    }

    fun fetchLastData(sensorId: Int) = viewModelScope.launch {
        val result = useCases.getLastData.execute(sensorId)
        _lastData.value = result
    }

    fun fetchHistory(sensorId: Int) = viewModelScope.launch {
        val previousList = (_history.value as? SensorResult.Success)?.data ?: emptyList()
        val result = useCases.getHistory.execute(sensorId, limitPerPage, currentOffset)

        if (result is SensorResult.Success) {
            val combined = (previousList + result.data)
                .distinctBy { it.id }
                .takeLast(maxStored)
            _history.value = SensorResult.Success(combined)
            currentOffset += limitPerPage
        } else if (previousList.isNotEmpty()) {
            _history.value = SensorResult.Success(previousList)
        } else {
            _history.value = SensorResult.ErrorEmpty
        }
    }

    fun resetHistory() {
        _history.value = SensorResult.Success(emptyList())
        currentOffset = 0
    }
}
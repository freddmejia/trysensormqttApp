package com.example.trysensormqtt.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trysensormqtt.data.local.entity.SensorTypeEntity
import com.example.trysensormqtt.domain.result.SensorResult
import com.example.trysensormqtt.domain.result.SensorUIState
import com.example.trysensormqtt.domain.use_case.SensorTypeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorTypeViewModel @Inject constructor(
    private val useCases: SensorTypeUseCases
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _uiEvent = MutableSharedFlow<SensorUIState>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _types = MutableLiveData<SensorResult<List<SensorTypeEntity>>>()
    val types: LiveData<SensorResult<List<SensorTypeEntity>>> = _types

    fun createType(type: SensorTypeEntity) = viewModelScope.launch {
        /*_loading.value = true
        val result = useCases.createType.execute(type)
        val event = when (result) {
            is SensorResult.Success -> SensorUIState.Success
            is SensorResult.Error -> SensorUIState.Error
            SensorResult.ErrorEmpty -> SensorUIState.Error
        }
        _uiEvent.emit(event)
        delay(300)
        _loading.value = false
        fetchAllTypes()*/
    }

    fun fetchAllTypes() = viewModelScope.launch {
        val result = useCases.getAllTypes.execute()
        _types.value = result
    }

    fun deleteType(type: SensorTypeEntity) = viewModelScope.launch {
        /*val result = useCases.deleteType.execute(type)
        val event = when (result) {
            is SensorResult.Success -> SensorUIState.Success
            is SensorResult.Error -> SensorUIState.Error
            SensorResult.ErrorEmpty -> SensorUIState.Error
        }
        _uiEvent.emit(event)
        fetchAllTypes()*/
    }

    fun resetTypes() {
        _types.value = SensorResult.Success(emptyList())
    }
}

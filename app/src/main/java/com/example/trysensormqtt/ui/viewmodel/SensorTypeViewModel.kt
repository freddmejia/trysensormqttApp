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

    private val _types = MutableLiveData<SensorResult<List<SensorTypeEntity>>>()
    val types: LiveData<SensorResult<List<SensorTypeEntity>>> = _types

    fun fetchAllTypes() = viewModelScope.launch {
        val result = useCases.getAllTypes.execute()
        _types.value = result
    }

}

package fredd.dev.trysensormqtt.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fredd.dev.trysensormqtt.data.local.entity.SensorTypeEntity
import fredd.dev.trysensormqtt.domain.result.SensorResult
import fredd.dev.trysensormqtt.domain.use_case.SensorTypeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
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

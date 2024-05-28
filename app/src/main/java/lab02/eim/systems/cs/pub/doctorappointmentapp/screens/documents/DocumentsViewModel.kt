package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.documents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MXRay
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.XRayRepository
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(private val repository: XRayRepository) : ViewModel() {
    private val _xRayList = MutableStateFlow<List<MXRay>>(emptyList())
    val xRayList = _xRayList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllXRays().distinctUntilChanged()
                .collect {listOfXRays ->
                    if (listOfXRays.isNullOrEmpty()) {
                        Log.d("Empty", "Empty list")
                    } else {
                        _xRayList.value = listOfXRays
                    }

                }
        }
    }
    fun addXRay(xRay: MXRay) = viewModelScope.launch { repository.addXRay(xRay) }
    fun removeXRay(xRay: MXRay) = viewModelScope.launch { repository.deleteXRay(xRay) }
}
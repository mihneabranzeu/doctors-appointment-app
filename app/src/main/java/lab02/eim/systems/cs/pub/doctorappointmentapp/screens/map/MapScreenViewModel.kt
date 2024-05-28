package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.map

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MDoctor
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MLocation
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.FireRepository
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.LocationRepository
import lab02.eim.systems.cs.pub.doctorappointmentapp.utils.toLatLngBounds
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    locationRepository: LocationRepository,
    private val fireRepository: FireRepository
) : ViewModel() {
    private val _eventChannel = Channel<MapScreenEvent>()

    internal fun getEventChannel() = _eventChannel.receiveAsFlow()
    val showAll = MutableStateFlow(true)

    val doctors: MutableState<DataOrException<List<MDoctor>, Boolean, Exception>> = mutableStateOf(DataOrException(listOf(), false, Exception("")))

    val mapScreenViewState = locationRepository.locations.combine(showAll) { allLocations, _ ->
        if (allLocations.isEmpty()) {
            MapScreenViewState.Loading
        } else {
            val boundingBox = allLocations.map { it.location }.toLatLngBounds()
            MapScreenViewState.LocationList(allLocations, boundingBox)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MapScreenViewState.Loading
    )

    fun getDoctorsForLocation(location: MLocation) {
        viewModelScope.launch {
            doctors.value.loading = true
            doctors.value = fireRepository.getAllDoctorsFromDatabase()

            if (!doctors.value.data.isNullOrEmpty()) doctors.value.loading = false
            doctors.value.data = doctors.value.data?.filter { doctor -> doctor.location.equals(location.name) }
        }
        Log.d("Map", "getDoctorsForLocation: ${doctors.value.data?.toList().toString()}")
    }

    init {
        // Load the full set of mountains
        viewModelScope.launch {
            locationRepository.loadLocations()
            getDoctorsForLocation(locationRepository.locations.value.first())
        }
    }
}
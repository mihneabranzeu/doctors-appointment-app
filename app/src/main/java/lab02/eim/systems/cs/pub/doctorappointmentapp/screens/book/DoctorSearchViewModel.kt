package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.Resource
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MDoctor
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.FireRepository
import javax.inject.Inject

@HiltViewModel
class DoctorSearchViewModel @Inject constructor (private val repository: FireRepository): ViewModel() {
    var availableTimes: List<String> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)
    val listOfDoctors: MutableState<DataOrException<List<MDoctor>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(), true, Exception("")))

    fun searchDoctors(specialty: String, location: String) {
        viewModelScope.launch {
            listOfDoctors.value.loading = true
            listOfDoctors.value = repository.getAllDoctorsFromDatabase()

            if (!listOfDoctors.value.data.isNullOrEmpty()) listOfDoctors.value.loading = false

            // Filter the doctors by specialty and location
            Log.d("GET", "before filter: ${listOfDoctors.value.data?.toList().toString()}")
            listOfDoctors.value.data = listOfDoctors.value.data?.filter { doctor -> doctor.speciality.equals(specialty) && doctor.location.equals(location) }
            Log.d("GET", "after filter: ${listOfDoctors.value.data?.toList().toString()}")
        }
        Log.d("GET", "getAllDoctorsDatabase: ${listOfDoctors.value.data?.toList().toString()}")
    }

    fun getAvailableTimes(doctorId: String, year: String, month: String, day: String) {
        viewModelScope.launch {
            availableTimes = repository.getAvailableHours(doctorId, year, month, day)
        }
    }

}
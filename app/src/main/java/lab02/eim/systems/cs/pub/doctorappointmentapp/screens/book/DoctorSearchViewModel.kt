package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.Resource
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MDoctor
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.DoctorRepository
import javax.inject.Inject

@HiltViewModel
class DoctorSearchViewModel @Inject constructor (private val repository: DoctorRepository): ViewModel() {
    var listOfDoctors: List<MDoctor> by mutableStateOf(listOf())
    var availableTimes: List<String> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

        init {
            searchDoctors("Cardiology", "Timisoara")
        }

        fun searchDoctors(specialty: String, location: String) {
            viewModelScope.launch() {
                if (specialty.isEmpty() || location.isEmpty()) {
                    return@launch
                }
                try {
                    when (val response = repository.getDoctors(specialty, location)) {
                        is Resource.Success -> {
                            listOfDoctors= response.data!!
                            if (listOfDoctors.isNotEmpty()) isLoading = false;
//                            isLoading = false
                        }
                        is Resource.Error -> {
                            isLoading = false
                            Log.e("Network", "searchBooks: Failed getting books", )
                        }
                        else -> {isLoading = false}
                    }
                } catch (exception: Exception) {

                    isLoading = false;
                    Log.d("Network", exception.message.toString())

              }
            }
        }

        fun getAvailableTimes(doctorId: String, date: String)  {
            viewModelScope.launch() {
                try {
                    when (val response = repository.getAvailableTimes(doctorId, date)) {
                        is Resource.Success -> {
                            availableTimes = response.data!!
                            if (availableTimes.isNotEmpty()) isLoading = false;
                            Log.d("Network", "getAvailableTimes: ${response.data}")
                        }
                        is Resource.Error -> {
                            Log.e("Network", "getAvailableTimes: Failed getting available times", )
                        }
                        else -> {isLoading = false}
                    }
                } catch (exception: Exception) {
                    Log.d("Network", exception.message.toString())
                }
            }
        }

}
package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MDoctor
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.DoctorRepository
import javax.inject.Inject

@HiltViewModel
class DoctorSearchViewModel @Inject constructor (private val repository: DoctorRepository): ViewModel() {
                val listOfDoctors: MutableState<DataOrException<List<MDoctor>, Boolean, Exception>> =
            mutableStateOf(DataOrException(null, null, Exception("")))

        init {
            searchDoctors("Cardiology", "Timisoara")
        }

        fun searchDoctors(specialty: String, location: String) {
            viewModelScope.launch() {
               if (specialty.isEmpty()) {
                   return@launch
               }
                listOfDoctors.value.loading = true;
                listOfDoctors.value = repository.getDoctors(specialty, location)
                Log.d("Doc", listOfDoctors.value.data.toString())
                if (listOfDoctors.value.data.toString().isNotEmpty()) listOfDoctors.value.loading = false
            }
        }

}
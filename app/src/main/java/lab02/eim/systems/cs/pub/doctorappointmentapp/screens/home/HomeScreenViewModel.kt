package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.FireRepository
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: FireRepository) : ViewModel() {
        val data: MutableState<DataOrException<List<MAppointment>, Boolean, Exception>>
        = mutableStateOf(DataOrException(listOf(), true, Exception("")))

       init {
           getAllAppointmentsFromDatabase()
       }

    fun getAllAppointmentsFromDatabase() {
       viewModelScope.launch {
           data.value.loading = true
           data.value = repository.getAllAppointmentsFromDatabase()

           if (!data.value.data.isNullOrEmpty()) data.value.loading = false
       }
        Log.d("GET", "getAllAppointmentsFromDatabase: ${data.value.data?.toList().toString()}")
    }
}
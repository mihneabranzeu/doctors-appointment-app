package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.appointments

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.WorkManagerRepository
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(private val repository: WorkManagerRepository): ViewModel() {

    fun startWork() {
        repository.startWork()
    }
}
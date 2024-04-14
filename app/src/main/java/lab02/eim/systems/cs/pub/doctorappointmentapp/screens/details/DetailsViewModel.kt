package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.Resource
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.DoctorRepository
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: DoctorRepository): ViewModel() {
    suspend fun getAppointmentInfo(appointmentId: String): Resource<MAppointment> {
        return repository.getAppointmentInfo(appointmentId)
    }
}
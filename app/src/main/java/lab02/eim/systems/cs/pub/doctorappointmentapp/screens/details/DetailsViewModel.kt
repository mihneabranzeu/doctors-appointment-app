package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.Resource
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.DoctorRepository
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.FireRepository
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: FireRepository): ViewModel() {
    suspend fun getAppointmentInfo(appointmentId: String): DataOrException<MAppointment, Boolean, Exception> {
        return repository.getAppointmentInfo(appointmentId)
    }
}
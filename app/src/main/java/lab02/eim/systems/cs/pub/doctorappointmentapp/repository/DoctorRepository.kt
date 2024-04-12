package lab02.eim.systems.cs.pub.doctorappointmentapp.repository

import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MDoctor
import lab02.eim.systems.cs.pub.doctorappointmentapp.network.DoctorsApi
import javax.inject.Inject

class DoctorRepository @Inject constructor(private val api: DoctorsApi) {
    private val dataOrException = DataOrException<List<MDoctor>, Boolean, Exception>()
    private val appointmentDataOrException = DataOrException<MDoctor, Boolean, Exception>()
    suspend fun getDoctors(specialty: String, location: String): DataOrException<List<MDoctor>, Boolean, Exception> {
        try {
            dataOrException.loading = true
//            dataOrException.data = api.getAllDoctors(searchQuery)
            dataOrException.data = listOf(
                MDoctor("1", "John","Doe", "Cardiology", "Cluj-Napoca"),
                MDoctor("2", "Petre","Dumitrescu", "Epidemiology", "Cluj-Napoca"),
                MDoctor("3", "Stefan","Tutugan", "Cardiology", "Timisoara"),
                MDoctor("4", "Tudor","Coheea", "Epidemiology", "Cluj-Napoca"),
                MDoctor("5", "Vlad","Pangratie", "Cardiology", "Timisoara"),
                MDoctor("6", "Eduard","Honciu", "Cardiology", "Cluj-Napoca")
            ).filter { doctor -> doctor.speciality.equals(specialty) && doctor.location.equals(location) }
            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false

        } catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun getDoctorInfo(appointmentId: String): DataOrException<MDoctor, Boolean, Exception> {
        val dataOrException = DataOrException<MDoctor, Boolean, Exception>()
        try {
            appointmentDataOrException.loading = true
            appointmentDataOrException.data = api.getDoctorInfo(appointmentId)
            if (appointmentDataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            appointmentDataOrException.e = e
        }
        return dataOrException
    }
}
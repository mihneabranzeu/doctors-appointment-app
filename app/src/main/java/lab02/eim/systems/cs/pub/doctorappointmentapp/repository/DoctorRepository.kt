package lab02.eim.systems.cs.pub.doctorappointmentapp.repository

import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.Resource
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MDoctor
import lab02.eim.systems.cs.pub.doctorappointmentapp.network.DoctorsApi
import javax.inject.Inject

class DoctorRepository @Inject constructor(private val api: DoctorsApi) {
    suspend fun getDoctors(specialty: String, location: String): Resource<List<MDoctor>> {
        return try {
               Resource.Loading(data = true)
//            dataOrException.data = api.getAllDoctors(searchQuery)
            val doctorList = listOf(
                MDoctor("1", "John","Doe", "Cardiology", "Cluj-Napoca"),
                MDoctor("2", "Petre","Dumitrescu", "Epidemiology", "Cluj-Napoca"),
                MDoctor("3", "Stefan","Tutugan", "Cardiology", "Timisoara"),
                MDoctor("4", "Tudor","Coheea", "Epidemiology", "Cluj-Napoca"),
                MDoctor("5", "Vlad","Pangratie", "Cardiology", "Timisoara"),
                MDoctor("6", "Eduard","Honciu", "Cardiology", "Cluj-Napoca")
            ).filter { doctor -> doctor.speciality.equals(specialty) && doctor.location.equals(location) }
            if (doctorList.isNotEmpty()) Resource.Loading(false)
            Resource.Success(doctorList)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

//    suspend fun getDoctorInfo(appointmentId: String): DataOrException<MDoctor, Boolean, Exception> {
//        val dataOrException = DataOrException<MDoctor, Boolean, Exception>()
//        try {
//            appointmentDataOrException.loading = true
//            appointmentDataOrException.data = api.getDoctorInfo(appointmentId)
//            if (appointmentDataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
//        } catch (e: Exception) {
//            appointmentDataOrException.e = e
//        }
//        return dataOrException
//    }
}
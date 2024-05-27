package lab02.eim.systems.cs.pub.doctorappointmentapp.repository

import android.util.Log
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.Resource
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
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

    suspend fun getAppointmentInfo(appointmentId: String): Resource<MAppointment> {
      val response = try {
          Resource.Loading(true)
//          api.getDoctorInfo()
          val appointments = listOf(
//              MAppointment("1", "2022-01-01", "cardiology", "Petre Dumitrescu", "Orhideea"),
//              MAppointment("2", "2022-02-01", "orthopedy", "Dre", "Orhideea"),
//              MAppointment("3", "2022-03-01", "massage", "Mihai Popescu", "Orhideea"),
//              MAppointment("4", "2022-04-01", "urology", "Petre Dumitrescu", "Orhideea"),
//              MAppointment("5", "2022-05-01", "general", "Petre Dumitrescu", "Orhideea"),
//              MAppointment("6", "2022-06-01", "pediatry", "Petre Dumitrescu", "Orhideea"),
//              MAppointment("7", "2022-07-01", "cardiology", "Petre Dumitrescu", "Orhideea"),
              MAppointment()
          )
          appointments.find { appointment ->  appointment.id == appointmentId}
      } catch (exception: Exception) {
          return Resource.Error(message = "An error occurred ${exception.message.toString()}")
      }
        Resource.Loading(false)
        return Resource.Success(response!!)
    }

    suspend fun getAvailableTimes(doctorId: String, date: String): Resource<List<String>> {
        val response = try {
            Resource.Loading(true)
//          api.getDoctorInfo()
           val availableTimes = listOf(
               "15.04.2024 10:00",
                "15.04.2024 11:00",
                "15.04.2024 12:00",
                "15.04.2024 13:00",
                "16.04.2024 14:00",
                "16.04.2024 15:00",
                "16.04.2024 16:00",
                "16.04.2024 17:00",
                "26.05.2024 10:00",
           )
            Log.d("AvailableTimes" ,"doctorId: $doctorId, date: $date")
            availableTimes.filter { time -> time.contains(date) }
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(false)
        return Resource.Success(response!!)
    }
}
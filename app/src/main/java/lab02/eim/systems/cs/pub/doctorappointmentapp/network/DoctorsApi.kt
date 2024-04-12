package lab02.eim.systems.cs.pub.doctorappointmentapp.network

import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MDoctor
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface DoctorsApi {
    @GET("doctors")
    suspend fun getAllDoctors(@Query("q") query: String): List<MDoctor>

    @GET("doctors/{doctorId}")
    suspend fun getDoctorInfo(@Path("doctorId") doctorId: String):MDoctor
}
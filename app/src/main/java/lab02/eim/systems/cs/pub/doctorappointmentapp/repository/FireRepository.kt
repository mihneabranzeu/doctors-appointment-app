package lab02.eim.systems.cs.pub.doctorappointmentapp.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.Resource
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MDoctor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryAppointment: Query,
    private val queryDoctor: Query
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllAppointmentsFromDatabase(): DataOrException<List<MAppointment>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MAppointment>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryAppointment.get().await().documents.map { documentSnapshot ->
                val appointment = documentSnapshot.toObject(MAppointment::class.java)!!
                val doctor = appointment.doctorId?.let { getDoctorDetails(it).data }
                appointment.doctor = doctor
                appointment.isUpcoming = isAppointmentUpcoming(appointment)
                appointment
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }
        return dataOrException
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isAppointmentUpcoming(appointment: MAppointment): Boolean {
        // Format the month and day as two digits
        val formattedMonth = appointment.month?.padStart(2, '0')
        val formattedDay = appointment.day?.padStart(2, '0')

        // Combine the year, month, day, and hour fields into a string
        val appointmentDateTimeString = "${appointment.year}-$formattedMonth-$formattedDay ${appointment.hour}:00"

        // Parse the string into a LocalDateTime object
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val appointmentDateTime = LocalDateTime.parse(appointmentDateTimeString, formatter)

        // Get the current date and time
        val currentDateTime = LocalDateTime.now()

        // Compare the two LocalDateTime objects
        return appointmentDateTime.isAfter(currentDateTime)
    }

    suspend fun getDoctorDetails(doctorId: String): DataOrException<MDoctor, Boolean, Exception> {
        val dataOrException = DataOrException<MDoctor, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryDoctor.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MDoctor::class.java)!!
            }.find { doctor -> doctor.id == doctorId }
            if (dataOrException.data != null) dataOrException.loading = false
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }
        return dataOrException
    }

    suspend fun getAllDoctorsFromDatabase(): DataOrException<List<MDoctor>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MDoctor>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryDoctor.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MDoctor::class.java)!!
            }
            if (dataOrException.data != null) dataOrException.loading = false
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }
        return dataOrException
    }

    suspend fun getAvailableHours(
        doctorId: String,
        year: String,
        month: String,
        day: String
    ): List<String> {
        val dataOrException = DataOrException<List<MAppointment>, Boolean, Exception>()
        val availableTimes = mutableListOf<String>("10", "11", "12", "13", "14", "15", "16", "17")

        try {
            dataOrException.loading = true
            dataOrException.data = queryAppointment.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MAppointment::class.java)!!
            }.filter { appointment ->
                appointment.doctorId == doctorId &&
                appointment.year == year && appointment.month == month && appointment.day == day
            }
            if (dataOrException.data != null) dataOrException.loading = false
            // Filter the available times
            dataOrException.data?.forEach { appointment ->
                availableTimes.remove(appointment.hour)
            }
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }
        return availableTimes
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAppointmentInfo(appointmentId: String): DataOrException<MAppointment, Boolean, Exception> {
        val dataOrException = DataOrException<MAppointment, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryAppointment.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MAppointment::class.java)!!
            }.find { appointment -> appointment.id == appointmentId }

            dataOrException.data?.doctor = getDoctorDetails(dataOrException.data?.doctorId!!).data
            dataOrException.data?.isUpcoming = isAppointmentUpcoming(dataOrException.data!!)

            if (dataOrException.data != null) dataOrException.loading = false
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }
        return dataOrException

    }
}
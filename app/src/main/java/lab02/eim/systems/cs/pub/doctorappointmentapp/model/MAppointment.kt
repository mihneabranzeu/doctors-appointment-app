package lab02.eim.systems.cs.pub.doctorappointmentapp.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class MAppointment(
                        @Exclude var id: String? = null,
                        var year: String? = null,
                        var month: String? = null,
                        var day: String? = null,
                        var hour: String? = null,
                        var category: String? = null,
                        @get: PropertyName("user_id")
                        @set: PropertyName("user_id")
                        var userId: String? = null,
                        @get: PropertyName("doctor_id")
                        @set: PropertyName("doctor_id")
                        var doctorId: String? = null,
                        @get: Exclude var doctor: MDoctor? = null,
                        @get: Exclude var isUpcoming: Boolean? = false,
                        var location: String? = null,
                        var details: String? = null,
                        var diagnostic: String? = null) {

}

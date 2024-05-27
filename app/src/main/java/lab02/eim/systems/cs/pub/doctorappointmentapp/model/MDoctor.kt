package lab02.eim.systems.cs.pub.doctorappointmentapp.model

import com.google.firebase.firestore.PropertyName

data class MDoctor(var id: String? = null,
                    @get:PropertyName("first_name")
                    @set:PropertyName("first_name")
                   var firstName: String? = null,
                    @get:PropertyName("last_name")
                    @set:PropertyName("last_name")
                   var lastName: String? = null,
                   var speciality: String? = null,
                   var location: String? = null) {
}

package lab02.eim.systems.cs.pub.doctorappointmentapp.model

import com.google.android.gms.maps.model.LatLng

data class MLocation (
    val id: Int,
    val name: String,
    val location: LatLng
)
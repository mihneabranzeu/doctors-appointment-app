package lab02.eim.systems.cs.pub.doctorappointmentapp.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

fun Collection<LatLng>.toLatLngBounds() : LatLngBounds {
    if (isEmpty()) error("Cannot create a LatLngBounds from an empty list")

    return LatLngBounds.builder().apply {
        for (latLng in this@toLatLngBounds) {
            include(latLng)
        }
    }.build()
}
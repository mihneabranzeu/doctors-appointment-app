package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.map

import com.google.android.gms.maps.model.LatLngBounds
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MLocation

sealed class MapScreenViewState {
    data object Loading : MapScreenViewState()
    data class LocationList(
        val locations: List<MLocation>,
        val boundingBox: LatLngBounds
    ): MapScreenViewState()
}
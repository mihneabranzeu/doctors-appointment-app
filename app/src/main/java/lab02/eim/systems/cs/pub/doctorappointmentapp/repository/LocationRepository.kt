package lab02.eim.systems.cs.pub.doctorappointmentapp.repository

import android.content.Context
import android.util.Xml
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import lab02.eim.systems.cs.pub.doctorappointmentapp.R
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MLocation
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class LocationRepository(@ApplicationContext val context: Context) {
    private val _locations = MutableStateFlow(emptyList<MLocation>())
    val locations: StateFlow<List<MLocation>> = _locations
    private var loaded = false

    suspend fun loadLocations(): StateFlow<List<MLocation>> {
        if (!loaded) {
            loaded = true
            _locations.value = withContext(Dispatchers.IO) {
                context.resources.openRawResource(R.raw.locations).use { inputStream ->
                    readLocations(inputStream)
                }
            }
        }
        return locations
    }

    private fun readLocations(inputStream: InputStream) =
        readWaypoints(inputStream).mapIndexed {index, waypoint ->
            waypoint.toLocation(index)
        }.toList()

    private data class WaypointBuilder(
        var latitude: Double? = null,
        var longitude: Double? = null,
        var name: String? = null,
    ) {
        fun build(): Waypoint? {
            val latitude = latitude ?: return null
            val longitude = longitude ?: return null
            val name = name ?: return null

            return Waypoint(
                name = name,
                location = LatLng(latitude, longitude),
            )
        }
    }

    private fun readWaypoints(inputStream: InputStream): Sequence<Waypoint> = sequence {
        // We don't use namespaces
        val ns: String? = null
        val parser = Xml.newPullParser()
        parser.setInput(inputStream, null)

        try {
            var eventType = parser.eventType

            var builder: WaypointBuilder? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (parser.name == "wpt") {
                            builder = WaypointBuilder(
                                latitude = parser.getAttributeValue(ns, "lat").toDouble(),
                                longitude = parser.getAttributeValue(ns, "lon").toDouble(),
                            )
                        } else if (builder != null) {
                            when (parser.name) {
                                "name" -> builder.name = parser.nextText()
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (parser.name == "wpt" && builder != null) {
                            builder.build()?.let { waypoint -> yield(waypoint) }
                            builder = null
                        }
                    }
                }
                eventType = parser.next()
            }

        } catch (e: XmlPullParserException) {
            // Handle parsing errors
        } catch (e: IOException) {
            // Handle IO errors
        }
    }
}

private data class Waypoint(
    val name: String,
    val location: LatLng,
)

private fun Waypoint.toLocation(id: Int): MLocation {
    return MLocation(
        id = id,
        name = name,
        location = location,
    )
}
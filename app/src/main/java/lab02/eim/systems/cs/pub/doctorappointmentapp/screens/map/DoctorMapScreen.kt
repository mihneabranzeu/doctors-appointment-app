package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import lab02.eim.systems.cs.pub.doctorappointmentapp.R
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.DoctorAppBar
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.FABContent
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MLocation
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorScreens
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.appointments.NavigationOption
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.DoctorRow

@Composable
fun DoctorMapScreen(navController: NavController, viewModel: MapScreenViewModel =  hiltViewModel()){
    val screenViewState = viewModel.mapScreenViewState.collectAsState()
    val viewState = screenViewState.value


    val context = LocalContext.current
    var isMapLoaded by remember { mutableStateOf(false) }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.style_json)
            )
        )
    }

    Scaffold (
        topBar = { DoctorAppBar(title = "Appointment App", navController = navController, icon = Icons.AutoMirrored.Filled.ArrowBack ) {
            navController.navigate(DoctorScreens.AppointmentsScreen.name)
        } },
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            when (viewState) {
                MapScreenViewState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
                is MapScreenViewState.LocationList -> {
                    LocationsMap(viewState = viewState, viewModel = viewModel, navController = navController)
                }
            }
        }
    }
}

@Composable
fun LocationsMap(viewState: MapScreenViewState.LocationList, viewModel: MapScreenViewModel, navController: NavController) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom( viewState.boundingBox.center , 5f)
    }

    var showDialog by remember { mutableStateOf(false)}
    var selectedLocation by remember {mutableStateOf<MLocation?>(null)}

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        viewState.locations.forEach {location ->
            Marker(
                state = rememberMarkerState(position = location.location),
                title = location.name,
                tag = location,
                onClick = {
                    selectedLocation = location
                    viewModel.getDoctorsForLocation(location)
                    showDialog = true
                    false
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Doctors at ${selectedLocation?.name}") },
                text = {
                    val listOfDoctors = viewModel.doctors.value.data!!
                    if (viewModel.doctors.value.loading == true) {
                        CircularProgressIndicator()
                    } else {
                        LazyColumn (modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        ) {
                            items(items = listOfDoctors) { doctor ->
                                DoctorRow(doctor = doctor, navController = navController ) {
                                    false
                                }
                            }
                        }
                    }

                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}



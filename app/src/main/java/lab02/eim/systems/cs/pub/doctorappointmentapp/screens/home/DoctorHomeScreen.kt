package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.DoctorAppBar
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.FABContent
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.TitleSection
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorScreens

@Composable
fun Home(navController: NavController) {
    Scaffold (
        topBar = { DoctorAppBar(title = "Appointment App", navController = navController)},
        floatingActionButton = {
            FABContent {

            }
        },
    ) {
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeContent(navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavController) {
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserNamae = if (!email.isNullOrEmpty())
        email.split("@")?.get(0) else "N/A"

    Column (modifier = Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top) {
        Row (modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Upcoming Appointments")

            Spacer(modifier = Modifier.fillMaxWidth(0.7f))

            Column {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription ="Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(DoctorScreens.ProfileScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colorScheme.secondary)

            Text(text = currentUserNamae!!,
                    modifier = Modifier.padding(2.dp),
                    color = Color.Red,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Clip)
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun UpcomingAppointmentsArea(appointments: List<MAppointment>, navController: NavController) {
    //upcoming appointments
}



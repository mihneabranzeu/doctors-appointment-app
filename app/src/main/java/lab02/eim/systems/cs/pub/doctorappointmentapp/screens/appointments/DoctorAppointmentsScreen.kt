package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.appointments

import android.graphics.ColorFilter
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import lab02.eim.systems.cs.pub.doctorappointmentapp.R
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.DoctorAppBar
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorScreens
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.DoctorList
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.DropdownMenuBox
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.MyDatePickerDialog
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.MyTimePickerDialog
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.saveToFirebase

@Composable
fun AppointmentsScreen(navController: NavController) {
    Scaffold (
        topBar = { DoctorAppBar(title = "Appointment App", navController = navController)}
    ) {
        Surface (modifier = Modifier
            .padding(it)
            .fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavigationOption(
                    iconId = R.drawable.calendar,
                    label = "Appointments",
                    onClick = { navController.navigate(DoctorScreens.DoctorHomeScreen.name) }
                )
                NavigationOption(
                    iconId = R.drawable.map,
                    label = "Locations",
                    onClick = { navController.navigate(DoctorScreens.DoctorMapScreen.name) }
                )
                NavigationOption(
                    iconId = R.drawable.documents,
                    label = "Documents",
                    onClick = { navController.navigate(DoctorScreens.AppointmentsScreen.name) }
                )

            }
        }

    }


}

@Composable
fun NavigationOption(iconId: Int, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = label,
            modifier = Modifier
                .size(80.dp),
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label,
            fontSize = 24.sp,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
        )
    }
}
package lab02.eim.systems.cs.pub.doctorappointmentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorNavigation
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.DoctorSearchViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.ui.theme.DoctorAppointmentAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoctorAppointmentAppTheme {
                DoctorAppointmentApp()
            }
        }
    }
}

@Composable
fun DoctorAppointmentApp() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
            DoctorNavigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoctorAppointmentAppTheme {
    }
}
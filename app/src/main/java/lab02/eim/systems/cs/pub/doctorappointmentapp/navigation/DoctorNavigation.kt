package lab02.eim.systems.cs.pub.doctorappointmentapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.DoctorSplashScreen
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.appointments.AppointmentsScreen
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.BookAppointmentScreen
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.DoctorSearchViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.home.Home
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.login.Login
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.profile.ProfileScreen

@Composable
fun DoctorNavigation() {
   val navController = rememberNavController()
   NavHost(navController = navController, startDestination = DoctorScreens.SplashScreen.name) {
      composable(DoctorScreens.SplashScreen.name) {
         DoctorSplashScreen(navController = navController)
      }

      composable(DoctorScreens.DoctorHomeScreen.name) {
         Home(navController = navController)
      }

      composable(DoctorScreens.AppointmentsScreen.name) {
         AppointmentsScreen(navController = navController)
      }

      composable(DoctorScreens.BookAppointmentScreen.name) {
         val bookViewModel = hiltViewModel<DoctorSearchViewModel>()
         BookAppointmentScreen(navController = navController, bookViewModel)
      }

      composable(DoctorScreens.LoginScreen.name) {
         Login(navController = navController)
      }

      composable(DoctorScreens.ProfileScreen.name) {
         ProfileScreen(navController = navController)
      }
   }
}
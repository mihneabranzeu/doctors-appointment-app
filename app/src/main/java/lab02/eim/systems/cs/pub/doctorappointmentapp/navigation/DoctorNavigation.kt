package lab02.eim.systems.cs.pub.doctorappointmentapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.DoctorSplashScreen
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.appointments.AppointmentsScreen
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.BookAppointmentScreen
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book.DoctorSearchViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.details.AppointmentDetailsScreen
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.documents.DoctorDocumentsScreen
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.documents.DocumentsViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.home.Home
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.home.HomeScreenViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.login.Login
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.map.DoctorMapScreen
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.map.MapScreenViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.profile.ProfileScreen

@Composable
fun DoctorNavigation() {
   val navController = rememberNavController()
   NavHost(navController = navController, startDestination = DoctorScreens.SplashScreen.name) {
      composable(DoctorScreens.SplashScreen.name) {
         DoctorSplashScreen(navController = navController)
      }

      composable(DoctorScreens.DoctorHomeScreen.name) {
         val homeViewModel = hiltViewModel<HomeScreenViewModel>()
         Home(navController = navController, viewModel = homeViewModel)
      }

      composable(DoctorScreens.AppointmentsScreen.name) {
         AppointmentsScreen(navController = navController)
      }

      composable(DoctorScreens.BookAppointmentScreen.name) {
         val bookViewModel = hiltViewModel<DoctorSearchViewModel>()
         BookAppointmentScreen(navController = navController, bookViewModel)
      }

      composable(DoctorScreens.DoctorMapScreen.name) {
         val mapViewModel = hiltViewModel<MapScreenViewModel>()
         DoctorMapScreen(navController = navController, viewModel = mapViewModel)
      }

      composable(DoctorScreens.DoctorDocumentsScreen.name) {
         val documentsVewModel = hiltViewModel<DocumentsViewModel>()
         DoctorDocumentsScreen(navController = navController, viewModel = documentsVewModel)
      }

      composable(DoctorScreens.LoginScreen.name) {
         Login(navController = navController)
      }

      composable(DoctorScreens.ProfileScreen.name) {
         ProfileScreen(navController = navController)
      }

      val detailsName = DoctorScreens.AppointmentDetailsScreen.name
      composable("$detailsName/{appointmentId}", arguments = listOf(navArgument("appointmentId"){
         type = NavType.StringType
      })) { BackStackEntry ->
         BackStackEntry.arguments?.getString("appointmentId").let {
            AppointmentDetailsScreen(navController = navController, appointmentId = it.toString())
         }
      }
   }
}
package lab02.eim.systems.cs.pub.doctorappointmentapp.navigation

import androidx.compose.foundation.layout.Spacer
import java.lang.IllegalArgumentException

enum class DoctorScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    BookAppointmentScreen,
    AppointmentsScreen,
    ProfileScreen,
    DoctorHomeScreen;

    companion object {
        fun fromRoute(route: String?): DoctorScreens
            = when(route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                LoginScreen.name -> LoginScreen
                CreateAccountScreen.name -> CreateAccountScreen
                BookAppointmentScreen.name -> BookAppointmentScreen
                AppointmentsScreen.name -> AppointmentsScreen
                ProfileScreen.name -> ProfileScreen
                DoctorHomeScreen.name -> DoctorHomeScreen
                null -> DoctorHomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized!")
            }
    }
}
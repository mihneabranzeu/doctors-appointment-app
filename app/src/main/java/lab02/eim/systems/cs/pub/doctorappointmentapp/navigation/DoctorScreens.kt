package lab02.eim.systems.cs.pub.doctorappointmentapp.navigation

import java.lang.IllegalArgumentException

enum class DoctorScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    BookAppointmentScreen,
    AppointmentsScreen,
    ProfileScreen,
    DoctorHomeScreen,
    DoctorMapScreen,
    AppointmentDetailsScreen,
    DoctorFLScreen,
    DoctorDocumentsScreen;
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
                AppointmentDetailsScreen.name -> AppointmentDetailsScreen
                DoctorMapScreen.name -> DoctorMapScreen
                DoctorDocumentsScreen.name -> DoctorDocumentsScreen
                DoctorFLScreen.name -> DoctorFLScreen
                null -> DoctorHomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized!")
            }
    }
}
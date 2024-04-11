package lab02.eim.systems.cs.pub.doctorappointmentapp.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import lab02.eim.systems.cs.pub.doctorappointmentapp.R
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorScreens

@Composable
fun DoctorSplashScreen(navController: NavController) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 0.9f,
                        animationSpec = tween(durationMillis = 800,
                                                easing = {
                                                    OvershootInterpolator(8f)
                                                        .getInterpolation(it)
                                                }))
        delay(2000L)

        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(DoctorScreens.LoginScreen.name)
        } else {
            navController.navigate(DoctorScreens.DoctorHomeScreen.name)
        }
    }
   Column(verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.scale(scale = scale.value)) {
       Image(painter = painterResource(R.drawable.healthcare), contentDescription = "app logo")
       Text(text = "Doctor Appointment App", style = MaterialTheme.typography.headlineLarge )
   }
}
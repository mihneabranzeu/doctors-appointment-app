package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.fl

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.DoctorAppBar
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorScreens

@Composable
fun DoctorFLScreen(navController: NavController, viewModel: FLViewModel = hiltViewModel()) {
    var deviceId = remember { mutableStateOf("") }
    var isLoadButtonEnabled = remember { mutableStateOf(true) }
    var isTrainButtonEnabled = remember { mutableStateOf(false) }
    val scope = MainScope()
    val textResult by viewModel.textResult.collectAsState()

    val context = LocalContext.current

    Scaffold (
        topBar = { DoctorAppBar(title = " Federated Learning",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            navController = navController,
            showProfile = false) {
            navController.navigate(DoctorScreens.DoctorHomeScreen.name)
        } }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally) {

                TextField(value = deviceId.value,
                    onValueChange = {deviceId.value = it},
                    label = { Text("Device ID") },
                    modifier = Modifier.fillMaxWidth())

                Button(onClick = {
                    if (deviceId.value.isEmpty() || !(1 .. 10).contains(deviceId.value.toInt())) {
                        Toast.makeText(context, "Please enter a client partition ID between 1 and 10 (inclusive)", Toast.LENGTH_LONG).show()
                    } else {
                        // Disable button
                        isLoadButtonEnabled.value = false
                        isTrainButtonEnabled.value = false
                        viewModel.setTextResult("Loading the local training dataset in memory. It will take several seconds.")
                        scope.launch {
                            val result = viewModel.loadDataInBackground(deviceId.value)
                            viewModel.setTextResult(result)
                            isLoadButtonEnabled.value = true
                            isTrainButtonEnabled.value = true
                        }
                    }
                },
                    modifier = Modifier
                        .height(60.dp)
                        .padding(top = 16.dp),
                    enabled = isLoadButtonEnabled.value) {
                    Text("Load Data")
                }
                Button(onClick = {
                    val parameters = viewModel.getParameters()
                    scope.launch {
                        viewModel.sendParameters(parameters)
                    }
                },
                    modifier = Modifier
                        .height(60.dp)
                        .padding(top = 16.dp),
                    enabled = isTrainButtonEnabled.value) {
                    Text("Send Parameters")
                }
                Button(onClick = {
                    scope.launch {
                        viewModel.handleFit()
                    }
                    viewModel.setTextResult("Started Training")
                },
                    modifier = Modifier
                        .height(60.dp)
                        .padding(top = 16.dp),
                ) {
                    Text("Train")
                }
                Button(onClick = {
                    scope.launch {
                        viewModel.handleEvaluate()
                    }
                },
                    modifier = Modifier
                        .height(60.dp)
                        .padding(top = 16.dp),
                    enabled = isTrainButtonEnabled.value) {
                    Text("Evaluate")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .border(2.dp, Color.Black)
                ) {
                    Text(text = textResult)
                }
            }
        }
    }
}

private const val TAG = "MainActivity"
typealias Float3DArray = Array<Array<FloatArray>>
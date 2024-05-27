package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.DoctorAppBar
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MDoctor
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorScreens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(navController: NavController, viewModel: DoctorSearchViewModel = hiltViewModel()) {

    Scaffold (
        topBar = { DoctorAppBar(title = "Book Appointment",
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    navController = navController,
                    showProfile = false) {
            navController.navigate(DoctorScreens.DoctorHomeScreen.name)
        }
                 },
    ) {
        Surface (modifier = Modifier
            .padding(it)
            .fillMaxWidth()) {
            Column(modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                val date = remember {
                    mutableStateOf("Choose a date")
                }

                val specialty = remember {
                    mutableStateOf("")
                }

                val location = remember {
                    mutableStateOf("")
                }

                var time = remember {
                    mutableStateOf("")
                }

                var doctorId = remember {
                    mutableStateOf("")
                }
                var specialties = listOf(
                    "Cardiology",
                    "Epidemiology",
                    "Nutrition",
                    "Psychiatry",
                    "Pulmonology"
                )

                var locations = listOf (
                    "Cluj-Napoca",
                    "Bucuresti",
                    "Timisoara",
                    "Iasi",
                    "Constanta"
                )

                val valid = remember(date, location, specialty) {
                    derivedStateOf {
                        date.value.trim() != "Choose a date" && location.value.trim().isNotEmpty() && specialty.value.trim().isNotEmpty()
                    }
                }

                var showDatePicker by remember {
                    mutableStateOf(false)
                }

                var displayDoctors by remember {
                    mutableStateOf(false)
                }

                var showTimePicker by remember {
                    mutableStateOf(false)
                }

                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()) {
                    Button(onClick = { showDatePicker = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)) {
                        Text(text = date.value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (showDatePicker) {
                    MyDatePickerDialog(
                        onDateSelected = { date.value = it },
                        onDismiss = { showDatePicker = false }
                    )
                }

                if (showTimePicker) {
                    MyTimePickerDialog(
                        doctorId = doctorId.value,
                        date = date.value,
                        onTimeSelected = {
                            time.value = it
                           showTimePicker = false },
                        onDismiss = {showTimePicker = false}
                    )
                }

                DropdownMenuBox(specialties, specialty, "Category")
                DropdownMenuBox(locations, location, "Location")

                Button(onClick = {
                    viewModel.searchDoctors(specialty.value, location.value)
                    displayDoctors = true
                },  enabled = valid.value) {
                    Text(text = "Search")
                }

                Spacer(modifier = Modifier.height(13.dp))



                if (displayDoctors) {
                    Log.d("BookAppointmentScreen", "time: ${time.value.toString()}")

                    val firstPart = date.value.split(" ")[0]
                    val dayMonthYear = firstPart.split(".")
                    val day = Integer.parseInt(dayMonthYear[0])
                    val month = Integer.parseInt(dayMonthYear[1])
                    val year = Integer.parseInt(dayMonthYear[2])

                    DoctorList( navController ) { id ->
                        viewModel.getAvailableTimes(id, year.toString(), month.toString(), day.toString())
                        doctorId.value = id
                        showTimePicker = true
                    }
                    Button(onClick = {
                        Log.d("Buc", "date: ${date.value}, time: ${time.value}, specialty: ${specialty.value}, location: ${location.value} ,doctorId: ${doctorId.value}")
                        val appointment = MAppointment(
                            year = year.toString(),
                            month = month.toString(),
                            day = day.toString(),
                            hour = time.value,
                            category = specialty.value,
                            doctorId = doctorId.value,
                            location = location.value,
                            details = "Details",
                            diagnostic = "Diagnostic",
                            userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        )
                        saveToFirebase(appointment, navController)
                    }, enabled = valid.value && time.value.trim().isNotEmpty()) {
                        Text(text = "Book")
                    }
                }
            }
        }

    }

}
fun saveToFirebase(appointment: MAppointment, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("appointments")

    if (appointment.toString().isNotEmpty()) {
        dbCollection.add(appointment)
            .addOnSuccessListener {documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener {task ->
                        if (task.isSuccessful) {
                            navController.popBackStack()
                        }
                    }.addOnFailureListener {
                        Log.w("Error", "Error adding document", it)
                    }
            }
    } else {

    }

}

@Composable
fun MyTimePickerDialog(doctorId: String, date: String, viewModel: DoctorSearchViewModel = hiltViewModel(), onTimeSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val availableTimes = viewModel.availableTimes

    LaunchedEffect(doctorId) {
        // Extract year, month, day from date
        val dayMonthYear = date.split(".")
        val day = Integer.parseInt(dayMonthYear[0])
        val month = Integer.parseInt(dayMonthYear[1])
        val year = Integer.parseInt(dayMonthYear[2])

        viewModel.getAvailableTimes(doctorId, year.toString(), month.toString(), day.toString())
    }


    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Select a time") },
        text = {
            if (availableTimes.isEmpty()) {
                Text("No available times")
            } else {
                LazyRow {
                    items(availableTimes) { time ->
                        Button(onClick = { onTimeSelected(time) },
                            modifier = Modifier.padding(4.dp)) {
                            Text("$time:00")
                        }
                    }
                }
            }

        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Close")
            }
        }
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Composable
fun DoctorRow(doctor: MDoctor, navController: NavController, onDoctorSelected: (String) -> Unit) {
    Card(modifier = Modifier
        .clickable { onDoctorSelected(doctor.id.toString()) }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
            val imageUrl = "https://t3.ftcdn.net/jpg/02/60/04/08/360_F_260040863_fYxB1SnrzgJ9AOkcT0hoe7IEFtsPiHAD.jpg"
            Image(painter = rememberImagePainter(data = imageUrl), contentDescription = "Doctor",
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
                    .clip(RoundedCornerShape(10.dp)),
                )
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = doctor.firstName + " " + doctor.lastName, fontWeight = FontWeight.Bold)
                Text(text = doctor.speciality.toString())
                Text(text = doctor.location.toString())
            }

        }
    }
}

@Composable
fun DoctorList(navController: NavController, viewModel: DoctorSearchViewModel = hiltViewModel(), onDoctorSelected: (String) -> Unit ) {
    val listOfDoctors = viewModel.listOfDoctors.value.data!!
    if (viewModel.listOfDoctors.value.loading == true) {
        LinearProgressIndicator()
    } else {
        LazyColumn (modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = listOfDoctors) { doctor ->
                DoctorRow(doctor = doctor, navController, onDoctorSelected)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(options: List<String>, selected: MutableState<String>, placeholder: String = "Select") {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selected.value,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("$placeholder") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selected.value = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}



fun convertMillisToDate(it: Long): String {
    val date = Date(it)
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return format.format(date)
}

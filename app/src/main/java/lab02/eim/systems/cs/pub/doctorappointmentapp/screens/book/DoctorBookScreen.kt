package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.book

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import dagger.hilt.android.lifecycle.HiltViewModel
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.DoctorAppBar
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
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                var date by remember {
                    mutableStateOf("Choose a date")
                }

                val specialty = remember {
                    mutableStateOf("")
                }

                val location = remember {
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

                var showDatePicker by remember {
                    mutableStateOf(false)
                }

                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()) {
                    Button(onClick = { showDatePicker = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)) {
                        Text(text = date, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (showDatePicker) {
                    MyDatePickerDialog(
                        onDateSelected = { date = it },
                        onDismiss = { showDatePicker = false }
                    )
                }

                DropdownMenuBox(specialties, specialty)
                DropdownMenuBox(locations, location)

                Button(onClick = {
                             viewModel.searchDoctors(specialty.value, location.value)
                }, ) {
                    Text(text = "Search")
                }

                Spacer(modifier = Modifier.height(13.dp))

                DoctorList( navController );



            }
        }

    }

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
fun DoctorRow(doctor: MDoctor, navController: NavController) {
    Card(modifier = Modifier
        .clickable { }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
    ) {
        Row(Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {
            val imageUrl = "https://t3.ftcdn.net/jpg/02/60/04/08/360_F_260040863_fYxB1SnrzgJ9AOkcT0hoe7IEFtsPiHAD.jpg"
            Image(painter = rememberImagePainter(data = imageUrl), contentDescription = "Doctor",
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp))
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = doctor.firstName + " " + doctor.lastName, fontWeight = FontWeight.Bold)
                Text(text = doctor.speciality.toString())
                Text(text = doctor.location.toString())
            }

        }
    }
}

@Composable
fun DoctorList(navController: NavController, viewModel: DoctorSearchViewModel = hiltViewModel() ) {
    val listOfDoctors = viewModel.listOfDoctors
    LazyColumn (modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = listOfDoctors) { doctor ->
            DoctorRow(doctor = doctor, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(options: List<String>, selected: MutableState<String>) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
    val format = SimpleDateFormat("dd.MM.YYYY", Locale.getDefault())
    return format.format(date)
}

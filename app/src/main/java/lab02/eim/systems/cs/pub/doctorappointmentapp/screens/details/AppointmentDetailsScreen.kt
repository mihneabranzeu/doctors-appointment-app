package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.details

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.DoctorAppBar
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.DataOrException
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.Resource
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorScreens

@Composable
fun AppointmentDetailsScreen(navController: NavController,
                             appointmentId: String,
                             viewModel: DetailsViewModel = hiltViewModel()) {
    Scaffold (
        topBar = {
            DoctorAppBar(
                title = "Appointment Details",
                navController = navController,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                showProfile = false) {
                navController.navigate(DoctorScreens.DoctorHomeScreen.name)
            }
        }
    ) {
        Surface (modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier
                .padding(top = 12.dp)
                .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                val appointmentInfo = produceState<DataOrException<MAppointment, Boolean, Exception>>(initialValue = DataOrException(MAppointment(), true, Exception(""))) {
                    value = viewModel.getAppointmentInfo(appointmentId)
                }.value

                if (appointmentInfo.data == null) {
                    Row {
                        LinearProgressIndicator()
                        Text(text = "Loading...")
                    }
                } else {
                    // Doctor info card
                    Card(shape = RoundedCornerShape(29.dp),
                        colors = CardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(330.dp)
                            .height(100.dp)
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(29.dp)
                            ),
                    ) {
                        Row (verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()){
                            Icon(imageVector = Icons.Filled.AccountCircle,
                                contentDescription ="Profile",
                                modifier = Modifier
                                    .size(65.dp),
                                tint = MaterialTheme.colorScheme.secondary)


                            Spacer(modifier = Modifier.width(15.dp))
                            Column() {
                                Text(text = "Dr. ${appointmentInfo.data!!.doctor?.firstName} ${appointmentInfo.data!!.doctor?.lastName}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold)
                                Text(text = "${appointmentInfo.data!!.category}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal)
                            }
                        }
                    }

                    // Appointment date and location
                    Card(shape = RoundedCornerShape(29.dp),
                        colors = CardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(330.dp)
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(29.dp)
                            ),
                    ) {
                       Column(modifier = Modifier
                           .fillMaxWidth()
                           .padding(15.dp),
                           verticalArrangement = Arrangement.Top,
                           horizontalAlignment = Alignment.Start) {
                          Text(text = "Appointment Date",
                              fontWeight = FontWeight.Normal,
                              fontSize = 15.sp
                          )
                           Text(text = "${appointmentInfo.data!!.day}.${appointmentInfo.data!!.month}-${appointmentInfo.data!!.year} ${appointmentInfo.data!!.hour}",
                               fontWeight = FontWeight.Bold,
                               fontSize = 20.sp
                           )

                           HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), color = MaterialTheme.colorScheme.secondary)


                           Text(text = "Location",
                               fontWeight = FontWeight.Normal,
                               fontSize = 15.sp
                           )
                           Text(text = appointmentInfo.data!!.location.toString(),
                               fontWeight = FontWeight.Bold,
                               fontSize = 20.sp
                           )
                       }
                    }

                    // Appointment details
                    Card(shape = RoundedCornerShape(29.dp),
                        colors = CardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(330.dp)
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(29.dp)
                            ),
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start) {
                            Text(text = "Investigation Details",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            )
                            //TODO: Add description field to appointment
                            Text(text = "Nulla enim ipsum adipisicing qui nulla cupidatat minim consectetur. Nostrud irure est culpa excepteur sint pariatur veniam minim sint magna Lorem irure. Ea in occaecat nisi. Nostrud adipisicing ullamco amet sint sunt labore aliquip ea. Voluptate ullamco elit veniam.",
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        }
                    }

                    // Appointment diagnostic
                    Card(shape = RoundedCornerShape(29.dp),
                        colors = CardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(330.dp)
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(29.dp)
                            ),
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start) {
                            Text(text = "Diagnostic",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            )
                            //TODO: Add diagnostic field to appointment
                            Text(text = "voluptate consequat cupidatat excepteur laboris id ut Lorem laboris voluptate magna aute aliquip esse labore occaecat aliqua excepteur minim non",
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

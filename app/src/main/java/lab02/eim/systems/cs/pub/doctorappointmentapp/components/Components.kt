package lab02.eim.systems.cs.pub.doctorappointmentapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import lab02.eim.systems.cs.pub.doctorappointmentapp.R
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MAppointment
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorScreens

@Composable
fun EmailInput(modifier: Modifier = Modifier,
               emailState: MutableState<String>,
               labelId: String = "Email",
               enabled: Boolean = true,
               imeAction: ImeAction = ImeAction.Next,
               onAction: KeyboardActions = KeyboardActions.Default) {
    InputField(modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)

}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(value = valueState.value,
        onValueChange = {valueState.value = it},
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        enabled = enabled,
        textStyle = TextStyle(fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType,
            imeAction = imeAction),
        keyboardActions = onAction)
}

@Composable
fun PasswordInput(modifier: Modifier,
                  passwordState: MutableState<String>,
                  labelId: String,
                  enabled: Boolean,
                  passwordVisibility: MutableState<Boolean>,
                  imeAction: ImeAction = ImeAction.Done,
                  onAction: KeyboardActions = KeyboardActions.Default) {
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(value = passwordState.value,
        onValueChange = {passwordState.value = it},
        label = { Text(text = labelId) },
        singleLine = true,
        enabled = enabled,
        textStyle = TextStyle(fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        keyboardActions = onAction,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility)})
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icons.Default.Close
    }
}

@Composable
fun TitleSection(modifier: Modifier = Modifier, label: String) {
    Surface (modifier = modifier.padding(start = 5.dp, top=1.dp)) {
        Column {
            Text(text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAppBar(
    title: String,
    showProfile: Boolean = true,
    navController: NavController,
    icon: ImageVector? = null,
    onBackArrowClicked: () -> Unit = {}
) {
    CenterAlignedTopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showProfile) {
                Image(painter = painterResource(id = R.drawable.healthcare), contentDescription = "App logo", modifier = Modifier.size(40.dp))
            }
            if (icon != null) {
                Icon(imageVector = icon, contentDescription ="arrow back", modifier = Modifier.clickable { onBackArrowClicked() })
            }
            Spacer(modifier = Modifier.width(40.dp))
            Text(text = title, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(150.dp))
        }
    },
        actions = {
            IconButton(onClick = { FirebaseAuth.getInstance().signOut().run {
                navController.navigate(DoctorScreens.LoginScreen.name)
            }
            }) {
                if (showProfile) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout", tint = MaterialTheme.colorScheme.secondary)
                } else Box {}
            }

        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.secondary,
            navigationIconContentColor = MaterialTheme.colorScheme.secondary
        )
    )
}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = { onTap() }, shape = RoundedCornerShape(50.dp), containerColor = MaterialTheme.colorScheme.primary) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add button", tint = MaterialTheme.colorScheme.onSecondary)
    }
}

@Composable
fun AppointmentCard(appointment: MAppointment = MAppointment("asdf", "2024-05-01T10:00", "Cardiology", "Dre", "Plaza Romania"),
                    onPressDetails: (String) -> Unit = {} ) {
    Card(shape = RoundedCornerShape(29.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .padding(16.dp)
            .height(180.dp)
            .width(330.dp)
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(29.dp)),
    ) {
        Column(modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top) {
            Row (verticalAlignment = Alignment.CenterVertically){
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription ="Profile",
                    modifier = Modifier
                        .size(65.dp),
                    tint = MaterialTheme.colorScheme.secondary)


                Spacer(modifier = Modifier.width(15.dp))
                Column() {
                    Text(text = "Dr. ${appointment.doctor}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)
                    Text(text = "${appointment.category}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text="Date",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal)
                    Text(text = "${appointment.date?.split("T")?.get(0)}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold)
                }
                Column (modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text(text="Location",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal)
                    Text(text = appointment.location.toString(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold)
                }
            }
        }
        Button(onClick = {onPressDetails(appointment.id.toString()) },
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 15.dp)) {
            Text(text = "Details")
        }
    }
}
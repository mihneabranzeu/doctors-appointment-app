package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.documents

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.DoctorAppBar
import lab02.eim.systems.cs.pub.doctorappointmentapp.components.FABContent
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MXRay
import lab02.eim.systems.cs.pub.doctorappointmentapp.navigation.DoctorScreens
import java.io.ByteArrayOutputStream

@Composable
fun DoctorDocumentsScreen(navController: NavController, viewModel: DocumentsViewModel)  {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var diagnostic by remember { mutableStateOf("") }
    val context = LocalContext.current


    Scaffold (
        topBar = {
            DoctorAppBar(
                title = "Documents",
                navController = navController,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                showProfile = false) {
                navController.navigate(DoctorScreens.AppointmentsScreen.name)
            } },
        floatingActionButton = {
            FABContent {
                showDialog = true
            }
        }
    ) {
        Surface (modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            val scrollState = rememberScrollState()
            val xRays = viewModel.xRayList.collectAsState().value


            Column(modifier = Modifier
                .padding(top = 12.dp)
                .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Upload an X-Ray") },
                        text = {
                            val pickImage = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                                imageUri = uri
                            }


                            Column {
                                Button(onClick = { pickImage.launch("image/*") }) {
                                    Text("Pick an image")
                                }

                                imageUri?.let { uri ->
                                    val image: Painter = rememberAsyncImagePainter(model = uri)
                                    Image(painter = image, contentDescription = "Selected Image")
                                }

                                TextField(
                                    value = diagnostic,
                                    onValueChange = { diagnostic = it },
                                    label = { Text("Diagnostic") }
                                )


                            }
                        },
                        confirmButton = {
                            Row {
                                Button(onClick = { showDialog = false }) {
                                    Text("Close")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(onClick = {
                                   if (imageUri != null && diagnostic.isNotEmpty()) {
                                        val imageBitmap = imageUri?.let { uri ->
                                            context.contentResolver.openInputStream(uri)?.buffered()?.use {
                                                BitmapFactory.decodeStream(it)
                                            }
                                        }
                                       val compressedImageByteArray = ByteArrayOutputStream().use { outputStream ->
                                           imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                                           outputStream.toByteArray()
                                       }
                                       if (compressedImageByteArray != null) {
                                           val xRay = MXRay(imageData = compressedImageByteArray, diagnostic = diagnostic)
                                           viewModel.addXRay(xRay)
                                           showDialog = false
                                       } else {
                                           Toast.makeText(context, "There was an issue with loading the image", Toast.LENGTH_SHORT).show()
                                       }
                                   } else {
                                       Toast.makeText(context, "Please select an image and a diagnostic", Toast.LENGTH_SHORT).show()
                                   }
                                }) {
                                    Text("Save")
                                }

                            }

                        }
                    )
                }

                xRays.forEach { xRay ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                bitmap = bitmapFromByteArray(xRay.imageData),
                                contentDescription = "XRay Image",
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = xRay.diagnostic)
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { viewModel.removeXRay(xRay) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete XRay")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun bitmapFromByteArray(byteArray: ByteArray): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    return bitmap.asImageBitmap()
}

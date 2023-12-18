package com.example.myapplication.ui.screens.edit

import android.annotation.SuppressLint
import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

@SuppressLint("Recycle")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    editViewModel: EditViewModel = viewModel(),
    uri: Uri,
    navController: NavHostController
) {

    val uiState by editViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val contentResolver = context.contentResolver
        val providerClient = contentResolver.acquireContentProviderClient(uri)
        val descriptor = providerClient?.openFile(uri, "r")
        val exif =
            androidx.exifinterface.media.ExifInterface(descriptor!!.fileDescriptor)
            //val date = exif.getAttribute(ExifInterface.TAG_DATETIME)
            val edits = EditUiState(
                device = exif.getAttribute(ExifInterface.TAG_MAKE).toString(),
                model = exif.getAttribute(ExifInterface.TAG_MODEL).toString(),
                latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE).toString(),
                longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE).toString(),
            )
            editViewModel.initUiState(uri, edits)
       descriptor.close()
    }
    LaunchedEffect(Unit) {
        editViewModel.navigateToImageScreen.collect { navigate ->
            if (navigate) {
                navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = uiState.device,
            onValueChange = { editViewModel.onDeviceChange(it) },
            label = { Text("Device name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.model,
            onValueChange = { editViewModel.onModelChange(it) },
            label = { Text("Model") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.latitude,
            onValueChange = { editViewModel.onLatitudeChange(it) },
            label = { Text("Latitude") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.longitude,
            onValueChange = { editViewModel.onLongitudeChange(it) },
            label = { Text("Longitude") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        Button(
            onClick = { editViewModel.save(contentResolver = context.contentResolver) }
        ) {
            Text("Save")
        }
    }
}
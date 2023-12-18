package com.example.myapplication.ui.screens.edit

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class EditViewModel(app: Application) : AndroidViewModel(app) {
    private lateinit var uri: Uri

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()

    private val _navigateToImageScreen = MutableStateFlow(false)
    val navigateToImageScreen: Flow<Boolean>
        get() = _navigateToImageScreen

    fun initUiState(uri: Uri, editUiState: EditUiState) {
        this.uri = uri
        _uiState.value = editUiState
    }

    fun onDeviceChange(value: String) {
        _uiState.value = _uiState.value.copy(device = value)
    }

    fun onModelChange(value: String) {
        _uiState.value = _uiState.value.copy(model = value)
    }

    fun onLatitudeChange(value: String) {
        _uiState.value = _uiState.value.copy(latitude = value)
    }

    fun onLongitudeChange(value: String) {
        _uiState.value = _uiState.value.copy(longitude = value)
    }

    @SuppressLint("Recycle")
    fun save(contentResolver: ContentResolver) {

        val providerClient = contentResolver.acquireContentProviderClient(uri)
        val descriptor = providerClient?.openFile(uri, "rw")
        val exif =
            ExifInterface(descriptor!!.fileDescriptor)

        if (_uiState.value.device != "") {
            val name = _uiState.value.device
            exif.setAttribute(ExifInterface.TAG_MAKE, name)
        }

        if (_uiState.value.model != "") {
            val model = _uiState.value.model
            exif.setAttribute(ExifInterface.TAG_MODEL, model)
        }

        if (_uiState.value.latitude != "") {
            val lat = _uiState.value.latitude
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat)
        }

        if (_uiState.value.longitude != "") {
            val long = _uiState.value.longitude
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, long)
        }
        exif.saveAttributes()
        descriptor.close()

        _navigateToImageScreen.value = true
    }
}

data class EditUiState(
    val date: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val device: String = "",
    val model: String = "",
)
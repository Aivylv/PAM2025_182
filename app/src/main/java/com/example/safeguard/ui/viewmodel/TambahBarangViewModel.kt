package com.example.safeguard.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.modeldata.Patient
import com.example.safeguard.network.ItemApiService
import com.example.safeguard.network.PatientApiService
import com.example.safeguard.repository.UserPreferences
import com.example.safeguard.util.FileUtils // Import Helper tadi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class TambahUiState(
    val item_name: String = "",
    val condition: String = "",
    val patient_id: Int? = null,
    val selectedPatientName: String = "Pilih Pasien",
    val listPasien: List<Patient> = emptyList(),
    val imageUri: Uri? = null,
    val isEntryValid: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)

class TambahBarangViewModel(
    private val itemApiService: ItemApiService,
    private val patientApiService: PatientApiService,
    private val userPreferences: UserPreferences
) : ViewModel() {

    var uiState by mutableStateOf(TambahUiState())
        private set

    init {
        loadPatients()
    }

    private fun loadPatients() {
        viewModelScope.launch {
            try {
                val patients = patientApiService.getPatients()
                uiState = uiState.copy(listPasien = patients)
            } catch (e: Exception) {
                uiState = uiState.copy(error = "Gagal memuat daftar pasien")
            }
        }
    }

    fun onImageSelected(uri: Uri?) {
        uiState = uiState.copy(imageUri = uri)
    }

    fun updateUiState(newState: TambahUiState) {
        uiState = newState.copy(
            isEntryValid = newState.item_name.isNotBlank() && newState.patient_id != null
        )
    }

    fun saveItem(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true)
            try {
                // 1. Ambil User ID (Pastikan tidak "0", harus valid dari database)
                val userIdStr = userPreferences.userId.first() ?: ""

                if (userIdStr.isBlank() || userIdStr == "0") {
                    uiState = uiState.copy(isSaving = false, error = "Sesi Invalid. Silakan Login Ulang.")
                    return@launch
                }

                // 2. Siapkan RequestBody untuk Text
                val itemNameBody = uiState.item_name.toRequestBody("text/plain".toMediaTypeOrNull())
                val conditionBody = uiState.condition.toRequestBody("text/plain".toMediaTypeOrNull())
                val patientIdBody = uiState.patient_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val userIdBody = userIdStr.toRequestBody("text/plain".toMediaTypeOrNull())

                // 3. Siapkan Multipart untuk Foto (Logic Baru)
                var photoPart: MultipartBody.Part? = null
                if (uiState.imageUri != null) {
                    val file = FileUtils.getFileFromUri(context, uiState.imageUri!!)
                    if (file != null) {
                        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)
                    }
                }

                // 4. Kirim ke API
                val response = itemApiService.postItem(
                    itemName = itemNameBody,
                    condition = conditionBody,
                    patientId = patientIdBody,
                    userId = userIdBody,
                    photo = photoPart
                )

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    uiState = uiState.copy(isSaving = false, error = "Gagal: ${response.message()}")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isSaving = false, error = "Error: ${e.message}")
            }
        }
    }
    fun resetForm() {
        uiState = TambahUiState(
            listPasien = uiState.listPasien // Pertahankan list pasien agar tidak perlu loading ulang
        )
    }
}
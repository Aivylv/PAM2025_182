package com.example.safeguard.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.modeldata.Patient // Pastikan model Patient sudah dibuat
import com.example.safeguard.network.ItemApiService
import com.example.safeguard.network.PatientApiService // Pastikan API Service Pasien sudah dibuat
import com.example.safeguard.repository.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

data class TambahUiState(
    val item_name: String = "",
    val condition: String = "",
    val patient_id: Int? = null,
    val selectedPatientName: String = "Pilih Pasien",
    val listPasien: List<Patient> = emptyList(), // Data untuk Dropdown (REQ-14)
    val imageUri: Uri? = null, // URI untuk Foto (REQ-252)
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
        loadPatients() // Ambil daftar pasien saat inisialisasi
    }

    // Mengambil daftar pasien untuk Dropdown (REQ-14)
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
        // REQ-15: Validasi kolom kritis (Nama Barang & Pemilik)
        uiState = newState.copy(
            isEntryValid = newState.item_name.isNotBlank() && newState.patient_id != null
        )
    }

    fun saveItem(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true)
            try {
                val userIdStr = userPreferences.userId.first() ?: "0"

                // Konversi data ke RequestBody untuk Multipart (REQ-252)
                val itemNameBody = uiState.item_name.toRequestBody("text/plain".toMediaTypeOrNull())
                val conditionBody = uiState.condition.toRequestBody("text/plain".toMediaTypeOrNull())
                val patientIdBody = uiState.patient_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val userIdBody = userIdStr.toRequestBody("text/plain".toMediaTypeOrNull())

                // Kirim data ke API
                val response = itemApiService.postItem(
                    itemName = itemNameBody,
                    condition = conditionBody,
                    patientId = patientIdBody,
                    userId = userIdBody,
                    photo = null // Implementasi File Photo akan membutuhkan konversi URI ke MultipartBody.Part
                )

                if (response.isSuccessful) {
                    onSuccess() // REQ-254
                } else {
                    uiState = uiState.copy(isSaving = false, error = "Gagal menyimpan data")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isSaving = false, error = "Koneksi terputus") // REQ-17
            }
        }
    }
}
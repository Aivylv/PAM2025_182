package com.example.safeguard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.modeldata.Patient
import com.example.safeguard.network.PatientApiService
import kotlinx.coroutines.launch

data class PasienUiState(
    val rm_number: String = "",
    val name: String = "",
    val room_info: String = "",
    val isSaving: Boolean = false,
    val error: String? = null
)

class PasienViewModel(private val patientApiService: PatientApiService) : ViewModel() {
    var uiState by mutableStateOf(PasienUiState())
        private set

    fun updateUiState(newState: PasienUiState) {
        uiState = newState
    }

    fun savePatient(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true, error = null)
            try {
                val patient = Patient(
                    patient_id = 0, // Akan di-generate otomatis oleh MySQL (Auto Inc)
                    rm_number = uiState.rm_number,
                    name = uiState.name,
                    room_info = uiState.room_info
                )
                val response = patientApiService.createPatient(patient)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    uiState = uiState.copy(isSaving = false, error = "Gagal mendaftarkan pasien")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isSaving = false, error = "Koneksi terputus")
            }
        }
    }
}
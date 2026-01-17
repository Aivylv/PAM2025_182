package com.example.safeguard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.modeldata.Item
import com.example.safeguard.network.ItemApiService
import kotlinx.coroutines.launch

data class TambahUiState(
    val item_name: String = "",
    val condition: String = "",
    val patient_id: String = "",
    val isEntryValid: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)

class TambahBarangViewModel(private val itemApiService: ItemApiService) : ViewModel() {
    var uiState by mutableStateOf(TambahUiState())
        private set

    fun updateUiState(newState: TambahUiState) {
        // REQ-15: Validasi kolom kritis tidak boleh kosong
        uiState = newState.copy(
            isEntryValid = newState.item_name.isNotBlank() && newState.patient_id.isNotBlank()
        )
    }

    fun saveItem(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true)
            try {
                val item = Item(
                    item_name = uiState.item_name,
                    condition = uiState.condition,
                    status = "Disimpan", // Status awal default
                    patient_id = uiState.patient_id.toInt(),
                    user_id = 1 // ID Petugas dari sesi login
                )
                val response = itemApiService.postItem(item)
                if (response.isSuccessful) {
                    onSuccess() // REQ-254: Notifikasi "Data Berhasil Disimpan"
                } else {
                    uiState = uiState.copy(isSaving = false, error = "Gagal menyimpan data")
                }
            } catch (e: Exception) {
                // REQ-17: Notifikasi jika koneksi internet terputus
                uiState = uiState.copy(isSaving = false, error = "Koneksi terputus")
            }
        }
    }
}
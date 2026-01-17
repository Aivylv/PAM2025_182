package com.example.safeguard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.modeldata.Item
import com.example.safeguard.network.ItemApiService
import com.example.safeguard.repository.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class TambahUiState(
    val item_name: String = "",
    val condition: String = "",
    val patient_id: String = "",
    val isEntryValid: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)

class TambahBarangViewModel(
    private val itemApiService: ItemApiService,
    private val userPreferences: UserPreferences // Tambahkan ini di constructor
) : ViewModel() {
    var uiState by mutableStateOf(TambahUiState())
        private set

    fun updateUiState(newState: TambahUiState) {
        //REQ-15: Validasi kolom kritis tidak boleh kosong [cite: 665]
        uiState = newState.copy(
            isEntryValid = newState.item_name.isNotBlank() && newState.patient_id.isNotBlank()
        )
    }

    fun saveItem(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true)
            try {
                //Ambil User ID asli dari sesi login [cite: 765]
                val userIdStr = userPreferences.userId.first() ?: "0"
                val userId = userIdStr.toIntOrNull() ?: 0

                val item = Item(
                    item_name = uiState.item_name,
                    condition = uiState.condition,
                    status = "Disimpan",
                    patient_id = uiState.patient_id.toInt(),
                    user_id = userId
                )

                val response = itemApiService.postItem(item)
                if (response.isSuccessful) {
                    onSuccess() // REQ-254 [cite: 662]
                } else {
                    uiState = uiState.copy(isSaving = false, error = "Gagal menyimpan: ${response.message()}")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isSaving = false, error = "Koneksi terputus") // REQ-17 [cite: 667]
            }
        }
    }
}
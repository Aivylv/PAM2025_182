package com.example.safeguard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.modeldata.Item
import com.example.safeguard.network.ItemApiService
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class DetailUiState(
    val item: Item = Item(0, "", "", null, "Disimpan", "", "", 0, 0),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = ""
)

class EditItemViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemApiService: ItemApiService
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    var uiState by mutableStateOf(DetailUiState())
        private set

    init {
        fetchItemDetails()
    }

    fun fetchItemDetails() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, isError = false)
            try {
                val item = itemApiService.getItem(itemId)
                uiState = uiState.copy(item = item, isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, isError = true, errorMessage = "Gagal memuat: ${e.message}")
            }
        }
    }

    fun updateItemData(newName: String, newCondition: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val currentItem = uiState.item
                // Pastikan receiver tidak null saat dikirim
                val updatedItem = currentItem.copy(
                    item_name = newName,
                    condition = newCondition,
                    receiver = currentItem.receiver ?: ""
                )
                val response = itemApiService.updateItem(itemId, updatedItem)
                if (response.isSuccessful) {
                    fetchItemDetails()
                    onSuccess()
                } else {
                    onError("Gagal: ${response.message()} (${response.code()})")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

    fun returnItem(receiverName: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val currentItem = uiState.item
                val updatedItem = currentItem.copy(
                    status = "Dikembalikan",
                    receiver = receiverName
                )

                val response = itemApiService.updateItem(itemId, updatedItem)

                if (response.isSuccessful) {
                    fetchItemDetails()
                    onSuccess()
                } else {
                    val errorMsg = when(response.code()) {
                        400 -> "Data tidak lengkap (Cek Backend)"
                        500 -> "Server Error (Cek Database)"
                        else -> "Gagal update: ${response.code()}"
                    }
                    onError(errorMsg)
                }
            } catch (e: HttpException) {
                onError("Koneksi ditolak: ${e.message}")
            } catch (e: Exception) {
                onError("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun deleteItem(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = itemApiService.deleteItem(itemId)
                if (response.isSuccessful) onSuccess()
            } catch (e: Exception) { /* Handle error */ }
        }
    }
}
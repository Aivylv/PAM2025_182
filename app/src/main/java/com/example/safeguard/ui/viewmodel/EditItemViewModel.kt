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

data class DetailUiState(
    val item: Item = Item(0, "", "", null, "Disimpan", "", "", 0, 0), // Default kosong
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
                uiState = uiState.copy(
                    isLoading = false,
                    isError = true,
                    errorMessage = "Gagal memuat data: ${e.message}"
                )
            }
        }
    }

    fun updateStatus(status: String, receiver: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val updatedItem = uiState.item.copy(status = status, receiver = receiver)
                val response = itemApiService.updateItem(itemId, updatedItem)
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) { /* Handle error */ }
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
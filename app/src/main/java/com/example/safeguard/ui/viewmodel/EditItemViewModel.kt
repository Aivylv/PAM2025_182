package com.example.safeguard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.modeldata.Item
import com.example.safeguard.network.ItemApiService
import kotlinx.coroutines.launch

class EditItemViewModel(private val itemApiService: ItemApiService) : ViewModel() {
    var itemState by mutableStateOf(Item(item_name = "", condition = "", status = "", user_id = 0, patient_id = 0))
        private set

    // REQ-18: Memfasilitasi pengubahan atribut data barang
    fun updateStatus(newStatus: String, receiver: String, onSuccess: () -> Unit) {
        // REQ-330: Validasi pengisian nama penerima jika status "Dikembalikan"
        if (newStatus == "Dikembalikan" && receiver.isBlank()) {
            return // Tampilkan error di UI jika perlu
        }

        viewModelScope.launch {
            try {
                val updatedItem = itemState.copy(status = newStatus, receiver = receiver)
                itemState.item_id?.let { id ->
                    val response = itemApiService.updateItem(id, updatedItem)
                    if (response.isSuccessful) onSuccess()
                }
            } catch (e: Exception) { /* Handle error */ }
        }
    }

    // REQ-270: Eksekusi penghapusan permanen
    fun deleteItem(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                itemState.item_id?.let { id ->
                    val response = itemApiService.deleteItem(id)
                    if (response.isSuccessful) onSuccess()
                }
            } catch (e: Exception) { /* Handle error */ }
        }
    }
}
package com.example.safeguard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.modeldata.Item
import com.example.safeguard.network.ItemApiService
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface DashboardUiState {
    data class Success(val items: List<Item>) : DashboardUiState
    object Error : DashboardUiState
    object Loading : DashboardUiState
}

class DashboardViewModel(private val itemApiService: ItemApiService) : ViewModel() {
    var dashboardUiState: DashboardUiState by mutableStateOf(DashboardUiState.Loading)
        private set

    init {
        getItems() // Panggil load data awal
    }

    //REQ-10 (Search) dan REQ-6 (Read All) sekaligus
    fun getItems(query: String = "") {
        viewModelScope.launch {
            dashboardUiState = DashboardUiState.Loading
            dashboardUiState = try {
                val listResult = if (query.isBlank()) {
                    itemApiService.getItems() // Ambil semua jika query kosong
                } else {
                    itemApiService.searchItems(query) // Cari jika ada query
                }
                DashboardUiState.Success(listResult)
            } catch (e: IOException) {
                DashboardUiState.Error
            } catch (e: Exception) {
                DashboardUiState.Error
            }
        }
    }
}
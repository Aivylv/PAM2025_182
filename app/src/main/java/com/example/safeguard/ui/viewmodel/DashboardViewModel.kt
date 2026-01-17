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

    // State untuk Search Bar
    var searchQuery by mutableStateOf("")
        private set

    init {
        getDashboardUiState()
    }

    fun getDashboardUiState() {
        viewModelScope.launch {
            dashboardUiState = DashboardUiState.Loading
            dashboardUiState = try {
                val items = if (searchQuery.isBlank()) {
                    itemApiService.getItems()
                } else {
                    itemApiService.searchItems(searchQuery)
                }
                DashboardUiState.Success(items)
            } catch (e: IOException) {
                DashboardUiState.Error
            } catch (e: Exception) {
                DashboardUiState.Error
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        getDashboardUiState()
    }
}
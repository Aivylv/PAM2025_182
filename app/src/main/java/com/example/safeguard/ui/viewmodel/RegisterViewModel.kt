package com.example.safeguard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.network.AuthApiService
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel(private val apiService: AuthApiService) : ViewModel() {
    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun updateUiState(name: String? = null, email: String? = null, pass: String? = null) {
        uiState = uiState.copy(
            name = name ?: uiState.name,
            email = email ?: uiState.email,
            password = pass ?: uiState.password,
            errorMessage = null
        )
    }

    fun registerUser(onSuccess: () -> Unit) {
        if (uiState.name.isBlank() || uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Semua kolom wajib diisi")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val response = apiService.register(uiState.name, uiState.email, uiState.password)
                if (response.success) {
                    onSuccess()
                } else {
                    uiState = uiState.copy(isLoading = false, errorMessage = response.message)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = "Koneksi Gagal")
            }
        }
    }
}
package com.example.safeguard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.network.AuthApiService
import com.example.safeguard.repository.UserPreferences
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

class LoginViewModel(
    private val apiService: AuthApiService,
    private val userPreferences: UserPreferences
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email, errorMessage = null)
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password, errorMessage = null)
    }

    fun login(onSuccess: () -> Unit) {
        // REQ-2: Validasi kolom tidak boleh kosong
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Email dan Password wajib diisi")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                // REQ-3: Verifikasi kredensial ke MySQL via API
                val response = apiService.login(uiState.email, uiState.password)
                if (response.success && response.token != null) {
                    // REQ-4: Simpan sesi login
                    userPreferences.saveSession(response.token, response.user?.user_id.toString())
                    uiState = uiState.copy(isLoading = false, success = true)
                    onSuccess()
                } else {
                    uiState = uiState.copy(isLoading = false, errorMessage = response.message)
                }
            } catch (e: Exception) {
                // REQ-344: Penanganan koneksi lambat/terputus
                uiState = uiState.copy(isLoading = false, errorMessage = "Gagal terhubung ke server")
            }
        }
    }
}
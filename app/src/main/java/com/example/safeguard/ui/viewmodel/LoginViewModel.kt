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

    fun login(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val response = apiService.login(uiState.email, uiState.password)

                if (response.success) {

                    //simpan session token & user_id
                    userPreferences.saveSession(
                        response.token ?: "",
                        response.user?.user_id.toString()
                    )

                    uiState = uiState.copy(isLoading = false, success = true)
                    onLoginSuccess()
                } else {
                    uiState = uiState.copy(isLoading = false, errorMessage = response.message)
                }
            } catch (e: Exception) {
                //tampilkan pesan error asli untuk debugging
                uiState = uiState.copy(isLoading = false, errorMessage = "Error: ${e.message}")
            }
        }
    }
}
package com.example.safeguard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeguard.network.AuthApiService
import com.example.safeguard.repository.UserPreferences
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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
    fun validateInput(): Boolean {
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Email dan Password tidak boleh kosong")
            return false
        }
        return true
    }
    fun login(onLoginSuccess: () -> Unit) {
        if (!validateInput()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val response = apiService.login(uiState.email, uiState.password)

                if (response.success) {

                    userPreferences.saveSession(
                        response.token ?: "",
                        response.user?.user_id.toString()
                    )

                    uiState = uiState.copy(isLoading = false, success = true)
                    onLoginSuccess()
                } else {
                    uiState = uiState.copy(isLoading = false, errorMessage = "Email atau Password salah")
                }
            } catch (e: HttpException) {
                val errorMsg = when (e.code()) {
                    400 -> "Email atau Password salah"
                    404, 401 -> "Email atau Password salah"
                    else -> "Gagal terhubung ke server (${e.code()})"
                }
                uiState = uiState.copy(isLoading = false, errorMessage = errorMsg)
            } catch (e: IOException) {
                uiState = uiState.copy(isLoading = false, errorMessage = "Koneksi internet bermasalah")
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = "Terjadi kesalahan: ${e.message}")
            }
        }
    }
}
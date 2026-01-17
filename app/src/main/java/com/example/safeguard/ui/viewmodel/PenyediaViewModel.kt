package com.example.safeguard.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.safeguard.SafeGuardApplication

object PenyediaViewModel {
    val Factory = viewModelFactory {
        // Initializer khusus LoginViewModel
        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            LoginViewModel(
                apiService = aplikasi.container.authApiService,
                userPreferences = aplikasi.container.userPreferences
            )
        }

        // Initializer khusus DashboardViewModel
        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            DashboardViewModel(
                itemApiService = aplikasi.container.itemApiService
            )
        }

        // Initializer khusus TambahBarangViewModel (Tambahkan ini agar tidak error di PetaNavigasi)
        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            TambahBarangViewModel(
                itemApiService = aplikasi.container.itemApiService
            )
        }

        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            RegisterViewModel(
                apiService = aplikasi.container.authApiService
            )
        }
    }
}
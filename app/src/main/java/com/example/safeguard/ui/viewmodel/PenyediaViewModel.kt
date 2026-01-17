package com.example.safeguard.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.safeguard.SafeGuardApplication

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            LoginViewModel(
                apiService = aplikasi.container.authApiService,
                userPreferences = aplikasi.container.userPreferences
            )
        }

        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            DashboardViewModel(
                itemApiService = aplikasi.container.itemApiService
            )
        }

        // PERBAIKAN DI SINI: Tambahkan userPreferences
        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            TambahBarangViewModel(
                itemApiService = aplikasi.container.itemApiService,
                userPreferences = aplikasi.container.userPreferences // SUNTIKKAN INI
            )
        }

        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            RegisterViewModel(
                apiService = aplikasi.container.authApiService
            )
        }

        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            EditItemViewModel(
                itemApiService = aplikasi.container.itemApiService
            )
        }
    }
}
package com.example.safeguard.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
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

        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            TambahBarangViewModel(
                itemApiService = aplikasi.container.itemApiService,
                patientApiService = aplikasi.container.patientApiService,
                userPreferences = aplikasi.container.userPreferences
            )
        }

        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            RegisterViewModel(
                apiService = aplikasi.container.authApiService
            )
        }

        initializer {
            EditItemViewModel(
                this.createSavedStateHandle(),
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication).container.itemApiService
            )
        }

        initializer {
            val aplikasi = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SafeGuardApplication)
            PasienViewModel(
                patientApiService = aplikasi.container.patientApiService
            )
        }
    }
}
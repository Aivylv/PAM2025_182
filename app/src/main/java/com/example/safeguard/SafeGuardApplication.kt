package com.example.safeguard

import android.app.Application
import com.example.safeguard.repository.ContainerApp
import com.example.safeguard.repository.DefaultContainerApp

class SafeGuardApplication : Application() {
    lateinit var container: ContainerApp

    override fun onCreate() {
        super.onCreate()
        container = DefaultContainerApp(this)
    }
}
package com.example.safeguard.ui.navigation

interface DestinasiNavigasi {
    val route: String
    val titleRes: String
}

object DestinasiLogin : DestinasiNavigasi {
    override val route = "login"
    override val titleRes = "Login Petugas"
}

object DestinasiDashboard : DestinasiNavigasi {
    override val route = "dashboard"
    override val titleRes = "Dashboard Barang Sitaan"
}
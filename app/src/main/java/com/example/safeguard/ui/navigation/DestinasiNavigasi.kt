package com.example.safeguard.ui.navigation

interface DestinasiNavigasi {
    val route: String
    val titleRes: String
}

object DestinasiLogin : DestinasiNavigasi {
    override val route = "login"
    override val titleRes = "Login Petugas"
}

object DestinasiRegister : DestinasiNavigasi {
    override val route = "register"
    override val titleRes = "Daftar Akun"
}

object DestinasiHome : DestinasiNavigasi {
    override val route = "dashboard"
    override val titleRes = "Dashboard Barang"
}

object DestinasiEntry : DestinasiNavigasi {
    override val route = "tambah_barang"
    override val titleRes = "Input Barang"
}

object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail_barang"
    override val titleRes = "Detail Barang"
}

object DestinasiOnboarding : DestinasiNavigasi {
    override val route = "onboarding"
    override val titleRes = "Selamat Datang"
}

object DestinasiHelp : DestinasiNavigasi {
    override val route = "bantuan"
    override val titleRes = "Pusat Bantuan"
}
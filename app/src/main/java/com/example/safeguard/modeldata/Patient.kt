package com.example.safeguard.modeldata

import kotlinx.serialization.Serializable
@Serializable
data class Patient(
    val patient_id: Int,          // PK
    val rm_number: String,       // Nomor Rekam Medis
    val name: String,            // Nama Lengkap Pasien
    val room_info: String? = null // Informasi Ruang Rawat
)
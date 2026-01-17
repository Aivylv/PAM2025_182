package com.example.safeguard.modeldata

import kotlinx.serialization.Serializable

//[cite: 403]
@Serializable
data class Item(
    val item_id: Int? = null,        // PK, Auto Inc [cite: 403]
    val item_name: String,           // Nama barang [cite: 403]
    val condition: String,           // Kondisi fisik [cite: 403]
    val photo_path: String? = null,  // Lokasi file foto [cite: 403]
    val status: String,              // 'Disimpan' atau 'Kembali' [cite: 403]
    val entry_date: String? = null,  // Waktu dicatat [cite: 403]
    val receiver: String? = null,    // Nama penerima saat diambil [cite: 403]
    val user_id: Int,                // FK ke tabel Users [cite: 403]
    val patient_id: Int,              // FK ke tabel Patients [cite: 403]
    val patient_name: String? = null,
    val rm_number: String? = null
)
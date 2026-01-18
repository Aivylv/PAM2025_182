package com.example.safeguard.modeldata

import kotlinx.serialization.Serializable

//[cite: 403]
@Serializable
data class Item(
    val item_id: Int? = null,        // PK, Auto Inc
    val item_name: String,
    val condition: String,
    val photo_path: String? = null,
    val status: String,
    val entry_date: String? = null,
    val receiver: String? = null,
    val user_id: Int,                //FK ke tabel Users
    val patient_id: Int,              //FK ke tabel Patients
    val patient_name: String? = null,
    val rm_number: String? = null,
    val return_date: String? = null
)
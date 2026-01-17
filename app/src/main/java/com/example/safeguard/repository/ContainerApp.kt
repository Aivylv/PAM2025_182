package com.example.safeguard.repository

import android.content.Context
import com.example.safeguard.network.AuthApiService
import com.example.safeguard.network.ItemApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface ContainerApp {
    val userPreferences: UserPreferences
    val authApiService: AuthApiService
    val itemApiService: ItemApiService
}

class DefaultContainerApp(private val context: Context) : ContainerApp {
    // Ganti URL ini dengan IP lokal komputer Anda atau domain server MySQL Anda
    private val baseUrl = "http://10.0.2.2:3000/api/"

    override val userPreferences = UserPreferences(context)

    private val json = Json { ignoreUnknownKeys = true }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    override val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    override val itemApiService: ItemApiService by lazy {
        retrofit.create(ItemApiService::class.java)
    }
}
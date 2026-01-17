package com.example.safeguard.repository

import android.content.Context
import com.example.safeguard.network.AuthApiService
import com.example.safeguard.network.ItemApiService
import com.example.safeguard.network.PatientApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface ContainerApp {
    val userPreferences: UserPreferences
    val authApiService: AuthApiService
    val itemApiService: ItemApiService
    val patientApiService: PatientApiService
}

class DefaultContainerApp(private val context: Context) : ContainerApp {
    private val baseUrl = "http://10.0.2.2:3000/api/"

    override val userPreferences = UserPreferences(context)

    private val json = Json { ignoreUnknownKeys = true }

    //AuthInterceptor
    //mengambil token dari DataStore dan menempelkannya ke Header
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()

        //Ambil token secara blocking (Synchronous) karena Interceptor berjalan di background thread
        val token = runBlocking {
            userPreferences.authToken.first()
        }

        val requestBuilder = original.newBuilder()

        //Jika token ada, tambahkan ke Header Authorization
        if (!token.isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
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

    override val patientApiService: PatientApiService by lazy {
        retrofit.create(PatientApiService::class.java)
    }
}
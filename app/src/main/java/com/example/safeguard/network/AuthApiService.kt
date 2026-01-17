package com.example.safeguard.network

import com.example.safeguard.modeldata.LoginResponse
import com.example.safeguard.modeldata.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {

    //login
    // Backend: authController.login -> Returns { token, user }
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    //register
    // Backend: authController.register -> Membutuhkan 'role' & Returns { data: {id} }
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("full_name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String = "officer" //esuai SRS & Controller
    ): RegisterResponse
}
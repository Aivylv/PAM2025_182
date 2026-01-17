package com.example.safeguard.network

import com.example.safeguard.modeldata.Patient
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PatientApiService {
    @GET("patients")
    suspend fun getPatients(): List<Patient>

    @POST("patients")
    suspend fun createPatient(@Body patient: Patient): retrofit2.Response<Void>

    @PUT("patients/{id}")
    suspend fun updatePatient(@Path("id") id: Int, @Body patient: Patient): retrofit2.Response<Void>

    @DELETE("patients/{id}")
    suspend fun deletePatient(@Path("id") id: Int): retrofit2.Response<Void>
}
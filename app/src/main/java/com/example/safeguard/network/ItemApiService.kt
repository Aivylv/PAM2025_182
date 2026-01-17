package com.example.safeguard.network

import com.example.safeguard.modeldata.Item
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemApiService {
    @GET("items")
    suspend fun getItems(): List<Item>

    // Search
    @GET("items")
    suspend fun searchItems(@Query("keyword") keyword: String): List<Item>

    @POST("items")
    suspend fun postItem(@Body item: Item): retrofit2.Response<Void>

    @PUT("items/{id}")
    suspend fun updateItem(@Path("id") id: Int, @Body item: Item): retrofit2.Response<Void>

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: Int): retrofit2.Response<Void>
}
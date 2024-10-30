package com.example.myapplication

import com.example.myapplication.model.RandomUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    fun getUsers(
        @Query("results") results: Int = 10
    ): Call<RandomUserResponse>
}

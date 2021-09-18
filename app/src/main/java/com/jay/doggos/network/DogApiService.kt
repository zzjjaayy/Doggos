package com.jay.doggos.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://dog.ceo/api/breeds/list/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface DogApiService {
    @GET("all")
    suspend fun getBreeds() : String
}

object DogApi {
    val retrofitService : DogApiService by lazy {
        retrofit.create(DogApiService::class.java)
    }
}


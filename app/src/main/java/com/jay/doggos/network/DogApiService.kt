package com.jay.doggos.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Moshi is used to
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private const val BASE_URL = "https://dog.ceo/api/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DogApiService {
    @GET("breeds/list/all")
    suspend fun getBreeds() : String

    @GET("breed/{breed}/images/random/10")
    suspend fun getCurrentBreed(@Path("breed") breed : String) : String

    @GET("breeds/image/random/10")
    suspend fun getRandom() : String
}

object DogApi {
    val retrofitService : DogApiService by lazy {
        retrofit.create(DogApiService::class.java)
    }
}


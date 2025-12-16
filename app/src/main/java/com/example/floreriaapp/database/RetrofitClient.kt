package com.example.floreriaapp.database

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Nueva URL Base actualizada
    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:WfvsdcVP/"

    val instance: FlorApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(FlorApiService::class.java)
    }
}

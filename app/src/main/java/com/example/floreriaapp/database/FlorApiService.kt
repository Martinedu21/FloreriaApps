package com.example.floreriaapp.database

import com.example.floreriaapp.Flor
import retrofit2.Call
import retrofit2.http.GET

interface FlorApiService {
    // Endpoint actualizado: flor
    @GET("flor") 
    fun obtenerFlores(): Call<List<Flor>>
}

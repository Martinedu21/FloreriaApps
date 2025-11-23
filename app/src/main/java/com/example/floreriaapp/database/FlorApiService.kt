package com.example.floreriaapp.database

import com.example.floreriaapp.Flor
import retrofit2.Call
import retrofit2.http.GET

interface FlorApiService {
    // Endpoint proporcionado: producto
    @GET("producto") 
    fun obtenerFlores(): Call<List<Flor>>
}

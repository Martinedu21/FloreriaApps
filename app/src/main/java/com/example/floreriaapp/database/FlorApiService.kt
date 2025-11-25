package com.example.floreriaapp.database

import com.example.floreriaapp.model.Flor
import retrofit2.Call
import retrofit2.http.GET

// ==================================================================================
// INTERFAZ FLORAPISERVICE (DEFINICIÓN DE ENDPOINTS)
// ==================================================================================
// Aquí definimos CÓMO pedirle datos a la Base de Datos Externa.
// Cada función representa una operación (leer, guardar, actualizar) en el servidor.
// Retrofit usará esta interfaz para generar el código real de conexión.
// ==================================================================================
interface FlorApiService {

    // [OBTENER FLORES]
    // @GET("producto"): Indica que haremos una petición de tipo GET (lectura)
    // a la dirección BASE_URL + "producto".
    //
    // Retorna un objeto Call<List<Flor>>, que es una promesa de que
    // el servidor nos responderá con una lista de objetos Flor.
    @GET("producto")
    fun obtenerFlores(): Call<List<Flor>>
}

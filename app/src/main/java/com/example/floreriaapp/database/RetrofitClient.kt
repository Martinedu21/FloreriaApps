package com.example.floreriaapp.database

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ==================================================================================
// CLASE RETROFITCLIENT (CONEXIÓN BASE DE DATOS EXTERNA - API REST)
// ==================================================================================
// Esta clase es responsable de crear y configurar la instancia de Retrofit.
// Retrofit es la librería que usamos para conectar la App con un servidor externo (Internet).
// Actúa como un patrón Singleton (object) para usar la misma conexión en toda la app.
// ==================================================================================
object RetrofitClient {
    
    // [URL BASE]
    // Es la dirección principal del servidor donde está alojada nuestra Base de Datos Externa.
    // Todos los pedidos de datos (endpoints) se harán a partir de esta dirección.
    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:9hVHF-B8/"

    // [INSTANCIA DEL SERVICIO]
    // Creamos el objeto que nos permitirá llamar a las funciones definidas en FlorApiService.
    // Usamos 'lazy' para que la conexión solo se cree cuando realmente la necesitemos (ahorra recursos).
    val instance: FlorApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // Le decimos a Retrofit dónde conectarse
            .addConverterFactory(GsonConverterFactory.create()) // [CONVERTIDOR JSON] Traduce automáticamente la respuesta JSON del servidor a objetos Kotlin.
            .build()

        retrofit.create(FlorApiService::class.java) // Crea la implementación de nuestra interfaz
    }
}

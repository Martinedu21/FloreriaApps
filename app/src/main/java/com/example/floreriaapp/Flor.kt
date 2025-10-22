package com.example.floreriaapp

// Data class para representar una flor
// Los data class en Kotlin se usan para guardar datos de manera simple y automática proporciona
// funciones útiles como toString(), equals(), hashCode() y copy().
data class Flor(
    val nombre: String,      // Nombre de la flor (por ejemplo: "Rosa Roja")
    val precio: Int,         // Precio de la flor en pesos (por ejemplo: 1500)
    val imagenResId: Int     // ID del recurso de la imagen de la flor (por ejemplo R.drawable.rosa)
)




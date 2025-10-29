package com.example.floreriaapp.model

data class Flor(

    // Propiedad que guarda el nombre de la flor.
    //
    val nombre: String,

    // Propiedad que describe la flor
    val descripcion: String,

    // Propiedad que guarda el precio de la flor en pesos.

    val precio: Int,

    // Propiedad que almacena el ID del recurso de imagen asociado a la flor.

    val imagenResId: Int
)

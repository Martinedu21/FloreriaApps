package com.example.floreriaapp.model

import java.io.Serializable

data class FlorEnCarrito(
    val id: Int,
    val nombre: String,
    val precio: Int,
    val imagenResId: Int,
    var cantidad: Int = 1
) : Serializable

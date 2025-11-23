package com.example.floreriaapp.model

import android.content.Context
import java.io.Serializable

data class FlorEnCarrito(
    val id: Int,
    val nombre: String,
    val precio: Int,
    val imagenNombre: String, // Cambiado de imagenResId a imagenNombre
    var cantidad: Int = 1
) : Serializable {
    
    val imagenResId: Int
        get() = 0

    fun getImagenResId(context: Context): Int {
        return context.resources.getIdentifier(imagenNombre, "drawable", context.packageName)
    }
}

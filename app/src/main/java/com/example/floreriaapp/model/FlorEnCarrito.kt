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
    
    // Propiedad auxiliar para controlar el contador de eliminación en la vista
    var cantidadAEliminar: Int = 1

    val imagenResId: Int
        get() = 0

    fun getImagenResId(context: Context): Int {
        return context.resources.getIdentifier(imagenNombre, "drawable", context.packageName)
    }
}

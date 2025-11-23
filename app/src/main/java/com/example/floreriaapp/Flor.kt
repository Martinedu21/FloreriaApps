package com.example.floreriaapp

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Data class para representar una flor
// Se anota con @Parcelize para que se pueda pasar entre actividades.
@Parcelize
data class Flor(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("precio")
    val precio: Int,

    // Mapeamos el campo "imagen" de la API a nuestra variable imagenNombre
    @SerializedName("imagen")
    val imagenNombre: String
) : Parcelable {
    
    val imagenResId: Int
        get() = 0 

    // Función helper para obtener el ID del recurso dinámicamente basado en el nombre
    fun getImagenResId(context: Context): Int {
        // Intentamos obtener el recurso. Si la API envía una URL completa o algo raro,
        // esto podría fallar si no limpiamos el nombre, pero asumimos que envía el nombre del recurso (ej: "rosa")
        return context.resources.getIdentifier(imagenNombre, "drawable", context.packageName)
    }
}

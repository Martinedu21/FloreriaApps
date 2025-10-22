package com.example.floreriaapp

// Importaciones necesarias para trabajar con Views y RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter para mostrar una lista de flores en un RecyclerView
// Recibe una lista de flores y una función que se ejecuta al hacer click en un item
class FlorAdapter(
    private val flores: List<Flor>,              // Lista de flores a mostrar
    private val onItemClick: (Flor) -> Unit     // Callback que se ejecuta al tocar una flor
) : RecyclerView.Adapter<FlorAdapter.FlorViewHolder>() {

    // ViewHolder: clase interna que contiene las referencias a los elementos del item
    inner class FlorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFlor: ImageView = itemView.findViewById(R.id.imgFlor)          // Imagen de la flor
        val txtNombreFlor: TextView = itemView.findViewById(R.id.txtNombreFlor) // Nombre de la flor
        val txtPrecioFlor: TextView = itemView.findViewById(R.id.txtPrecioFlor) // Precio de la flor
    }

    // Crea la vista de cada item y devuelve un ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flor, parent, false) // Infla el layout del item
        return FlorViewHolder(view) // Retorna un ViewHolder con la vista inflada
    }

    // Asocia los datos de la flor con los elementos del item
    override fun onBindViewHolder(holder: FlorViewHolder, position: Int) {
        val flor = flores[position]                     // Obtenemos la flor de la posición actual
        holder.imgFlor.setImageResource(flor.imagenResId) // Asigna la imagen
        holder.txtNombreFlor.text = flor.nombre          // Asigna el nombre
        holder.txtPrecioFlor.text = "$${flor.precio}"   // Asigna el precio con formato $

        // Configura el click en el item: ejecuta la función pasada al adapter
        holder.itemView.setOnClickListener { onItemClick(flor) }
    }

    // Devuelve la cantidad de items que tiene la lista
    override fun getItemCount() = flores.size
}



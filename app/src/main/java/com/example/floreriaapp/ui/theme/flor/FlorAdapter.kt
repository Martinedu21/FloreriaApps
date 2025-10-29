package com.example.floreriaapp.ui.theme.flor
// Paquete que agrupa las clases relacionadas con la pantalla de listado de flores.

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.model.Flor

// ---------------------------------------------------------------------------
// Adapter para mostrar una lista de flores en un RecyclerView.
// Este adaptador recibe:
//   - Una lista de objetos Flor.
//   - Una función (callback) que se ejecuta cuando se presiona el botón "Agregar al carrito".
// ---------------------------------------------------------------------------
class FlorAdapter(
    private val flores: List<Flor>,             // Lista de flores a mostrar
    private val onAgregarClick: (Flor) -> Unit  // Callback que se ejecuta al tocar el botón "Agregar"
) : RecyclerView.Adapter<FlorAdapter.FlorViewHolder>() {

    // -----------------------------------------------------------------------
    // ViewHolder: clase interna que guarda las referencias a los componentes
    // de la vista (imagen, nombre, descripción, precio, botón...).
    // Permite acceder a ellos sin tener que buscarlos repetidamente,
    // mejorando el rendimiento del RecyclerView.
    // -----------------------------------------------------------------------
    inner class FlorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFlor: ImageView = itemView.findViewById(R.id.imgFlor)                   // Imagen de la flor
        val txtNombreFlor: TextView = itemView.findViewById(R.id.txtNombreFlor)        // Nombre de la flor
        val txtDescripcionFlor: TextView = itemView.findViewById(R.id.txtDescripcionFlor) // Descripción de la flor
        val txtPrecioFlor: TextView = itemView.findViewById(R.id.txtPrecioFlor)        // Precio de la flor
        val btnAgregarCarrito: Button = itemView.findViewById(R.id.btnAgregarCarrito)  // Botón "Agregar al carrito"
    }

    // -----------------------------------------------------------------------
    // Crea la vista de cada ítem de la lista.
    // -----------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flor, parent, false) // "Inflar" = convertir el XML en una vista real
        return FlorViewHolder(view)
    }

    // -----------------------------------------------------------------------
    // Vincula (asocia) los datos de una flor con los elementos visuales del ítem.
    // Aquí se rellenan los TextView y ImageView con la información de la flor.
    // -----------------------------------------------------------------------
    override fun onBindViewHolder(holder: FlorViewHolder, position: Int) {
        val flor = flores[position] // Obtiene la flor correspondiente a esta posición

        // Asigna los datos a los elementos del layout
        holder.imgFlor.setImageResource(flor.imagenResId)
        holder.txtNombreFlor.text = flor.nombre
        holder.txtDescripcionFlor.text = flor.descripcion
        holder.txtPrecioFlor.text = "$${flor.precio}"

        // Configura el botón "Agregar al carrito"
        // Cuando el usuario lo toca, se ejecuta la función que recibió el adapter.
        // Esto permite manejar la acción desde la Activity o Fragment que use el adaptador.
        holder.btnAgregarCarrito.setOnClickListener {
            onAgregarClick(flor)
        }
    }

    // -----------------------------------------------------------------------
    // Devuelve el número total de elementos en la lista.
    // El RecyclerView usa este valor para saber cuántos ítems mostrar.
    // -----------------------------------------------------------------------
    override fun getItemCount() = flores.size
}

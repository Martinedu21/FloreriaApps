package com.example.floreriaapp.ui.theme.carrito
// Paquete donde se encuentra este adaptador, asociado a la interfaz del carrito.

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.model.FlorEnCarrito

// ---------------------------------------------------------------------------
// Adaptador del RecyclerView que muestra la lista de flores en el carrito.
// Se encarga de "conectar" los datos (objetos FlorEnCarrito) con las vistas
// del layout item_carrito.xml.
// ---------------------------------------------------------------------------
class CarritoAdapter(initialCarrito: List<FlorEnCarrito>) :
    RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    // Lista mutable de flores en el carrito.
    // Se inicializa con los datos que se pasan desde la Activity.
    private val carrito: MutableList<FlorEnCarrito> = initialCarrito.toMutableList()

    // -----------------------------------------------------------------------
    // Clase interna que representa cada "ítem" visual del RecyclerView.
    // Contiene las referencias a los componentes visuales del layout
    // (imagen, nombre y precio).
    // -----------------------------------------------------------------------
    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFlor: ImageView = itemView.findViewById(R.id.imgFlorCarrito)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreFlorCarrito)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioFlorCarrito)
    }

    // -----------------------------------------------------------------------
    // Se llama cuando el RecyclerView necesita crear un nuevo ViewHolder.

    // -----------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    // -----------------------------------------------------------------------
    // Se llama por cada elemento de la lista para asignar sus datos a las vistas.
    // Aquí se "vinculan" los datos de FlorEnCarrito con el layout del ítem.
    // -----------------------------------------------------------------------
    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val flor = carrito[position]  // Obtiene el elemento actual de la lista

        // Asigna los datos del objeto FlorEnCarrito a las vistas
        holder.imgFlor.setImageResource(flor.imagenResId)
        holder.txtNombre.text = flor.nombre
        holder.txtPrecio.text = "$${flor.precio}"  // Muestra el precio con formato de dinero
    }

    // -----------------------------------------------------------------------
    // Devuelve la cantidad total de elementos en la lista del carrito.
    // El RecyclerView usa este valor para saber cuántos ítems mostrar.
    // -----------------------------------------------------------------------
    override fun getItemCount() = carrito.size

    // -----------------------------------------------------------------------
    // Permite actualizar el contenido del carrito cuando cambian los datos.
    // Se usa, por ejemplo, cuando se vacía o se modifica el carrito.
    // -----------------------------------------------------------------------
    fun setFlores(flores: List<FlorEnCarrito>) {
        this.carrito.clear()       // Limpia la lista actual
        this.carrito.addAll(flores) // Agrega los nuevos elementos
        notifyDataSetChanged()      // Notifica al RecyclerView que los datos cambiaron
    }

    // -----------------------------------------------------------------------
    // Calcula el total de precios de todas las flores en el carrito.
    // Este método puede ser usado por la Activity para mostrar el total.
    // -----------------------------------------------------------------------
    fun getTotal(): Int {
        return carrito.sumOf { it.precio }
    }
}

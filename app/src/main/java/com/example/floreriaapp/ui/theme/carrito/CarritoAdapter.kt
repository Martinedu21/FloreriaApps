package com.example.floreriaapp.ui.theme.carrito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.model.FlorEnCarrito
import java.text.NumberFormat
import java.util.Locale

class CarritoAdapter(
    initialCarrito: List<FlorEnCarrito>,
    private val onEliminarClick: (Int, Int) -> Unit // Callback: (id, cantidad)
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    private val carrito: MutableList<FlorEnCarrito> = initialCarrito.toMutableList()

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFlor: ImageView = itemView.findViewById(R.id.imgFlorCarrito)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreFlorCarrito)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioFlorCarrito)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidadFlorCarrito)
        
        // Controles para eliminar cantidad
        val btnMas: Button = itemView.findViewById(R.id.btnMasCarrito)
        val btnMenos: Button = itemView.findViewById(R.id.btnMenosCarrito)
        val txtCantidadEliminar: TextView = itemView.findViewById(R.id.txtCantidadEliminar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val flor = carrito[position]
        
        val imageResId = flor.getImagenResId(holder.itemView.context)
        if (imageResId != 0) {
            holder.imgFlor.setImageResource(imageResId)
        } else {
             holder.imgFlor.setImageResource(R.drawable.ic_launcher_foreground)
        }
        
        holder.txtNombre.text = flor.nombre
        
        val formatoChile = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        holder.txtPrecio.text = formatoChile.format(flor.precio)
        
        holder.txtCantidad.text = "En carrito: ${flor.cantidad}"
        
        // Lógica del contador de eliminación
        // Reiniciamos a 1 al bindear para evitar estados incorrectos al reciclar vistas
        flor.cantidadAEliminar = 1 
        holder.txtCantidadEliminar.text = flor.cantidadAEliminar.toString()
        
        holder.btnMas.setOnClickListener {
            // Tope máximo: cantidad actual en el carrito
            if (flor.cantidadAEliminar < flor.cantidad) {
                flor.cantidadAEliminar++
                holder.txtCantidadEliminar.text = flor.cantidadAEliminar.toString()
            }
        }

        holder.btnMenos.setOnClickListener {
            if (flor.cantidadAEliminar > 1) {
                flor.cantidadAEliminar--
                holder.txtCantidadEliminar.text = flor.cantidadAEliminar.toString()
            }
        }
        
        holder.btnEliminar.setOnClickListener {
            onEliminarClick(flor.id, flor.cantidadAEliminar)
        }
    }

    override fun getItemCount() = carrito.size

    fun setFlores(flores: List<FlorEnCarrito>) {
        this.carrito.clear()
        this.carrito.addAll(flores)
        notifyDataSetChanged()
    }

    fun getTotal(): Int {
        return carrito.sumOf { it.precio * it.cantidad }
    }
}

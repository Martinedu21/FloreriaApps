package com.example.floreriaapp.ui.theme.carrito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.model.FlorEnCarrito

class CarritoAdapter(initialCarrito: List<FlorEnCarrito>) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    private val carrito: MutableList<FlorEnCarrito> = initialCarrito.toMutableList()

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFlor: ImageView = itemView.findViewById(R.id.imgFlorCarrito)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreFlorCarrito)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioFlorCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val flor = carrito[position]
        holder.imgFlor.setImageResource(flor.imagenResId)
        holder.txtNombre.text = flor.nombre
        holder.txtPrecio.text = "$${flor.precio}"
    }

    override fun getItemCount() = carrito.size

    fun setFlores(flores: List<FlorEnCarrito>) {
        this.carrito.clear()
        this.carrito.addAll(flores)
        notifyDataSetChanged()
    }

    fun getTotal(): Int {
        return carrito.sumOf { it.precio }
    }
}
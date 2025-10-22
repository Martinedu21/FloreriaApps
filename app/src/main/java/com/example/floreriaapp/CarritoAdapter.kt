package com.example.floreriaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarritoAdapter(private val carrito: List<Flor>) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

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
}

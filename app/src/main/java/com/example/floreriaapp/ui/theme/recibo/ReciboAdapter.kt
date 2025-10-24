package com.example.floreriaapp.ui.theme.recibo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.model.Flor

class ReciboAdapter(private val flores: List<Flor>) : RecyclerView.Adapter<ReciboAdapter.ReciboViewHolder>() {

    inner class ReciboViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFlor: ImageView = itemView.findViewById(R.id.imgFlorCarrito)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreFlorCarrito)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioFlorCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReciboViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return ReciboViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReciboViewHolder, position: Int) {
        val flor = flores[position]
        holder.imgFlor.setImageResource(flor.imagenResId)
        holder.txtNombre.text = flor.nombre
        holder.txtPrecio.text = "$${flor.precio}"
    }

    override fun getItemCount() = flores.size
}
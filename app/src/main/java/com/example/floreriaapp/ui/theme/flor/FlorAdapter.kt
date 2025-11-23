package com.example.floreriaapp.ui.theme.flor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.model.Flor
import java.text.NumberFormat
import java.util.Locale

class FlorAdapter(
    private val flores: List<Flor>,
    private val onAgregarClick: (Flor) -> Unit
) : RecyclerView.Adapter<FlorAdapter.FlorViewHolder>() {

    inner class FlorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFlor: ImageView = itemView.findViewById(R.id.imgFlor)
        val txtNombreFlor: TextView = itemView.findViewById(R.id.txtNombreFlor)
        val txtDescripcionFlor: TextView = itemView.findViewById(R.id.txtDescripcionFlor)
        val txtPrecioFlor: TextView = itemView.findViewById(R.id.txtPrecioFlor)
        val btnAgregarCarrito: Button = itemView.findViewById(R.id.btnAgregarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flor, parent, false)
        return FlorViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlorViewHolder, position: Int) {
        val flor = flores[position]
        holder.imgFlor.setImageResource(flor.imagenResId)
        holder.txtNombreFlor.text = flor.nombre
        holder.txtDescripcionFlor.text = flor.descripcion
        
        // Formato de moneda chilena
        val formatoChile = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        holder.txtPrecioFlor.text = formatoChile.format(flor.precio)

        holder.btnAgregarCarrito.setOnClickListener {
            onAgregarClick(flor)
        }
    }

    override fun getItemCount() = flores.size
}

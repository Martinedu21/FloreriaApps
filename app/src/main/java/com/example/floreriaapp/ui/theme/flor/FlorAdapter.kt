package com.example.floreriaapp.ui.theme.flor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.Flor
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
        
        // Nuevos elementos para el contador
        val btnMas: Button = itemView.findViewById(R.id.btnMas)
        val btnMenos: Button = itemView.findViewById(R.id.btnMenos)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flor, parent, false)
        return FlorViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlorViewHolder, position: Int) {
        val flor = flores[position]
        
        // Usar el nuevo método para obtener el ID del recurso basado en el nombre
        val imageResId = flor.getImagenResId(holder.itemView.context)
        if (imageResId != 0) {
            holder.imgFlor.setImageResource(imageResId)
        } else {
            // Imagen por defecto si no se encuentra el recurso
            holder.imgFlor.setImageResource(R.drawable.ic_launcher_foreground) 
        }

        holder.txtNombreFlor.text = flor.nombre
        holder.txtDescripcionFlor.text = flor.descripcion
        
        // Formato de moneda chilena
        val formatoChile = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        holder.txtPrecioFlor.text = formatoChile.format(flor.precio)

        // Lógica del contador
        holder.txtCantidad.text = flor.cantidadSeleccionada.toString()

        holder.btnMas.setOnClickListener {
            flor.cantidadSeleccionada++
            holder.txtCantidad.text = flor.cantidadSeleccionada.toString()
        }

        holder.btnMenos.setOnClickListener {
            if (flor.cantidadSeleccionada > 1) {
                flor.cantidadSeleccionada--
                holder.txtCantidad.text = flor.cantidadSeleccionada.toString()
            }
        }

        holder.btnAgregarCarrito.setOnClickListener {
            onAgregarClick(flor)
        }
    }

    override fun getItemCount() = flores.size
}

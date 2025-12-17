package com.example.floreriaapp.ui.theme.flor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        holder.imgFlor.setImageResource(flor.getImagenResId(holder.itemView.context))
        holder.txtNombreFlor.text = flor.nombre
        holder.txtDescripcionFlor.text = flor.descripcion
        
        val formatoChile = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        holder.txtPrecioFlor.text = formatoChile.format(flor.precio)

        holder.txtCantidad.text = flor.cantidadSeleccionada.toString()

        holder.btnMas.setOnClickListener {
            flor.cantidadSeleccionada++
            holder.txtCantidad.text = flor.cantidadSeleccionada.toString()
        }

        holder.btnMenos.setOnClickListener {
            // Permitimos bajar hasta 0 si el usuario lo desea, pero no menos
            if (flor.cantidadSeleccionada > 0) {
                flor.cantidadSeleccionada--
                holder.txtCantidad.text = flor.cantidadSeleccionada.toString()
            }
        }

        holder.btnAgregarCarrito.setOnClickListener {
            if (flor.cantidadSeleccionada > 0) {
                onAgregarClick(flor)
            } else {
                Toast.makeText(holder.itemView.context, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = flores.size
}

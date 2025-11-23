package com.example.floreriaapp.ui.theme.recibo
// Paquete donde se agrupan las clases relacionadas con la pantalla del recibo o comprobante de compra.

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.Flor

// ---------------------------------------------------------------------------
// Adapter para mostrar una lista de flores en la pantalla del recibo.
// Recibe una lista de objetos Flor y los muestra en un RecyclerView.
// Reutiliza el mismo layout de los ítems del carrito (item_carrito.xml).
// ---------------------------------------------------------------------------
class ReciboAdapter(private val flores: List<Flor>) :
    RecyclerView.Adapter<ReciboAdapter.ReciboViewHolder>() {

    // -----------------------------------------------------------------------
    // ViewHolder: clase interna que mantiene las referencias a los elementos
    // del layout de cada ítem del RecyclerView (imagen, nombre y precio).
    // -----------------------------------------------------------------------
    inner class ReciboViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFlor: ImageView = itemView.findViewById(R.id.imgFlorCarrito)          // Imagen de la flor
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreFlorCarrito)   // Nombre de la flor
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioFlorCarrito)   // Precio de la flor
    }

    // -----------------------------------------------------------------------
    // Crea la vista de cada ítem a partir del layout XML "item_carrito".
    // Este método solo se llama cuando el RecyclerView necesita una nueva vista.
    // -----------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReciboViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return ReciboViewHolder(view)
    }

    // -----------------------------------------------------------------------
    // Asigna (vincula) los datos de la flor a las vistas del ViewHolder.
    // Este método se llama tantas veces como elementos haya en la lista.
    // -----------------------------------------------------------------------
    override fun onBindViewHolder(holder: ReciboViewHolder, position: Int) {
        val flor = flores[position]

        // Muestra los datos de cada flor en el recibo usando la carga dinámica de imagen
        val imageResId = flor.getImagenResId(holder.itemView.context)
        if (imageResId != 0) {
            holder.imgFlor.setImageResource(imageResId)
        } else {
            holder.imgFlor.setImageResource(R.drawable.ic_launcher_foreground)
        }
        
        holder.txtNombre.text = flor.nombre                // Nombre de la flor
        holder.txtPrecio.text = "$${flor.precio}"          // Precio formateado
    }

    // -----------------------------------------------------------------------
    // Devuelve el número total de ítems que tiene el RecyclerView.
    // -----------------------------------------------------------------------
    override fun getItemCount() = flores.size
}

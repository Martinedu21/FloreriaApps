package com.example.floreriaapp.ui.theme.recibo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.MainActivity
import com.example.floreriaapp.R
import com.example.floreriaapp.model.FlorEnCarrito
import com.example.floreriaapp.ui.theme.carrito.CarritoAdapter
import java.text.NumberFormat
import java.util.Locale

class ReciboActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo)

        val txtTotalPagado: TextView = findViewById(R.id.txtTotalPagado)
        val recyclerViewDetalle: RecyclerView = findViewById(R.id.recyclerViewDetalle)
        val btnVolverInicio: Button = findViewById(R.id.btnVolverInicio)

        // Recibir datos
        val itemsCompra = intent.getSerializableExtra("ITEMS_COMPRA") as? ArrayList<FlorEnCarrito> ?: arrayListOf()
        val totalCompra = intent.getIntExtra("TOTAL_COMPRA", 0)

        // Configurar RecyclerView (reutilizamos CarritoAdapter ya que muestra lo mismo)
        val adapter = CarritoAdapter(itemsCompra) { _, _ -> }
        recyclerViewDetalle.layoutManager = LinearLayoutManager(this)
        recyclerViewDetalle.adapter = adapter

        // [FORMATO MONEDA CLP EN RECIBO]
        // Muestra el total final pagado formateado como pesos chilenos (ej: $15.000).
        val formatoChile = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        txtTotalPagado.text = formatoChile.format(totalCompra)

        // Botón volver
        btnVolverInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}

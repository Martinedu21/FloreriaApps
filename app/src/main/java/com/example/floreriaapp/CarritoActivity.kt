package com.example.floreriaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarritoActivity : AppCompatActivity() {

    private lateinit var recyclerViewCarrito: RecyclerView
    private lateinit var txtTotal: TextView
    private lateinit var btnVaciar: Button
    private lateinit var btnComprar: Button
    private lateinit var adapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito)
        txtTotal = findViewById(R.id.txtTotal)
        btnVaciar = findViewById(R.id.btnVaciar)
        btnComprar = findViewById(R.id.btnComprar)

        adapter = CarritoAdapter(CartRepository.carrito)
        recyclerViewCarrito.adapter = adapter
        recyclerViewCarrito.layoutManager = LinearLayoutManager(this)

        actualizarTotal()

        btnVaciar.setOnClickListener {
            CartRepository.carrito.clear()
            adapter.notifyDataSetChanged()
            actualizarTotal()
            Toast.makeText(this, "Carrito vaciado", Toast.LENGTH_SHORT).show()
        }

        btnComprar.setOnClickListener {
            if (CartRepository.carrito.isNotEmpty()) {
                // Guarda la compra y limpia el carrito
                CartRepository.lastPurchase = CartRepository.carrito.toList()
                CartRepository.carrito.clear()

                // Notifica al adaptador y actualiza la UI
                adapter.notifyDataSetChanged()
                actualizarTotal()

                // Abre la pantalla de recibo
                val intent = Intent(this, ReciboActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarTotal()
        adapter.notifyDataSetChanged()
    }

    private fun actualizarTotal() {
        val total = CartRepository.carrito.sumOf { it.precio }
        txtTotal.text = "Total: $$total"
    }
}

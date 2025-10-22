package com.example.floreriaapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReciboActivity : AppCompatActivity() {

    private lateinit var recyclerViewRecibo: RecyclerView
    private lateinit var txtTotalRecibo: TextView
    private lateinit var adapter: ReciboAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo)

        recyclerViewRecibo = findViewById(R.id.recyclerViewRecibo)
        txtTotalRecibo = findViewById(R.id.txtTotalRecibo)

        // Usa la lista de la última compra del repositorio
        adapter = ReciboAdapter(CartRepository.lastPurchase)
        recyclerViewRecibo.adapter = adapter
        recyclerViewRecibo.layoutManager = LinearLayoutManager(this)

        actualizarTotal()
    }

    private fun actualizarTotal() {
        val total = CartRepository.lastPurchase.sumOf { it.precio }
        txtTotalRecibo.text = "Total: $$total"
    }
}

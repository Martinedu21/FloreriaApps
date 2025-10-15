package com.example.floreriaapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.NumberFormatException

class CarritoActivity : AppCompatActivity() {

    private lateinit var listViewCarrito: ListView
    private lateinit var txtTotal: TextView
    private var carrito = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        listViewCarrito = findViewById(R.id.listViewCarrito)
        txtTotal = findViewById(R.id.txtTotal)

        carrito = intent.getStringArrayListExtra("carrito") ?: arrayListOf()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, carrito)
        listViewCarrito.adapter = adapter

        // Calcular total
        var total = 0
        for (item in carrito) {
            try {
                val precioString = item.substringAfterLast('$')
                val precio = precioString.trim().toInt()
                total += precio
            } catch (e: NumberFormatException) {
                // Ignorar items que no tengan un precio válido
            }
        }

        txtTotal.text = "Total: $$total"
    }
}

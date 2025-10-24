package com.example.floreriaapp.ui.theme.carrito

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.database.DatabaseHelper
import com.example.floreriaapp.model.FlorEnCarrito
import com.example.floreriaapp.ui.theme.recibo.ReciboActivity

class CarritoActivity : AppCompatActivity() {

    private lateinit var recyclerViewCarrito: RecyclerView
    private lateinit var txtTotal: TextView
    private lateinit var btnVaciar: Button
    private lateinit var btnComprar: Button
    private lateinit var adapter: CarritoAdapter
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito)
        txtTotal = findViewById(R.id.txtTotal)
        btnVaciar = findViewById(R.id.btnVaciar)
        btnComprar = findViewById(R.id.btnComprar)

        db = DatabaseHelper(this)

        // Cargar productos del carrito desde SQLite
        adapter = CarritoAdapter(obtenerCarritoDesdeDB())
        recyclerViewCarrito.adapter = adapter
        recyclerViewCarrito.layoutManager = LinearLayoutManager(this)

        actualizarTotal()

        // Botón para vaciar el carrito
        btnVaciar.setOnClickListener {
            db.vaciarCarrito()
            adapter.setFlores(emptyList())
            actualizarTotal()
            Toast.makeText(this, "Carrito vaciado", Toast.LENGTH_SHORT).show()
        }

        // Botón para comprar
        btnComprar.setOnClickListener {
            if (adapter.itemCount > 0) {
                // Abrir ReciboActivity primero
                val intent = Intent(this, ReciboActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refrescar lista y total cuando se regrese al carrito
        adapter.setFlores(obtenerCarritoDesdeDB())
        actualizarTotal()
    }

    private fun obtenerCarritoDesdeDB(): List<FlorEnCarrito> {
        val lista = mutableListOf<FlorEnCarrito>()
        val cursor = db.obtenerCarrito()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                val imagen = cursor.getInt(cursor.getColumnIndexOrThrow("imagen"))
                lista.add(FlorEnCarrito(id, nombre, precio.toInt(), imagen))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    private fun actualizarTotal() {
        // Calcular total directamente desde SQLite
        val cursor = db.obtenerCarrito()
        var total = 0.0
        if (cursor.moveToFirst()) {
            do {
                total += cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
            } while (cursor.moveToNext())
        }
        cursor.close()
        txtTotal.text = "Total: $$total"
    }
}

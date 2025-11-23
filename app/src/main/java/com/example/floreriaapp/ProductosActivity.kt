package com.example.floreriaapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.database.DatabaseHelper
import com.example.floreriaapp.model.Flor
import com.example.floreriaapp.ui.theme.carrito.CarritoActivity
import com.example.floreriaapp.ui.theme.flor.FlorAdapter
import com.example.floreriaapp.ui.theme.formulario.FormularioActivity

class ProductosActivity : AppCompatActivity() {

    private lateinit var recyclerViewFlores: RecyclerView
    private val floresLista = listOf(
        Flor("Rosa", "Clásica y elegante, perfecta para cualquier ocasión.", 15000, R.drawable.rosa),
        Flor("Tulipán", "Colores vibrantes que alegran cualquier espacio.", 20000, R.drawable.tulipan),
        Flor("Orquídea", "Exótica y sofisticada, un regalo inolvidable.", 25000, R.drawable.orquidea),
        Flor("Girasol", "Irradia alegría y energía positiva.", 15000, R.drawable.girasol)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Nuestros Productos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Botón para volver atrás

        recyclerViewFlores = findViewById(R.id.recyclerViewFlores)
        val db = DatabaseHelper(this)

        // Lógica de inicialización de DB si está vacía
        val cursor = db.obtenerFlores()
        if (!cursor.moveToFirst()) {
            floresLista.forEach { flor ->
                db.agregarFlor(flor.nombre, flor.descripcion, flor.precio.toDouble(), flor.imagenResId)
            }
        }
        cursor.close()

        val adapter = FlorAdapter(floresLista) { flor ->
            db.agregarAlCarrito(flor.nombre, flor.precio.toDouble(), flor.imagenResId)
            Toast.makeText(this, "${flor.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        recyclerViewFlores.layoutManager = LinearLayoutManager(this)
        recyclerViewFlores.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Al pulsar la flecha de atrás o el título, volvemos al Inicio
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_productos -> true // Ya estamos aquí
            R.id.menu_formulario -> {
                startActivity(Intent(this, FormularioActivity::class.java))
                true
            }
            R.id.menu_carrito -> {
                startActivity(Intent(this, CarritoActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

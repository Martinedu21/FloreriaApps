package com.example.floreriaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewFlores: RecyclerView
    private lateinit var btnCarrito: Button
    private lateinit var btnFormulario: Button

    private val floresLista = listOf(
        Flor("Rosa", "Clásica y elegante, perfecta para cualquier ocasión.", 15000, R.drawable.rosa),
        Flor("Tulipán", "Colores vibrantes que alegran cualquier espacio.", 20000, R.drawable.tulipan),
        Flor("Orquídea", "Exótica y sofisticada, un regalo inolvidable.", 25000, R.drawable.orquidea),
        Flor("Girasol", "Irradia alegría y energía positiva.", 15000, R.drawable.girasol)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerViewFlores = findViewById(R.id.recyclerViewFlores)
        btnCarrito = findViewById(R.id.btnCarrito)
        btnFormulario = findViewById(R.id.btnFormulario)

        // Instanciar base de datos
        val db = DatabaseHelper(this)

        // Guardar flores en la base de datos solo si no existen
        val cursor = db.obtenerFlores()
        if (!cursor.moveToFirst()) {
            floresLista.forEach { flor ->
                db.agregarFlor(flor.nombre, flor.descripcion, flor.precio.toDouble(), flor.imagenResId)
            }
        }
        cursor.close()

        // Configurar adaptador del RecyclerView
        val adapter = FlorAdapter(floresLista) { flor ->
            db.agregarAlCarrito(flor.nombre, flor.precio.toDouble(), flor.imagenResId)
            Toast.makeText(this, "${flor.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        recyclerViewFlores.layoutManager = LinearLayoutManager(this)
        recyclerViewFlores.adapter = adapter

        // Botones
        btnCarrito.setOnClickListener { verCarrito() }
        btnFormulario.setOnClickListener { abrirFormulario() }
    }

    private fun verCarrito() {
        // Abrir CarritoActivity
        val intent = Intent(this, CarritoActivity::class.java)
        startActivity(intent)
    }

    private fun abrirFormulario() {
        val intent = Intent(this, FormularioActivity::class.java)
        startActivity(intent)
    }
}


package com.example.floreriaapp

// Importaciones necesarias para Android y RecyclerView
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Actividad principal de la app
class MainActivity : AppCompatActivity() {

    // Vistas de la interfaz
    private lateinit var recyclerViewFlores: RecyclerView
    private lateinit var btnCarrito: Button
    private lateinit var btnFormulario: Button

    // Lista de flores disponibles
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

        // Inicializar vistas
        recyclerViewFlores = findViewById(R.id.recyclerViewFlores)
        btnCarrito = findViewById(R.id.btnCarrito)
        btnFormulario = findViewById(R.id.btnFormulario)

        // Configurar adaptador del RecyclerView
        val adapter = FlorAdapter(floresLista) { flor ->
            CartRepository.carrito.add(flor)
            Toast.makeText(this, "${flor.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        // Configurar layout y adaptador del RecyclerView
        recyclerViewFlores.layoutManager = LinearLayoutManager(this)
        recyclerViewFlores.adapter = adapter

        // Configurar acciones de los botones
        btnCarrito.setOnClickListener { verCarrito() }
        btnFormulario.setOnClickListener { abrirFormulario() }
    }

    // Función para abrir CarritoActivity
    private fun verCarrito() {
        val intent = Intent(this, CarritoActivity::class.java)
        startActivity(intent)
    }

    // Función para abrir FormularioActivity
    private fun abrirFormulario() {
        val intent = Intent(this, FormularioActivity::class.java)
        startActivity(intent)
    }
}

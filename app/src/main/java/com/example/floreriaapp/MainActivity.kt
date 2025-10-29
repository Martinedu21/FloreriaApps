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

// MainActivity: pantalla principal de la app donde se muestran las flores
class MainActivity : AppCompatActivity() {

    // Referencias a las vistas de la UI
    private lateinit var recyclerViewFlores: RecyclerView  // RecyclerView para mostrar las flores
    private lateinit var btnCarrito: Button               // Botón para ir al carrito
    private lateinit var btnFormulario: Button            // Botón para abrir el formulario

    // Lista de flores inicial que se mostrará si la base de datos está vacía
    private val floresLista = listOf(
        Flor("Rosa", "Clásica y elegante, perfecta para cualquier ocasión.", 15000, R.drawable.rosa),
        Flor("Tulipán", "Colores vibrantes que alegran cualquier espacio.", 20000, R.drawable.tulipan),
        Flor("Orquídea", "Exótica y sofisticada, un regalo inolvidable.", 25000, R.drawable.orquidea),
        Flor("Girasol", "Irradia alegría y energía positiva.", 15000, R.drawable.girasol)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el layout principal
        setContentView(R.layout.activity_main)

        // -------------------------
        // CONFIGURAR TOOLBAR
        // -------------------------
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // Establece la Toolbar como ActionBar

        // -------------------------
        // INICIALIZAR VISTAS
        // -------------------------
        recyclerViewFlores = findViewById(R.id.recyclerViewFlores)
        btnCarrito = findViewById(R.id.btnCarrito)
        btnFormulario = findViewById(R.id.btnFormulario)

        // -------------------------
        // INICIALIZAR BASE DE DATOS
        // -------------------------
        val db = DatabaseHelper(this)

        // -------------------------
        // GUARDAR FLORES EN DB SI NO EXISTEN
        // -------------------------
        val cursor = db.obtenerFlores()  // Consulta todas las flores
        if (!cursor.moveToFirst()) {      // Si la tabla está vacía
            floresLista.forEach { flor -> // Agrega las flores iniciales
                db.agregarFlor(
                    flor.nombre,
                    flor.descripcion,
                    flor.precio.toDouble(),
                    flor.imagenResId
                )
            }
        }
        cursor.close() // Cierra el cursor para liberar recursos

        // -------------------------
        // CONFIGURAR ADAPTADOR DEL RECYCLERVIEW
        // -------------------------
        val adapter = FlorAdapter(floresLista) { flor ->
            // Callback: se ejecuta al hacer click en "Agregar al carrito"
            db.agregarAlCarrito(flor.nombre, flor.precio.toDouble(), flor.imagenResId)
            Toast.makeText(this, "${flor.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        // Configura el RecyclerView con un LinearLayout
        recyclerViewFlores.layoutManager = LinearLayoutManager(this)
        recyclerViewFlores.adapter = adapter

        // -------------------------
        // BOTONES
        // -------------------------
        // Botón para abrir el carrito
        btnCarrito.setOnClickListener { verCarrito() }

        // Botón para abrir el formulario
        btnFormulario.setOnClickListener { abrirFormulario() }
    }

    // -------------------------
    // FUNCIONES AUXILIARES
    // -------------------------

    // Abre la actividad del carrito
    private fun verCarrito() {
        val intent = Intent(this, CarritoActivity::class.java)
        startActivity(intent)
    }

    // Abre la actividad del formulario
    private fun abrirFormulario() {
        val intent = Intent(this, FormularioActivity::class.java)
        startActivity(intent)
    }
}



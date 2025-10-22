package com.example.floreriaapp

// Importaciones necesarias para Android y RecyclerView
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Actividad principal de la app
class MainActivity : AppCompatActivity() {

    // Vistas de la interfaz
    private lateinit var recyclerViewFlores: RecyclerView  // Lista de flores
    private lateinit var btnCarrito: Button               // Botón para abrir carrito
    private lateinit var btnFormulario: Button            // Botón para abrir formulario
    private var carrito = arrayListOf<String>()           // Lista que guarda los productos agregados

    // Lista de flores disponibles
    private val floresLista = listOf(
        Flor("Rosa", "Clásica y elegante, perfecta para cualquier ocasión.", 15000, R.drawable.rosa),
        Flor("Tulipán", "Colores vibrantes que alegran cualquier espacio.", 20000, R.drawable.tulipan),
        Flor("Orquídea", "Exótica y sofisticada, un regalo inolvidable.", 25000, R.drawable.orquidea),
        Flor("Girasol", "Irradia alegría y energía positiva.", 15000, R.drawable.girasol)
    )

    // Launcher para recibir resultados de CarritoActivity
    private val carritoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val nuevosCarrito = result.data?.getStringArrayListExtra("carrito")
            if (nuevosCarrito != null) {
                carrito = nuevosCarrito           // Actualiza carrito con los datos recibidos
                Toast.makeText(this, "Carrito actualizado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Asocia el layout

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Inicializar vistas
        recyclerViewFlores = findViewById(R.id.recyclerViewFlores)
        btnCarrito = findViewById(R.id.btnCarrito)
        btnFormulario = findViewById(R.id.btnFormulario) // Botón nuevo para abrir formulario

        // Configurar adaptador del RecyclerView con el nuevo evento de click
        val adapter = FlorAdapter(floresLista) { flor ->
            carrito.add("${flor.nombre} - $${flor.precio}") // Agrega flor al carrito
            Toast.makeText(this, "${flor.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        // Configurar layout y adaptador del RecyclerView
        recyclerViewFlores.layoutManager = LinearLayoutManager(this)
        recyclerViewFlores.adapter = adapter

        // Configurar acciones de los botones
        btnCarrito.setOnClickListener { verCarrito() }
        btnFormulario.setOnClickListener { abrirFormulario() } // Abre formulario
    }

    // Función para abrir CarritoActivity y pasarle el carrito actual
    private fun verCarrito() {
        val intent = Intent(this, CarritoActivity::class.java)
        intent.putStringArrayListExtra("carrito", carrito)
        carritoLauncher.launch(intent) // Lanza la actividad y espera resultado
    }

    // Función para abrir FormularioActivity
    private fun abrirFormulario() {
        val intent = Intent(this, FormularioActivity::class.java)
        startActivity(intent) // Lanza actividad sin esperar resultado
    }
}

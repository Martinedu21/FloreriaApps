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
import com.example.floreriaapp.database.RetrofitClient
import com.example.floreriaapp.Flor
import com.example.floreriaapp.ui.theme.carrito.CarritoActivity
import com.example.floreriaapp.ui.theme.flor.FlorAdapter
import com.example.floreriaapp.ui.theme.formulario.FormularioActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosActivity : AppCompatActivity() {

    private lateinit var recyclerViewFlores: RecyclerView
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Nuestros Productos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerViewFlores = findViewById(R.id.recyclerViewFlores)
        recyclerViewFlores.layoutManager = LinearLayoutManager(this)
        
        db = DatabaseHelper(this)

        // Cargar datos desde la API
        cargarDatosDesdeApi()
    }

    private fun cargarDatosDesdeApi() {
        val apiService = RetrofitClient.instance
        val call = apiService.obtenerFlores()

        call.enqueue(object : Callback<List<Flor>> {
            override fun onResponse(call: Call<List<Flor>>, response: Response<List<Flor>>) {
                if (response.isSuccessful) {
                    val listaFlores = response.body() ?: emptyList()
                    setupAdapter(listaFlores)
                    Toast.makeText(this@ProductosActivity, "Productos cargados", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProductosActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Flor>>, t: Throwable) {
                Toast.makeText(this@ProductosActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupAdapter(flores: List<Flor>) {
        val adapter = FlorAdapter(flores) { flor ->
            db.agregarAlCarrito(flor.nombre, flor.precio.toDouble(), flor.imagenNombre)
            Toast.makeText(this, "${flor.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }
        recyclerViewFlores.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_productos -> true
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

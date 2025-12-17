package com.example.floreriaapp.model

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.Flor
import com.example.floreriaapp.MainActivity
import com.example.floreriaapp.R
import com.example.floreriaapp.database.DatabaseHelper
import com.example.floreriaapp.repository.CarritoRepository
import com.example.floreriaapp.repository.ProductosRepository
import com.example.floreriaapp.ui.theme.carrito.CarritoActivity
import com.example.floreriaapp.ui.theme.flor.FlorAdapter
import com.example.floreriaapp.ui.theme.formulario.FormularioActivity
import com.example.floreriaapp.viewmodel.ProductosViewModel
import com.example.floreriaapp.viewmodel.ProductosViewModelFactory

class ProductosActivity : AppCompatActivity() {

    private lateinit var recyclerViewFlores: RecyclerView
    private lateinit var viewModel: ProductosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Nuestros Productos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerViewFlores = findViewById(R.id.recyclerViewFlores)
        recyclerViewFlores.layoutManager = LinearLayoutManager(this)

        // Configuración de MVVM
        val dbHelper = DatabaseHelper(this)
        val carritoRepository = CarritoRepository(dbHelper)
        val productosRepository = ProductosRepository()
        val factory = ProductosViewModelFactory(productosRepository, carritoRepository)
        
        viewModel = ViewModelProvider(this, factory)[ProductosViewModel::class.java]

        // Observar datos
        viewModel.flores.observe(this) { flores ->
            setupAdapter(flores)
        }

        viewModel.error.observe(this) { mensaje ->
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }
        
        viewModel.mensaje.observe(this) { mensaje ->
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }

        // Cargar datos
        viewModel.cargarFlores()
    }

    private fun setupAdapter(flores: List<Flor>) {
        val adapter = FlorAdapter(flores) { flor ->
            viewModel.agregarAlCarrito(flor)
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

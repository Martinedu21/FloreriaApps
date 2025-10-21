package com.example.floreriaapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var listViewFlores: ListView
    private lateinit var btnCarrito: Button
    private val flores = arrayListOf("Rosa - $2000", "Tulipán - $1500", "Orquídea - $3000", "Girasol - $1800")
    private val carrito = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        listViewFlores = findViewById(R.id.listViewFlores)
        btnCarrito = findViewById(R.id.btnCarrito)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, flores)
        listViewFlores.adapter = adapter

        listViewFlores.setOnItemClickListener { _, _, position, _ ->
            val florSeleccionada = flores[position]
            carrito.add(florSeleccionada)
            Toast.makeText(this, "$florSeleccionada agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        btnCarrito.setOnClickListener {
            verCarrito()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_solicitudes -> {
                val intent = Intent(this, FormularioActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_carrito -> {
                verCarrito()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun verCarrito() {
        val intent = Intent(this, CarritoActivity::class.java)
        intent.putStringArrayListExtra("carrito", carrito)
        startActivity(intent)
    }
}

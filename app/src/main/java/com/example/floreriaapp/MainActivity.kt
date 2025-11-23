package com.example.floreriaapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.floreriaapp.ui.theme.carrito.CarritoActivity
import com.example.floreriaapp.ui.theme.formulario.FormularioActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Floripondia Store"

        val btnVerProductos: Button = findViewById(R.id.btnVerProductos)
        btnVerProductos.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // La opción de inicio se ha eliminado del menú
            R.id.menu_productos -> {
                startActivity(Intent(this, ProductosActivity::class.java))
                true
            }
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

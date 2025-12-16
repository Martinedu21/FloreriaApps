package com.example.floreriaapp.ui.theme.carrito

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.MainActivity
import com.example.floreriaapp.model.ProductosActivity
import com.example.floreriaapp.R
import com.example.floreriaapp.database.DatabaseHelper
import com.example.floreriaapp.repository.CarritoRepository
import com.example.floreriaapp.ui.theme.formulario.FormularioActivity
import com.example.floreriaapp.ui.theme.recibo.ReciboActivity
import java.text.NumberFormat
import java.util.Locale

class CarritoActivity : AppCompatActivity() {

    private lateinit var viewModel: CarritoViewModel
    private lateinit var adapter: CarritoAdapter
    private lateinit var txtTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Carrito de Compras"

        val recyclerViewCarrito: RecyclerView = findViewById(R.id.recyclerViewCarrito)
        txtTotal = findViewById(R.id.txtTotal)
        val btnVaciar: Button = findViewById(R.id.btnVaciar)
        val btnComprar: Button = findViewById(R.id.btnComprar)

        val dbHelper = DatabaseHelper(this)
        val repository = CarritoRepository(dbHelper)
        val factory = CarritoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CarritoViewModel::class.java]

        // Inicializamos el adaptador con el callback de eliminación
        adapter = CarritoAdapter(emptyList()) { id, cantidad ->
            viewModel.eliminarCantidad(id, cantidad)
            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
        }
        
        recyclerViewCarrito.adapter = adapter
        recyclerViewCarrito.layoutManager = LinearLayoutManager(this)

        viewModel.carrito.observe(this) { items ->
            adapter.setFlores(items)
        }

        viewModel.total.observe(this) { total ->
            val formatoChile = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            txtTotal.text = "Total: ${formatoChile.format(total)}"
        }

        btnVaciar.setOnClickListener {
            viewModel.vaciarCarrito()
            Toast.makeText(this, "Carrito vaciado", Toast.LENGTH_SHORT).show()
        }

        btnComprar.setOnClickListener {
            val itemsActuales = viewModel.carrito.value
            val totalActual = viewModel.total.value ?: 0

            if (!itemsActuales.isNullOrEmpty()) {
                val intent = Intent(this, ReciboActivity::class.java)
                intent.putExtra("ITEMS_COMPRA", ArrayList(itemsActuales))
                intent.putExtra("TOTAL_COMPRA", totalActual)
                startActivity(intent)
                viewModel.vaciarCarrito()
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarCarrito()
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
            R.id.menu_productos -> {
                startActivity(Intent(this, ProductosActivity::class.java))
                true
            }
            R.id.menu_formulario -> {
                startActivity(Intent(this, FormularioActivity::class.java))
                true
            }
            R.id.menu_carrito -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

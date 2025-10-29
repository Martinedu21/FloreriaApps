package com.example.floreriaapp.ui.theme.carrito
// Paquete donde se encuentra la actividad. "ui.theme.carrito" indica que forma parte
// del módulo de interfaz de usuario (UI) relacionada con la pantalla del carrito.

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floreriaapp.R
import com.example.floreriaapp.database.DatabaseHelper
import com.example.floreriaapp.model.FlorEnCarrito
import com.example.floreriaapp.ui.theme.recibo.ReciboActivity

// ---------------------------------------------------------------------------
// Activity encargada de mostrar y gestionar el carrito de compras.
// Aquí se muestran los productos agregados, el total y las acciones
// de vaciar o comprar.
// ---------------------------------------------------------------------------
class CarritoActivity : AppCompatActivity() {

    // Declaración de vistas del layout
    private lateinit var recyclerViewCarrito: RecyclerView
    private lateinit var txtTotal: TextView
    private lateinit var btnVaciar: Button
    private lateinit var btnComprar: Button

    // Adaptador que gestiona cómo se muestran los ítems en el RecyclerView
    private lateinit var adapter: CarritoAdapter

    // Referencia al helper de base de datos (SQLite)
    private lateinit var db: DatabaseHelper

    // -------------------------------------------------------------------
    // Método principal que se ejecuta al crear la actividad.
    // Configura la interfaz, carga los datos y define los eventos.
    // -------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        // Vincular las vistas del layout con las variables
        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito)
        txtTotal = findViewById(R.id.txtTotal)
        btnVaciar = findViewById(R.id.btnVaciar)
        btnComprar = findViewById(R.id.btnComprar)

        // Inicializar la base de datos
        db = DatabaseHelper(this)

        // -----------------------------
        // Configurar el RecyclerView
        // -----------------------------
        // Se obtienen los productos guardados en SQLite y se cargan en el adaptador
        adapter = CarritoAdapter(obtenerCarritoDesdeDB())
        recyclerViewCarrito.adapter = adapter
        recyclerViewCarrito.layoutManager = LinearLayoutManager(this)

        // Muestra el total inicial de la compra
        actualizarTotal()

        // ------------------------------------------------
        // Botón "Vaciar Carrito"
        // Elimina todos los registros de la base de datos
        // y actualiza la vista.
        // ------------------------------------------------
        btnVaciar.setOnClickListener {
            db.vaciarCarrito()                      // Vacía la tabla "carrito"
            adapter.setFlores(emptyList())          // Limpia la lista del adaptador
            actualizarTotal()                       // Actualiza el total mostrado
            Toast.makeText(this, "Carrito vaciado", Toast.LENGTH_SHORT).show()
        }

        // ------------------------------------------------
        // Botón "Comprar"
        // Si hay productos, abre la pantalla de recibo.
        // Si no hay, muestra un mensaje de aviso.
        // ------------------------------------------------
        btnComprar.setOnClickListener {
            if (adapter.itemCount > 0) {
                // Crear un intent para abrir la pantalla de recibo
                val intent = Intent(this, ReciboActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // -------------------------------------------------------
    // onResume(): se ejecuta cuando el usuario vuelve a esta
    // pantalla (por ejemplo, después de ir al recibo o a otra vista).
    // Se usa para actualizar la lista y el total por si hubo cambios.
    // -------------------------------------------------------
    override fun onResume() {
        super.onResume()
        adapter.setFlores(obtenerCarritoDesdeDB())  // Recarga la lista desde la DB
        actualizarTotal()                           // Recalcula el total
    }

    // -------------------------------------------------------
    // Obtiene todos los productos guardados en el carrito
    // desde la base de datos SQLite y los convierte a objetos FlorEnCarrito.
    // -------------------------------------------------------
    private fun obtenerCarritoDesdeDB(): List<FlorEnCarrito> {
        val lista = mutableListOf<FlorEnCarrito>()
        val cursor = db.obtenerCarrito()

        // Recorre los resultados del cursor y los transforma en objetos
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                val imagen = cursor.getInt(cursor.getColumnIndexOrThrow("imagen"))

                // Se crea un objeto FlorEnCarrito con los datos del registro
                lista.add(FlorEnCarrito(id, nombre, precio.toInt(), imagen))
            } while (cursor.moveToNext())
        }

        cursor.close() // Cerrar el cursor para evitar fugas de memoria
        return lista
    }

    // -------------------------------------------------------
    // Calcula el total de la compra sumando los precios de
    // todos los productos guardados en la base de datos.
    // -------------------------------------------------------
    private fun actualizarTotal() {
        val cursor = db.obtenerCarrito()
        var total = 0.0

        if (cursor.moveToFirst()) {
            do {
                total += cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
            } while (cursor.moveToNext())
        }

        cursor.close()
        txtTotal.text = "Total: $$total"
    }
}


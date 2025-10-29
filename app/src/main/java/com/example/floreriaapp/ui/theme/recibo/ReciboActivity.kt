package com.example.floreriaapp.ui.theme.recibo
// Paquete donde se agrupa la lógica de la pantalla de "recibo" o comprobante de compra.

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.floreriaapp.R
import com.example.floreriaapp.database.DatabaseHelper

// ---------------------------------------------------------------------------
// Activity que muestra el recibo de compra con las flores adquiridas.
// Muestra  los productos del carrito, el total y un botón
// para finalizar la compra (que limpia el carrito).
// ---------------------------------------------------------------------------
class ReciboActivity : AppCompatActivity() {

    // Contenedor lineal donde se agregarán los elementos del recibo (TextViews y botón)
    private lateinit var reciboLayout: LinearLayout

    // Referencia a la base de datos local (SQLite)
    private lateinit var db: DatabaseHelper

    // -----------------------------------------------------------------------
    // onCreate(): método principal que se ejecuta al abrir la pantalla.
    // Aquí se configuran las vistas y se cargan los datos del carrito.
    // -----------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo)

        // Vincular vistas del layout
        reciboLayout = findViewById(R.id.reciboLayout)

        // Inicializar la base de datos
        db = DatabaseHelper(this)

        // Mostrar los productos del carrito y el total
        mostrarRecibo()

        // Agregar dinámicamente el botón "Finalizar Compra"
        agregarBotonFinalizar()
    }

    // -----------------------------------------------------------------------
    // Muestra los productos comprados leyendo directamente desde SQLite.
    // Recorre la tabla "carrito", crea un TextView por cada producto y los
    // agrega dinámicamente al LinearLayout.
    // -----------------------------------------------------------------------
    private fun mostrarRecibo() {
        val cursor: Cursor = db.obtenerCarrito() // Obtener todos los registros del carrito
        var total = 0.0

        if (cursor.moveToFirst()) {
            do {
                // Obtener los valores de cada columna
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                total += precio // Sumar al total general

                // Crear un TextView dinámicamente para mostrar la flor y su precio
                val tv = TextView(this)
                tv.text = "$nombre - $$precio"   // Ejemplo: "Rosa Roja - $1500"
                tv.textSize = 16f
                reciboLayout.addView(tv)          // Agregarlo al layout principal
            } while (cursor.moveToNext())
        }

        // Cerrar el cursor para liberar recursos
        cursor.close()

        // Crear y mostrar el total al final del recibo
        val tvTotal = TextView(this)
        tvTotal.text = "Total: $$total"
        tvTotal.textSize = 18f
        tvTotal.setPadding(0, 20, 0, 0)  // Añade espacio antes del total
        reciboLayout.addView(tvTotal)
    }

    // -----------------------------------------------------------------------
    // Agrega un botón "Finalizar Compra" al final del recibo.
    // Al presionarlo:
    //   1. Vacía la tabla del carrito.
    //   2. Muestra un mensaje de confirmación.
    //   3. Cierra la actividad, regresando a la pantalla anterior.
    // -----------------------------------------------------------------------
    private fun agregarBotonFinalizar() {
        val btnFinalizar = Button(this)
        btnFinalizar.text = "Finalizar Compra"

        btnFinalizar.setOnClickListener {
            db.vaciarCarrito() // Limpia la base de datos del carrito
            Toast.makeText(this, "Compra finalizada y carrito vaciado", Toast.LENGTH_SHORT).show()
            finish() // Cierra esta actividad y regresa (por ejemplo, al carrito o al inicio)
        }

        // Agregar el botón al layout debajo de los productos y el total
        reciboLayout.addView(btnFinalizar)
    }
}




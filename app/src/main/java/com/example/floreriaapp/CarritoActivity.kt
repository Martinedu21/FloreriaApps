package com.example.floreriaapp

// Importaciones necesarias para la funcionalidad de Android
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.NumberFormatException

// Definición de la actividad del carrito de compras
class CarritoActivity : AppCompatActivity() {

    // Declaración de variables para los elementos de la UI
    private lateinit var listViewCarrito: ListView  // ListView para mostrar los productos del carrito
    private lateinit var txtTotal: TextView         // TextView para mostrar el total de la compra
    private lateinit var btnVaciar: Button          // Botón para vaciar el carrito
    private var carrito = arrayListOf<String>()     // Lista que guarda los items del carrito
    private lateinit var adapter: ArrayAdapter<String> // Adaptador para vincular la lista al ListView

    // Método llamado cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito) // Asocia el layout XML con esta actividad

        // Inicializar vistas a partir de su ID en el layout
        listViewCarrito = findViewById(R.id.listViewCarrito)
        txtTotal = findViewById(R.id.txtTotal)
        btnVaciar = findViewById(R.id.btnVaciar) // Asegúrate de que este botón exista en el layout

        // Obtener el carrito enviado desde MainActivity (puede venir vacío)
        carrito = intent.getStringArrayListExtra("carrito") ?: arrayListOf()

        // Configurar ListView con un adaptador para mostrar los items
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, carrito)
        listViewCarrito.adapter = adapter

        // Mostrar el total actual de los items en el carrito
        actualizarTotal()

        // Configurar acción del botón "Vaciar"
        btnVaciar.setOnClickListener {
            carrito.clear()                  // Limpiar todos los items del carrito
            adapter.notifyDataSetChanged()   // Notificar al adaptador que los datos cambiaron
            actualizarTotal()                // Actualizar la suma total a 0

            // Enviar carrito vacío de vuelta a MainActivity
            val resultIntent = Intent()
            resultIntent.putStringArrayListExtra("carrito", carrito)
            setResult(Activity.RESULT_OK, resultIntent) // Devolver resultado OK
        }
    }

    // Función para actualizar el total de los items en el carrito
    private fun actualizarTotal() {
        var total = 0
        for (item in carrito) {
            try {
                // Extrae el precio del item asumiendo que está después del último '$'
                val precioString = item.substringAfterLast('$')
                val precio = precioString.trim().toInt() // Convierte a número
                total += precio
            } catch (e: NumberFormatException) {
                // Ignorar items que no tengan un precio válido
            }
        }
        txtTotal.text = "Total: $$total" // Muestra el total en el TextView
    }
}



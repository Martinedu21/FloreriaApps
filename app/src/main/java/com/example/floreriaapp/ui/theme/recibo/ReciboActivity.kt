package com.example.floreriaapp.ui.theme.recibo

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.floreriaapp.R
import com.example.floreriaapp.database.DatabaseHelper

class ReciboActivity : AppCompatActivity() {

    private lateinit var reciboLayout: LinearLayout
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo)

        reciboLayout = findViewById(R.id.reciboLayout)
        db = DatabaseHelper(this)

        mostrarRecibo()
        agregarBotonFinalizar()
    }

    private fun mostrarRecibo() {
        val cursor: Cursor = db.obtenerCarrito()
        var total = 0.0

        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                total += precio

                val tv = TextView(this)
                tv.text = "$nombre - $precio"
                tv.textSize = 16f
                reciboLayout.addView(tv)
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Mostrar total
        val tvTotal = TextView(this)
        tvTotal.text = "Total: $total"
        tvTotal.textSize = 18f
        tvTotal.setPadding(0, 20, 0, 0)
        reciboLayout.addView(tvTotal)
    }

    private fun agregarBotonFinalizar() {
        val btnFinalizar = Button(this)
        btnFinalizar.text = "Finalizar Compra"
        btnFinalizar.setOnClickListener {
            db.vaciarCarrito()
            Toast.makeText(this, "Compra finalizada y carrito vaciado", Toast.LENGTH_SHORT).show()
            finish() // Cierra ReciboActivity y regresa al CarritoActivity o Main
        }
        reciboLayout.addView(btnFinalizar)
    }
}



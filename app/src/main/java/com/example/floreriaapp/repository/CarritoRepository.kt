package com.example.floreriaapp.repository

import com.example.floreriaapp.database.DatabaseHelper
import com.example.floreriaapp.model.FlorEnCarrito

class CarritoRepository(private val dbHelper: DatabaseHelper) {

    fun obtenerCarrito(): List<FlorEnCarrito> {
        val lista = mutableListOf<FlorEnCarrito>()
        val cursor = dbHelper.obtenerCarrito()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                
                // Leemos la imagen como String (nombre del recurso)
                val imagenNombre = cursor.getString(cursor.getColumnIndexOrThrow("imagen")) 
                
                val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))

                // Pasamos imagenNombre al constructor
                lista.add(FlorEnCarrito(id, nombre, precio.toInt(), imagenNombre, cantidad))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun agregarAlCarrito(nombre: String, precio: Double, imagenNombre: String, cantidad: Int) {
        dbHelper.agregarAlCarrito(nombre, precio, imagenNombre, cantidad)
    }
    
    fun eliminarCantidad(id: Int, cantidad: Int) {
        dbHelper.eliminarCantidadDelCarrito(id, cantidad)
    }

    fun vaciarCarrito() {
        dbHelper.vaciarCarrito()
    }
}

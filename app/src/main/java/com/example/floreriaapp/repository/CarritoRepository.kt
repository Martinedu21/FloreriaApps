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
                val imagen = cursor.getInt(cursor.getColumnIndexOrThrow("imagen"))
                val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))

                lista.add(FlorEnCarrito(id, nombre, precio.toInt(), imagen, cantidad))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun vaciarCarrito() {
        dbHelper.vaciarCarrito()
    }
}

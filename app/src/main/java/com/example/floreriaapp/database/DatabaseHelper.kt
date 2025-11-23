package com.example.floreriaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "floreria.db", null, 3) { // Incrementamos versión a 3 para migrar a String en imagen

    companion object {
        const val TABLE_FLORES = "flores"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_IMAGEN = "imagen" // Ahora guardará el nombre del recurso (String)

        const val TABLE_CARRITO = "carrito"
        const val COLUMN_CANTIDAD = "cantidad"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createFlores = """
            CREATE TABLE $TABLE_FLORES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_DESCRIPCION TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_IMAGEN TEXT 
            )
        """.trimIndent()
        // COLUMN_IMAGEN cambiado a TEXT

        val createCarrito = """
            CREATE TABLE $TABLE_CARRITO (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_IMAGEN TEXT, 
                $COLUMN_CANTIDAD INTEGER DEFAULT 1
            )
        """.trimIndent()
        // COLUMN_IMAGEN cambiado a TEXT

        db?.execSQL(createFlores)
        db?.execSQL(createCarrito)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // En una app real deberíamos migrar datos, pero para este ejercicio recreamos tablas.
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FLORES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CARRITO")
        onCreate(db)
    }

    // ============================================================
    // ============        OPERACIONES CON FLORES       ============
    // ============================================================

    // Modificado: imagen ahora es String
    fun agregarFlor(nombre: String, descripcion: String, precio: Double, imagenNombre: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_DESCRIPCION, descripcion)
            put(COLUMN_PRECIO, precio)
            put(COLUMN_IMAGEN, imagenNombre)
        }
        return db.insert(TABLE_FLORES, null, values)
    }

    fun obtenerFlores(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_FLORES", null)
    }

    // ============        OPERACIONES CON CARRITO      ============

    // Modificado: imagen ahora es String
    fun agregarAlCarrito(nombre: String, precio: Double, imagenNombre: String): Long {
        val db = writableDatabase

        // Verificar si el producto ya existe en el carrito
        val cursor = db.query(
            TABLE_CARRITO,
            arrayOf(COLUMN_ID, COLUMN_CANTIDAD),
            "$COLUMN_NOMBRE = ?",
            arrayOf(nombre),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            // Si existe, actualizamos la cantidad
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val cantidadActual = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD))
            val nuevaCantidad = cantidadActual + 1

            val values = ContentValues().apply {
                put(COLUMN_CANTIDAD, nuevaCantidad)
            }
            cursor.close()
            return db.update(TABLE_CARRITO, values, "$COLUMN_ID = ?", arrayOf(id.toString())).toLong()
        } else {
            // Si no existe, insertamos nuevo registro
            cursor.close()
            val values = ContentValues().apply {
                put(COLUMN_NOMBRE, nombre)
                put(COLUMN_PRECIO, precio)
                put(COLUMN_IMAGEN, imagenNombre) // Guardamos el nombre de la imagen
                put(COLUMN_CANTIDAD, 1)
            }
            return db.insert(TABLE_CARRITO, null, values)
        }
    }

    fun obtenerCarrito(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_CARRITO", null)
    }

    fun eliminarDelCarrito(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_CARRITO, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    fun vaciarCarrito() {
        val db = writableDatabase
        db.delete(TABLE_CARRITO, null, null)
    }
}

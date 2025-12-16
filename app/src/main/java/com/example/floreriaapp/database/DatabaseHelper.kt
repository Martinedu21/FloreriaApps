package com.example.floreriaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "floreria.db", null, 3) {

    companion object {
        const val TABLE_FLORES = "flores"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_IMAGEN = "imagen"

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

        val createCarrito = """
            CREATE TABLE $TABLE_CARRITO (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_IMAGEN TEXT, 
                $COLUMN_CANTIDAD INTEGER DEFAULT 1
            )
        """.trimIndent()

        db?.execSQL(createFlores)
        db?.execSQL(createCarrito)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FLORES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CARRITO")
        onCreate(db)
    }

    // ============================================================
    // ============        OPERACIONES CON FLORES       ============
    // ============================================================

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

    // Modificado para aceptar la cantidad seleccionada
    fun agregarAlCarrito(nombre: String, precio: Double, imagenNombre: String, cantidad: Int = 1): Long {
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
            // Si existe, actualizamos la cantidad sumando la nueva
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val cantidadActual = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD))
            val nuevaCantidad = cantidadActual + cantidad

            val values = ContentValues().apply {
                put(COLUMN_CANTIDAD, nuevaCantidad)
            }
            cursor.close()
            return db.update(TABLE_CARRITO, values, "$COLUMN_ID = ?", arrayOf(id.toString())).toLong()
        } else {
            // Si no existe, insertamos nuevo registro con la cantidad seleccionada
            cursor.close()
            val values = ContentValues().apply {
                put(COLUMN_NOMBRE, nombre)
                put(COLUMN_PRECIO, precio)
                put(COLUMN_IMAGEN, imagenNombre)
                put(COLUMN_CANTIDAD, cantidad)
            }
            return db.insert(TABLE_CARRITO, null, values)
        }
    }

    fun obtenerCarrito(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_CARRITO", null)
    }
    
    // Función para restar cantidad o eliminar si llega a 0
    fun eliminarCantidadDelCarrito(id: Int, cantidadAEliminar: Int) {
        val db = writableDatabase
        
        // Obtenemos la cantidad actual
        val cursor = db.query(
            TABLE_CARRITO,
            arrayOf(COLUMN_CANTIDAD),
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        
        if (cursor.moveToFirst()) {
            val cantidadActual = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD))
            cursor.close()
            
            if (cantidadAEliminar >= cantidadActual) {
                // Si eliminamos, borramos el registro
                db.delete(TABLE_CARRITO, "$COLUMN_ID=?", arrayOf(id.toString()))
            } else {
                // Si no, solo restamos la cantidad
                val nuevaCantidad = cantidadActual - cantidadAEliminar
                val values = ContentValues().apply {
                    put(COLUMN_CANTIDAD, nuevaCantidad)
                }
                db.update(TABLE_CARRITO, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
            }
        } else {
            cursor.close()
        }
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

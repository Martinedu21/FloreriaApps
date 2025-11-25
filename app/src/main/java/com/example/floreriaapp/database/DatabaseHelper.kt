package com.example.floreriaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// ==================================================================================
// CLASE DATABASEHELPER (BASE DE DATOS INTERNA)
// ==================================================================================
// Esta clase gestiona la base de datos SQLite interna del teléfono.
// Permite guardar los datos del catálogo y del carrito para que no se borren
// al cerrar la aplicación (Persistencia de datos).
// ==================================================================================
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "floreria.db", null, 2) {

    companion object {
        const val TABLE_FLORES = "flores"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_IMAGEN = "imagen"

        const val TABLE_CARRITO = "carrito"
        // [CONTADOR] Nueva columna para guardar cuántas flores de este tipo lleva el usuario
        const val COLUMN_CANTIDAD = "cantidad"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createFlores = """
            CREATE TABLE $TABLE_FLORES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_DESCRIPCION TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_IMAGEN INTEGER
            )
        """.trimIndent()

        // [CONTADOR] Se crea la tabla carrito incluyendo la columna cantidad con valor por defecto 1
        val createCarrito = """
            CREATE TABLE $TABLE_CARRITO (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_IMAGEN INTEGER,
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

    // ... Operaciones Flores ...
    fun agregarFlor(nombre: String, descripcion: String, precio: Double, imagen: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_DESCRIPCION, descripcion)
            put(COLUMN_PRECIO, precio)
            put(COLUMN_IMAGEN, imagen)
        }
        return db.insert(TABLE_FLORES, null, values)
    }

    fun obtenerFlores(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_FLORES", null)
    }

    // ==============================================================================
    // LÓGICA DEL CONTADOR AL AGREGAR AL CARRITO
    // ==============================================================================
    fun agregarAlCarrito(nombre: String, precio: Double, imagen: Int): Long {
        val db = writableDatabase

        // 1. Verificamos si el producto YA EXISTE en la tabla carrito
        val cursor = db.query(
            TABLE_CARRITO,
            arrayOf(COLUMN_ID, COLUMN_CANTIDAD),
            "$COLUMN_NOMBRE = ?",
            arrayOf(nombre),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            // [CONTADOR - CASO 1] El producto YA EXISTE.
            // En lugar de crear otro registro, obtenemos la cantidad actual y le sumamos 1.
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val cantidadActual = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD))
            val nuevaCantidad = cantidadActual + 1

            val values = ContentValues().apply {
                put(COLUMN_CANTIDAD, nuevaCantidad)
            }
            cursor.close()
            // Actualizamos el registro existente con la nueva cantidad
            return db.update(TABLE_CARRITO, values, "$COLUMN_ID = ?", arrayOf(id.toString())).toLong()
        } else {
            // [CONTADOR - CASO 2] El producto NO EXISTE.
            // Insertamos un nuevo registro con cantidad = 1.
            cursor.close()
            val values = ContentValues().apply {
                put(COLUMN_NOMBRE, nombre)
                put(COLUMN_PRECIO, precio)
                put(COLUMN_IMAGEN, imagen)
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

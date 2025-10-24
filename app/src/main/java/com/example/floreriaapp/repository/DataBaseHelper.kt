package com.example.floreriaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Clase para manejar SQLite
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "floreria.db", null, 1) {

    companion object {
        // Tabla de flores
        const val TABLE_FLORES = "flores"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_IMAGEN = "imagen"

        // Tabla del carrito
        const val TABLE_CARRITO = "carrito"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla de flores
        val createFlores = """
            CREATE TABLE $TABLE_FLORES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_DESCRIPCION TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_IMAGEN INTEGER
            )
        """.trimIndent()

        // Crear tabla de carrito
        val createCarrito = """
            CREATE TABLE $TABLE_CARRITO (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_IMAGEN INTEGER
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

    // ---------------------------
    // FLORES
    // ---------------------------
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

    // ---------------------------
    // CARRITO
    // ---------------------------
    fun agregarAlCarrito(nombre: String, precio: Double, imagen: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_PRECIO, precio)
            put(COLUMN_IMAGEN, imagen)
        }
        return db.insert(TABLE_CARRITO, null, values)
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

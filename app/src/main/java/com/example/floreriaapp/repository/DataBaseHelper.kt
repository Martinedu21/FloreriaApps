package com.example.floreriaapp.database
// Paquete donde se encuentra esta clase.
// "database" se suele usar para agrupar clases que gestionan almacenamiento o persistencia de datos.

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// ------------------------------------------------------
// Clase que maneja la base de datos SQLite de la aplicación.
// Hereda de SQLiteOpenHelper, que proporciona métodos para
// crear, actualizar y acceder a una base de datos local.
// ------------------------------------------------------
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "floreria.db", null, 1) {

    // ---------------------------------------------------------------------
    // Objeto companion que define constantes reutilizables para nombres
    // de tablas y columnas. Así se evita escribir texto repetido y posibles errores.
    // ---------------------------------------------------------------------
    companion object {
        // Nombres de la tabla de flores y sus columnas
        const val TABLE_FLORES = "flores"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_IMAGEN = "imagen"

        // Tabla del carrito
        const val TABLE_CARRITO = "carrito"
    }

    // ------------------------------------------------------------
    // Método que se ejecuta automáticamente la primera vez que se
    // crea la base de datos. Aquí definimos las estructuras de tablas.
    // ------------------------------------------------------------
    override fun onCreate(db: SQLiteDatabase?) {
        // Sentencia SQL para crear la tabla de flores
        val createFlores = """
            CREATE TABLE $TABLE_FLORES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_DESCRIPCION TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_IMAGEN INTEGER
            )
        """.trimIndent()

        // Sentencia SQL para crear la tabla del carrito
        val createCarrito = """
            CREATE TABLE $TABLE_CARRITO (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_IMAGEN INTEGER
            )
        """.trimIndent()

        // Ejecuta las sentencias SQL para crear las tablas en la base de datos
        db?.execSQL(createFlores)
        db?.execSQL(createCarrito)
    }

    // ------------------------------------------------------------
    // Método que se ejecuta cuando se actualiza la versión de la DB.
    // Aquí se eliminan las tablas existentes y se vuelven a crear.
    // ------------------------------------------------------------
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FLORES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CARRITO")
        onCreate(db)
    }

    // ============================================================
    // ============        OPERACIONES CON FLORES       ============
    // ============================================================

    // Inserta una nueva flor en la tabla "flores"
    fun agregarFlor(nombre: String, descripcion: String, precio: Double, imagen: Int): Long {
        val db = writableDatabase  // Obtiene una referencia en modo escritura
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_DESCRIPCION, descripcion)
            put(COLUMN_PRECIO, precio)
            put(COLUMN_IMAGEN, imagen)
        }
        // Inserta los valores en la tabla y devuelve el ID del nuevo registro
        return db.insert(TABLE_FLORES, null, values)
    }

    // Devuelve un Cursor con todos los registros de la tabla "flores"
    // El Cursor permite recorrer los resultados con moveToNext()
    fun obtenerFlores(): Cursor {
        val db = readableDatabase  // Modo lectura (no permite modificar)
        return db.rawQuery("SELECT * FROM $TABLE_FLORES", null)
    }

    // ============        OPERACIONES CON CARRITO      ============

    // Inserta un nuevo ítem en el carrito (tabla "carrito")
    fun agregarAlCarrito(nombre: String, precio: Double, imagen: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_PRECIO, precio)
            put(COLUMN_IMAGEN, imagen)
        }
        return db.insert(TABLE_CARRITO, null, values)
    }

    // Devuelve todos los elementos actualmente en el carrito
    fun obtenerCarrito(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_CARRITO", null)
    }

    // Elimina un elemento específico del carrito según su ID
    fun eliminarDelCarrito(id: Int): Int {
        val db = writableDatabase
        // Elimina el registro donde la columna "id" coincida con el parámetro
        return db.delete(TABLE_CARRITO, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // Vacía completamente el carrito (elimina todos los registros)
    fun vaciarCarrito() {
        val db = writableDatabase
        db.delete(TABLE_CARRITO, null, null)
    }
}


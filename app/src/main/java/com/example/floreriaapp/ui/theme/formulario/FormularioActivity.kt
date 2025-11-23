package com.example.floreriaapp.ui.theme.formulario

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.floreriaapp.MainActivity
import com.example.floreriaapp.ProductosActivity
import com.example.floreriaapp.R
import com.example.floreriaapp.ui.theme.carrito.CarritoActivity
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

// Actividad que maneja un formulario con título, descripción, razón y foto adjunta
class FormularioActivity : AppCompatActivity() {

    // Elementos de la interfaz
    private lateinit var etTitulo: TextInputEditText         // Campo para el título
    private lateinit var actvRazon: AutoCompleteTextView    // Campo para seleccionar la razón
    private lateinit var etDescripcion: TextInputEditText   // Campo para la descripción
    private lateinit var btnAdjuntarFoto: Button            // Botón para adjuntar foto
    private lateinit var btnEnviar: Button                  // Botón para enviar el formulario
    private lateinit var ivFoto: ImageView                  // ImageView para mostrar la foto seleccionada
    private var currentPhotoPath: String? = null           // Ruta de la foto tomada con cámara
    private var photoURI: Uri? = null                       // URI de la foto para mostrar en ImageView

    // Lanzador para tomar foto con la cámara
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            photoURI?.let {
                ivFoto.setImageURI(it)   // Muestra la foto en ImageView
                ivFoto.visibility = View.VISIBLE
            }
        }
    }

    // Lanzador para elegir foto desde la galería
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                photoURI = uri
                ivFoto.setImageURI(uri)   // Muestra la foto seleccionada
                ivFoto.visibility = View.VISIBLE
            }
        }
    }

    // Lanzador para solicitar permiso de cámara
    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            dispatchTakePictureIntent()  // Si se da permiso, abre cámara
        } else {
            // Si se niega permiso, muestra alerta
            AlertDialog.Builder(this)
                .setMessage("Permiso de cámara denegado")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario) // Asocia el layout XML

        // Habilitar botón de atrás en la Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Formulario"

        // Inicializar vistas
        etTitulo = findViewById(R.id.et_titulo)
        actvRazon = findViewById(R.id.actv_razon)
        etDescripcion = findViewById(R.id.et_descripcion)
        btnAdjuntarFoto = findViewById(R.id.btn_adjuntar_foto)
        btnEnviar = findViewById(R.id.btn_enviar)
        ivFoto = findViewById(R.id.iv_foto)

        // Lista de razones para el AutoCompleteTextView
        val razones = arrayOf("Ramo Personalizado", "Reclamo", "Sugerencia", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, razones)
        actvRazon.setAdapter(adapter)

        // Configura click para adjuntar foto
        btnAdjuntarFoto.setOnClickListener { selectImage() }

        // Configura click para enviar formulario
        btnEnviar.setOnClickListener {
            if (validarFormulario()) {
                // Aquí iría la lógica real de envío (a servidor, BD, etc.)
                Toast.makeText(this, "Formulario enviado con éxito", Toast.LENGTH_LONG).show()
                limpiarFormulario() // Opcional: limpiar después de enviar
            }
        }
    }

    // Menú superior
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Al pulsar la flecha de atrás o el título, volvemos al Inicio
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_productos -> {
                startActivity(Intent(this, ProductosActivity::class.java))
                true
            }
            R.id.menu_formulario -> true // Ya estamos aquí
            R.id.menu_carrito -> {
                startActivity(Intent(this, CarritoActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Función de validación
    private fun validarFormulario(): Boolean {
        var esValido = true

        val titulo = etTitulo.text.toString().trim()
        val razon = actvRazon.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()

        if (titulo.isEmpty()) {
            etTitulo.error = "El título es obligatorio"
            esValido = false
        } else {
            etTitulo.error = null // Limpia error si ya está correcto
        }

        if (razon.isEmpty()) {
            actvRazon.error = "Seleccione una razón"
            esValido = false
        } else {
            actvRazon.error = null
        }

        if (descripcion.isEmpty()) {
            etDescripcion.error = "La descripción es obligatoria"
            esValido = false
        } else {
            etDescripcion.error = null
        }

        return esValido
    }

    // Función para limpiar formulario tras envío exitoso
    private fun limpiarFormulario() {
        etTitulo.text = null
        actvRazon.text = null
        etDescripcion.text = null
        ivFoto.setImageDrawable(null)
        ivFoto.visibility = View.GONE
        photoURI = null
        etTitulo.requestFocus()
    }

    // Función que muestra opciones para adjuntar foto
    private fun selectImage() {
        val options = arrayOf<CharSequence>("Tomar Foto", "Elegir de la Galería", "Cancelar")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Adjuntar Foto")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Tomar Foto" ->
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA) // Solicita permiso cámara
                options[item] == "Elegir de la Galería" -> {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    pickImageLauncher.launch(pickPhoto) // Lanza galería
                }
                options[item] == "Cancelar" -> dialog.dismiss() // Cierra diálogo
            }
        }
        builder.show()
    }

    // Función que abre la cámara para tomar foto
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Crear archivo para guardar la foto
                val photoFile: File? = try { createImageFile() } catch (ex: IOException) { null }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.floreriaapp.provider",
                        it
                    )
                    this.photoURI = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI) // Guarda la foto
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    takePictureLauncher.launch(takePictureIntent) // Lanza cámara
                }
            }
        }
    }

    // Función para crear un archivo temporal para la foto
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", ".jpg", storageDir
        ).apply { currentPhotoPath = absolutePath } // Guarda ruta actual
    }
}

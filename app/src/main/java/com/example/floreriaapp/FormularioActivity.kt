package com.example.floreriaapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class FormularioActivity : AppCompatActivity() {

    private lateinit var etTitulo: TextInputEditText
    private lateinit var actvRazon: AutoCompleteTextView
    private lateinit var etDescripcion: TextInputEditText
    private lateinit var btnAdjuntarFoto: Button
    private lateinit var ivFoto: ImageView
    private var currentPhotoPath: String? = null
    private var photoURI: Uri? = null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            photoURI?.let {
                ivFoto.setImageURI(it)
                ivFoto.visibility = View.VISIBLE
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                photoURI = uri
                ivFoto.setImageURI(uri)
                ivFoto.visibility = View.VISIBLE
            }
        }
    }
    
    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            dispatchTakePictureIntent()
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        etTitulo = findViewById(R.id.et_titulo)
        actvRazon = findViewById(R.id.actv_razon)
        etDescripcion = findViewById(R.id.et_descripcion)
        btnAdjuntarFoto = findViewById(R.id.btn_adjuntar_foto)
        ivFoto = findViewById(R.id.iv_foto)

        val razones = arrayOf("Ramo Personalizado", "Reclamo", "Sugerencia", "Otros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, razones)
        actvRazon.setAdapter(adapter)

        btnAdjuntarFoto.setOnClickListener {
            selectImage()
        }
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Tomar Foto", "Elegir de la Galería", "Cancelar")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Adjuntar Foto")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Tomar Foto" -> {
                    requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
                options[item] == "Elegir de la Galería" -> {
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    pickImageLauncher.launch(pickPhoto)
                }
                options[item] == "Cancelar" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.floreriaapp.provider",
                        it
                    )
                    this.photoURI = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    takePictureLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
}

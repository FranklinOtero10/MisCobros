package com.example.miscobros

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.miscobros.models.Cobrador
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AgregarCobradorActivity : AppCompatActivity() {

    private lateinit var edtNombresCobrador: TextInputEditText
    private lateinit var edtEmailCobrador: TextInputEditText
    private lateinit var edtDireccionCobrador: TextInputEditText
    private lateinit var edtTelCobrador: TextInputEditText
    private lateinit var btnGuardarCobrador: Button
    private lateinit var btnGaleriaCobrador: Button
    private lateinit var imgCobrador: ImageView
    private lateinit var database: DatabaseReference
    private var estado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cobrador)
        //AppBar personalizada la llamamos mediante id
        setSupportActionBar(findViewById(R.id.topAppBar))
        //habilitamos el boton de regresar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        estado = 0

        //Inicializando las variables
        edtNombresCobrador = findViewById(R.id.edtNombresCobrador)
        edtDireccionCobrador = findViewById(R.id.edtDireccionCobrador)
        edtEmailCobrador = findViewById(R.id.edtEmailCobrador)
        edtTelCobrador = findViewById(R.id.edtTelCobrador)
        btnGuardarCobrador = findViewById(R.id.btnGuardarCobrador)
        btnGaleriaCobrador = findViewById(R.id.btnGaleriaCob)
        imgCobrador = findViewById(R.id.imgCobrador)

        //cargar datos si se atualizara
        cargarDatos()

        if (estado == 0){
            title = "Agregar Cobrador"
        }else{
            title = "Actualizar Cobrador"
            btnGuardarCobrador.setText(R.string.update)
        }

        database = FirebaseDatabase.getInstance().getReference("cobradores")
        btnGuardarCobrador.setOnClickListener {
            if (estado == 0){
                insertarCobrador()
            }else{
                modificarCobrador()
            }
        }

        btnGaleriaCobrador.setOnClickListener {
            pedirPermisos()
        }
    }

    private fun insertarCobrador(){
        if (edtNombresCobrador.text.toString().isNotEmpty() && edtEmailCobrador.text.toString().isNotEmpty() &&
                edtDireccionCobrador.text.toString().isNotEmpty() && edtTelCobrador.text.toString().isNotEmpty()){
            val dbId = database.push().key!!
            val cobrador = Cobrador(dbId ,edtNombresCobrador.text.toString(), edtEmailCobrador.text.toString(),
            edtDireccionCobrador.text.toString(), edtTelCobrador.text.toString())
            database.child(dbId).setValue(cobrador).addOnCompleteListener {
                configProgressDialog()
            }.addOnFailureListener {
                Toast.makeText(this, "No se pudo guardar el cobrador", Toast.LENGTH_SHORT).show()
            }
        }else{
            val contextView = findViewById<View>(R.id.layoutCob)
            Snackbar.make(contextView, "Debe completar todos los campos", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun modificarCobrador(){
        if (edtNombresCobrador.text.toString().isNotEmpty() && edtEmailCobrador.text.toString().isNotEmpty() &&
            edtDireccionCobrador.text.toString().isNotEmpty() && edtTelCobrador.text.toString().isNotEmpty()){
            val uid = intent.getStringExtra("uid")
            database = FirebaseDatabase.getInstance().getReference("cobradores").child(uid.toString())
            val cobrador = Cobrador(uid.toString() ,edtNombresCobrador.text.toString(), edtEmailCobrador.text.toString(),
                edtDireccionCobrador.text.toString(), edtTelCobrador.text.toString())
            database.setValue(cobrador).addOnCompleteListener {
                configProgressDialog()
            }.addOnFailureListener {
                Toast.makeText(this, "No se pudo guardar el cobrador", Toast.LENGTH_SHORT).show()
            }
        }else{
            val contextView = findViewById<View>(R.id.layoutCob)
            Snackbar.make(contextView, "Debe completar todos los campos", Snackbar.LENGTH_SHORT).show()
        }
    }

    //Cargando datos si se va actualizar
    private fun cargarDatos(){
        try {
            edtNombresCobrador.setText(intent.getStringExtra("nombre"))
            edtEmailCobrador.setText(intent.getStringExtra("correo"))
            edtDireccionCobrador.setText(intent.getStringExtra("direccion"))
            edtTelCobrador.setText(intent.getStringExtra("telefono"))
            estado = intent.getIntExtra("estado", 0)
        }catch (e: Exception){
            estado = 0
        }
    }

    //Configurando el progress dialog
    private fun configProgressDialog(){
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        alertBuilder.setView(dialogView)
        alertBuilder.setCancelable(false)
        val dialog = alertBuilder.create()
        dialog.show()
        //Configurando hilo para asignar tiempo
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            finish()
            Toast.makeText(this, "Cobrador guardado", Toast.LENGTH_SHORT).show()
        }, 3000)
    }

    //Abriendo la galeria
    private fun pedirPermisos(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED ->{
                    elegirImagen()
                }

                else-> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            elegirImagen()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted ->
        if (isGranted){
            elegirImagen()
        }else{
            Toast.makeText(this, "Debe habilitar los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data
            imgCobrador.setImageURI(data)
        }
    }

    private fun elegirImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    //Configuración del action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Finalizar la actividad
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
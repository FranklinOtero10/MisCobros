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
import com.example.miscobros.models.Cliente
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception


class AgregarClienteActivity : AppCompatActivity() {

    private lateinit var edtNombres: TextInputEditText
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtDireccion: TextInputEditText
    private lateinit var edtTel: TextInputEditText
    private lateinit var btnGuardarCliente: Button
    private lateinit var btnGaleriaCliente: Button
    private lateinit var imgCliente: ImageView
    private lateinit var database: DatabaseReference
    private var estado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cliente)
        //AppBar personalizada la llamamos mediante id
        setSupportActionBar(findViewById(R.id.topAppBar))
        //habilitamos el boton de regresar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        estado = 0

        //Inicializando las variables
        edtNombres = findViewById(R.id.edtNombresCliente)
        edtDireccion = findViewById(R.id.edtDireccionCliente)
        edtEmail = findViewById(R.id.edtEmailCliente)
        edtTel = findViewById(R.id.edtTelCliente)
        btnGuardarCliente = findViewById(R.id.btnGuardarCliente)
        btnGaleriaCliente = findViewById(R.id.btnGaleria)
        imgCliente = findViewById(R.id.imgCliente)

        //Cargando datos si se va a actualizar
        cargarDatos()

        if (estado == 0){
            title = "Agregar Cliente"
        }else{
            title = "Actualizar Cliente"
            btnGuardarCliente.setText(R.string.update)
        }

        database = FirebaseDatabase.getInstance().getReference("clientes")
        btnGuardarCliente.setOnClickListener {
            if (estado == 0){
                insertarCliente()
            }else{
                modificarCliente()
            }
        }

        btnGaleriaCliente.setOnClickListener {
            pedirPermisos()
        }

    }

    private fun insertarCliente(){
        if (edtNombres.text.toString().isNotEmpty() && edtEmail.text.toString().isNotEmpty() &&
                edtDireccion.text.toString().isNotEmpty() && edtTel.text.toString().isNotEmpty()){
            val dbId = database.push().key!!
            val cliente = Cliente(dbId, edtNombres.text.toString(), edtEmail.text.toString(),
                edtDireccion.text.toString(), edtTel.text.toString())
            database.child(dbId).setValue(cliente).addOnCompleteListener {
                configProgressDialog()
            }.addOnFailureListener {
                Toast.makeText(this, "No se pudo guardar el cliente", Toast.LENGTH_SHORT).show()
            }

        }else{
            val contextView = findViewById<View>(R.id.miLayout)
            Snackbar.make(contextView, "Debe completar todos los campos", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun modificarCliente(){
        if (edtNombres.text.toString().isNotEmpty() && edtEmail.text.toString().isNotEmpty() &&
            edtDireccion.text.toString().isNotEmpty() && edtTel.text.toString().isNotEmpty()){
            val uid = intent.getStringExtra("uid")
            database = FirebaseDatabase.getInstance().getReference("clientes").child(uid.toString())
            val cliente = Cliente(uid.toString(),edtNombres.text.toString(), edtEmail.text.toString(),
                edtDireccion.text.toString(), edtTel.text.toString())
            database.setValue(cliente).addOnCompleteListener {
                configProgressDialog()
            }.addOnFailureListener {
                Toast.makeText(this, "No se pudo actualizar el cliente", Toast.LENGTH_SHORT).show()
            }
        }else{
            val contextView = findViewById<View>(R.id.miLayout)
            Snackbar.make(contextView, "Debe completar todos los campos", Snackbar.LENGTH_SHORT).show()
        }
    }

    //Cargar datos si se va a actualizar
    private fun cargarDatos(){
        try {
            edtNombres.setText(intent.getStringExtra("nombre"))
            edtEmail.setText(intent.getStringExtra("correo"))
            edtDireccion.setText(intent.getStringExtra("direccion"))
            edtTel.setText(intent.getStringExtra("telefono"))
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
            Toast.makeText(this, "Cliente guardado", Toast.LENGTH_SHORT).show()
        }, 3000)
    }

    //https://www.youtube.com/watch?v=miJooBq9iwE
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
            imgCliente.setImageURI(data)
        }
    }

    private fun elegirImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private fun guardarImagen(){

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
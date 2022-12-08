package com.example.miscobros

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.miscobros.models.Cobros
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AgregarCobrosActivity : AppCompatActivity() {

    private lateinit var edtConceptoCobros: TextInputEditText
    private lateinit var edtSaldoCobros: TextInputEditText
    private lateinit var edtFechaCobros: TextInputEditText
    private lateinit var edtCobradorCobros: TextInputEditText
    private lateinit var edtClienteCobros: TextInputEditText
    private lateinit var btnGuardarCobros: Button
    private lateinit var btnUpdateCobros: Button
    private lateinit var database: DatabaseReference

    private var estado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cobros)
        //AppBar personalizada la llamamos mediante id
        setSupportActionBar(findViewById(R.id.topAppBar))
        //habilitamos el boton de regresar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        estado = 0

        edtConceptoCobros = findViewById(R.id.edtConceptoCobros)
        edtSaldoCobros = findViewById(R.id.edtSaldoCobros)
        edtFechaCobros = findViewById(R.id.edtFechaCobros)
        edtCobradorCobros = findViewById(R.id.edtCobradorCobros)
        edtClienteCobros = findViewById(R.id.edtClienteCobros)
        btnGuardarCobros = findViewById(R.id.btnGuardarCobros)
        btnUpdateCobros = findViewById(R.id.btnUpdateCobros)
        //cargar datos si se atualizara
        cargarDatos()

        if (estado == 0){
            title = "Agregar Cobro"
        }else{
            title = "Actualizar Cobro"
            btnGuardarCobros.setText(R.string.update)
        }
        //selCliente()
        //selCobrador()
        database = FirebaseDatabase.getInstance().getReference("cobros")

        edtFechaCobros.setOnClickListener {
            showDatePickerDialog()
        }

        edtClienteCobros.setOnClickListener {
            val intent = Intent(this, SeleccionarClienteActivity::class.java)
            startActivity(intent)
        }

        edtCobradorCobros.setOnClickListener {
            val intent = Intent(this, SeleccionarCobradorActivity::class.java)
            startActivity(intent)
        }

        btnUpdateCobros.setOnClickListener {
            selCliente()
            selCobrador()
        }

        btnGuardarCobros.setOnClickListener {
            if (estado == 0){
                insertarCobros()
            }else{
                modificarCobro()
            }
        }
    }

    private fun insertarCobros(){
        if(edtConceptoCobros.text.toString().isNotEmpty() && edtSaldoCobros.text.toString().isNotEmpty() && edtFechaCobros.text.toString().isNotEmpty()
            && edtCobradorCobros.text.toString().isNotEmpty() && edtClienteCobros.text.toString().isNotEmpty()){
            val dbId = database.push().key!!
            val cobros = Cobros(dbId, edtConceptoCobros.text.toString(), edtSaldoCobros.text.toString().toDouble(),
            edtFechaCobros.text.toString(), edtCobradorCobros.text.toString(), edtClienteCobros.text.toString())
            database.child(dbId).setValue(cobros).addOnCompleteListener {
                borrarShared()
                configProgressDialog()
            }.addOnFailureListener {
                Toast.makeText(this, "No se pudo guardar el cobro", Toast.LENGTH_SHORT).show()
            }
        }else{
            val contextView = findViewById<View>(R.id.layoutCobrosA)
            Snackbar.make(contextView, "Debe completar todos los campos", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun modificarCobro() {
        if (edtConceptoCobros.text.toString().isNotEmpty() && edtSaldoCobros.text.toString().isNotEmpty() && edtFechaCobros.text.toString().isNotEmpty()
            && edtCobradorCobros.text.toString().isNotEmpty() && edtClienteCobros.text.toString().isNotEmpty()){
            val uid = intent.getStringExtra("uid")
            database = FirebaseDatabase.getInstance().getReference("cobros").child(uid.toString())
            val cobros = Cobros(uid.toString(), edtConceptoCobros.text.toString(), edtSaldoCobros.text.toString().toDouble(),
                edtFechaCobros.text.toString(), edtCobradorCobros.text.toString(), edtClienteCobros.text.toString())
            database.setValue(cobros).addOnCompleteListener {
                borrarShared()
                configProgressDialog()
            }.addOnFailureListener {
                Toast.makeText(this, "No se pudo guardar el cobro", Toast.LENGTH_SHORT).show()
            }
        }else{
            val contextView = findViewById<View>(R.id.layoutCobrosA)
            Snackbar.make(contextView, "Debe completar todos los campos", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun cargarDatos(){
        try {
            edtConceptoCobros.setText(intent.getStringExtra("concepto"))
            edtSaldoCobros.setText(intent.getStringExtra("saldo"))
            edtFechaCobros.setText(intent.getStringExtra("fecha"))
            edtCobradorCobros.setText(intent.getStringExtra("cobrador"))
            edtClienteCobros.setText(intent.getStringExtra("cliente"))
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
            Toast.makeText(this, "Cobro guardado", Toast.LENGTH_SHORT).show()
        }, 3000)
    }

    private fun selCliente(){
        //val nombreCliente = intent.getStringExtra("selNombre")
        //edtClienteCobros.setText(nombreCliente)
        val preferences = getSharedPreferences("clientesSel", MODE_PRIVATE)
        edtClienteCobros.setText(preferences.getString("selNombre", ""))
    }

    private fun selCobrador(){
        //val nombreCobrador = intent.getStringExtra("selNombreCob")
        //edtCobradorCobros.setText(nombreCobrador)
        val preferences = getSharedPreferences("cobradoresSel", MODE_PRIVATE)
        edtCobradorCobros.setText(preferences.getString("selNombreCob", ""))
    }

    private fun borrarShared(){
        val preferences = getSharedPreferences("clientesSel", MODE_PRIVATE)
        preferences.edit().clear().apply()
        val preferencesC = getSharedPreferences("cobradoresSel", MODE_PRIVATE)
        preferencesC.edit().clear().apply()
    }

    private fun showDatePickerDialog() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        edtFechaCobros.setText(sdf.format(myCalendar.time))
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
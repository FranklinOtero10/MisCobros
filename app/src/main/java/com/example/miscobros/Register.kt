package com.example.miscobros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var btnRegistrar: Button
    private lateinit var edtUsuario: TextInputEditText
    private lateinit var edtContra: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(findViewById(R.id.topAppBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Registrar Usuario"
        btnRegistrar = findViewById(R.id.btnRegistrar)
        edtUsuario = findViewById(R.id.edtUsuario1)
        edtContra = findViewById(R.id.edtContra1)

        btnRegistrar.setOnClickListener {
            if (edtUsuario.text.toString().isNotEmpty() && edtContra.text.toString().isNotEmpty()) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    edtUsuario.text.toString(),
                    edtContra.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //el registro fue un exito nos regresara al login
                        finish()
                        Toast.makeText(this, "Usuario registrado con exito!", Toast.LENGTH_SHORT).show()
                    } else {
                        showAlert()
                    }
                }
            }else{
                Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Finalizar la actividad
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*private fun AddUsuario() {
        title = "Autenticacion"

        //Funcion que permite registrarse en firebase
        btnRegistrar.setOnClickListener {
            if (edtUsuario.text.toString().isNotEmpty() && edtContra.text.toString().isNotEmpty()) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    edtUsuario.text.toString(),
                    edtContra.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //el registro fue un exito nos regresara al login
                        onBackPressed()
                    } else {
                        ShowAlertr()
                    }
                }
            }else{
                Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al registrarse")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }



}
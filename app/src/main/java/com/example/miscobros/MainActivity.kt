package com.example.miscobros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

 class MainActivity : AppCompatActivity() {

    //Autentificacion de firebase
    private lateinit var firebaseAuth: FirebaseAuth
    //lateinit var authStateListener: FirebaseAuth.AuthStateListener

    lateinit var ivLogo:ImageView
    lateinit var btnIniciarSesion: Button
    lateinit var btnRegistrarse: Button
    lateinit var edtUsuario: TextInputEditText
    lateinit var edtContra: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivLogo = findViewById(R.id.ivLogo)
        edtUsuario = findViewById(R.id.edtUsuario)
        edtContra = findViewById(R.id.edtContra)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)

        animacionLogo()

        //Inicializando la variable de Firebase
        firebaseAuth = Firebase.auth

        btnIniciarSesion.setOnClickListener {
            if (edtUsuario.text.toString().isNotEmpty() && edtContra.text.toString().isNotEmpty()){
                singIn(edtUsuario.text.toString(), edtContra.text.toString())
            }else{
                Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            }

        }
        //nos lleva al ventana para registrarnos
        btnRegistrarse.setOnClickListener {
            intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }

    //Funcion que permite iniciar sesion con firebase
    private fun singIn(correo:String, contra:String){
        firebaseAuth.signInWithEmailAndPassword(correo, contra)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    //Toast.makeText(baseContext, "Bienvenido ${user?.email.toString()}", Toast.LENGTH_SHORT).show()
                    //Aqui deberia ir al menu principal
                    intent = Intent(this, MenuPrincipalActivity::class.java)
                    intent.putExtra("correo", correo)
                    startActivity(intent)
                }else{
                    showAlert()
                }
            }
    }

    fun animacionLogo(){
        ivLogo.animate().apply {
            duration = 1000
            rotationY(360f)
        }.start()
    }

    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Usuario/Contraseña incorrectos")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

}
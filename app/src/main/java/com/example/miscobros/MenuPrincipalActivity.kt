package com.example.miscobros


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View


import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.example.miscobros.databinding.LayoutNavigationHeaderBinding



class MenuPrincipalActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var imgMenu: ImageView
    lateinit var menuNavegacion: NavigationView
    lateinit var navController: NavController
    //lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding: LayoutNavigationHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        drawerLayout = findViewById(R.id.drawerLayout)
        imgMenu = findViewById(R.id.imgMenu)
        menuNavegacion = findViewById(R.id.navigationView)
        menuNavegacion.itemIconTintList = null

        binding = LayoutNavigationHeaderBinding.inflate(layoutInflater)

        val correo : String? = intent.getStringExtra("correo")

        Toast.makeText(baseContext, "Bienvenido ${correo.toString()}", Toast.LENGTH_SHORT).show()

        binding.txvNombreUsuario.text = correo

        //Evento click para que se abra el menu lateral al dar clic al icono de las barras
        imgMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val titulo: TextView = findViewById(R.id.txvTitulo)

        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(menuNavegacion, navController)

        //Establecemos el titulo que trae la activity en el menu
        navController.addOnDestinationChangedListener { _, destination, _ ->
            titulo.text = destination.label
        }
    }
}
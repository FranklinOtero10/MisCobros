package com.example.miscobros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.miscobros.adapters.CobradorAdapter
import com.example.miscobros.adapters.SeleccionarClienteAdapter
import com.example.miscobros.adapters.SeleccionarCobradorAdapter
import com.example.miscobros.models.Cobrador
import com.google.firebase.database.*

class SeleccionarCobradorActivity : AppCompatActivity() {

    private lateinit var rcvSelListaCobradores : RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swRefres : SwipeRefreshLayout
    private lateinit var selCobradorAdapter: SeleccionarCobradorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_cobrador)

        rcvSelListaCobradores = findViewById(R.id.rcvSeleccionarCobrador)
        swRefres = findViewById(R.id.swRefreshSeleccionarCobrador)

        swRefres.setOnRefreshListener {
            configRecyclerView()
            // Configurando hilo, para asignar tiempo
            Handler(Looper.getMainLooper()).postDelayed({
                swRefres.isRefreshing = false
                Toast.makeText(this, "Lista actualizada", Toast.LENGTH_SHORT).show()
            }, 2000)
        }
        configRecyclerView()
    }

    private fun obtenerCobradores(){
        databaseReference = FirebaseDatabase.getInstance().getReference("cobradores")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lst: MutableList<Cobrador> = mutableListOf()
                if (snapshot.exists()){
                    for (cobradorSnap in snapshot.children){
                        val cobradorData = cobradorSnap.getValue(Cobrador::class.java)
                        lst.add(cobradorData!!)
                    }
                    selCobradorAdapter.setSelCobradores(lst)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun configRecyclerView(){
        selCobradorAdapter = SeleccionarCobradorAdapter(mutableListOf())
        linearLayoutManager = LinearLayoutManager(this)

        obtenerCobradores()

        rcvSelListaCobradores.apply {
            rcvSelListaCobradores.setHasFixedSize(true)
            rcvSelListaCobradores.layoutManager = linearLayoutManager
            rcvSelListaCobradores.adapter = selCobradorAdapter
        }
    }
}
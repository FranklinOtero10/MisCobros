package com.example.miscobros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.miscobros.adapters.SeleccionarClienteAdapter
import com.example.miscobros.models.Cliente
import com.google.firebase.database.*

class SeleccionarClienteActivity : AppCompatActivity() {

    private lateinit var rcvSelListaClientes : RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var selClienteAdapater : SeleccionarClienteAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swRefres : SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_cliente)

        rcvSelListaClientes = findViewById(R.id.rcvSeleccionarClientes)
        swRefres = findViewById(R.id.swRefreshSeleccionarCliente)

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

    private fun obtenerClientes(){
        databaseReference = FirebaseDatabase.getInstance().getReference("clientes")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lst: MutableList<Cliente> = mutableListOf()
                if (snapshot.exists()){
                    for (clienteSnap in snapshot.children){
                        val clienteData = clienteSnap.getValue(Cliente::class.java)
                        lst.add(clienteData!!)
                    }
                    selClienteAdapater.setSelClientes(lst)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun configRecyclerView(){
        selClienteAdapater = SeleccionarClienteAdapter(mutableListOf())
        linearLayoutManager = LinearLayoutManager(this)

        obtenerClientes()

        rcvSelListaClientes.apply {
            rcvSelListaClientes.setHasFixedSize(true)
            rcvSelListaClientes.layoutManager = linearLayoutManager
            rcvSelListaClientes.adapter = selClienteAdapater
        }
    }

}
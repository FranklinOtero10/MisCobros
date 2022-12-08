package com.example.miscobros


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.miscobros.adapters.ClienteAdapter
import com.example.miscobros.models.Cliente
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class ListaClientesFragment : Fragment() {

    private lateinit var btnAgregarCliente:FloatingActionButton
    private lateinit var rcvListaClientes : RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var clienteAdapater : ClienteAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swRefres : SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_clientes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcvListaClientes = view.findViewById(R.id.rcvListaClientes)
        btnAgregarCliente = view.findViewById(R.id.btnAgregarCliente)
        swRefres = view.findViewById(R.id.swRefreshCliente)

        btnAgregarCliente.setOnClickListener {
            val intent = Intent(view.context, AgregarClienteActivity::class.java)
            startActivity(intent)
        }

        swRefres.setOnRefreshListener {
            configRecyclerView()
            // Configurando hilo, para asignar tiempo
            Handler(Looper.getMainLooper()).postDelayed({
                swRefres.isRefreshing = false
                Toast.makeText(view.context, "Lista actualizada", Toast.LENGTH_SHORT).show()
            }, 2000)
        }
        configRecyclerView()
    }

    private fun obtenerClientes(){
        databaseReference = FirebaseDatabase.getInstance().getReference("clientes")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lst: MutableList<Cliente> = mutableListOf()
                if (snapshot.exists()){
                    for (clienteSnap in snapshot.children){
                        val clienteData = clienteSnap.getValue(Cliente::class.java)
                        lst.add(clienteData!!)
                    }
                    clienteAdapater.setClientes(lst)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun configRecyclerView(){
        clienteAdapater = ClienteAdapter(mutableListOf())
        linearLayoutManager = LinearLayoutManager(view?.context)

        obtenerClientes()

        rcvListaClientes.apply {
            rcvListaClientes.setHasFixedSize(true)
            rcvListaClientes.layoutManager = linearLayoutManager
            rcvListaClientes.adapter = clienteAdapater
        }
    }

}
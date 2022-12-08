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
import com.example.miscobros.adapters.CobrosAdapter
import com.example.miscobros.models.Cobros
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class ListasCobrosFragment : Fragment() {

    private lateinit var btnAgregarCobros: FloatingActionButton
    private lateinit var rcvListaCobros : RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var cobrosAdapter: CobrosAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swRefres : SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_cobros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAgregarCobros = view.findViewById(R.id.btnAgregarCobros)
        rcvListaCobros = view.findViewById(R.id.rcvListaCobros)
        swRefres = view.findViewById(R.id.swRefreshCobros)


        btnAgregarCobros.setOnClickListener {
            val intent = Intent(view.context, AgregarCobrosActivity::class.java)
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

    private fun obtenerCobros(){
        databaseReference = FirebaseDatabase.getInstance().getReference("cobros")

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lst: MutableList<Cobros> = mutableListOf()
                if (snapshot.exists()){
                    for (cobrosSnap in snapshot.children){
                        val cobrosData = cobrosSnap.getValue(Cobros::class.java)
                        lst.add(cobrosData!!)
                    }
                    cobrosAdapter.setCobros(lst)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun configRecyclerView() {
        cobrosAdapter = CobrosAdapter(mutableListOf())
        linearLayoutManager = LinearLayoutManager(view?.context)

        obtenerCobros()

        rcvListaCobros.apply {
            rcvListaCobros.setHasFixedSize(true)
            rcvListaCobros.layoutManager = linearLayoutManager
            rcvListaCobros.adapter = cobrosAdapter
        }
    }
}
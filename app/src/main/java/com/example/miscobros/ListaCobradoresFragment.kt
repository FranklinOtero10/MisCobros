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
import com.example.miscobros.adapters.CobradorAdapter
import com.example.miscobros.models.Cobrador
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*


class ListaCobradoresFragment : Fragment() {

    private lateinit var btnAgregarCobrador: FloatingActionButton
    private lateinit var rcvCobrador: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var cobradorAdapter: CobradorAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swRefres : SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_cobradores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAgregarCobrador = view.findViewById(R.id.btnAgregarCobrador)
        rcvCobrador = view.findViewById(R.id.rcvListaCobradores)
        swRefres = view.findViewById(R.id.swRefreshCobrador)

        btnAgregarCobrador.setOnClickListener {
            val intent = Intent(view.context, AgregarCobradorActivity::class.java)
            startActivity(intent)
        }

        swRefres.setOnRefreshListener {
            configRecyclerView()
            Handler(Looper.getMainLooper()).postDelayed({
                swRefres.isRefreshing = false
                Toast.makeText(view.context, "Lista actualizada", Toast.LENGTH_SHORT).show()
            }, 2000)
        }
        configRecyclerView()
    }

    private fun obtenerCobradores(){
        databaseReference = FirebaseDatabase.getInstance().getReference("cobradores")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lst: MutableList<Cobrador> = mutableListOf()
                if (snapshot.exists()){
                    for (cobradorSnap in snapshot.children){
                        val cobradorData = cobradorSnap.getValue(Cobrador::class.java)
                        lst.add(cobradorData!!)
                    }
                    cobradorAdapter.setCobradores(lst)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun configRecyclerView(){
        cobradorAdapter = CobradorAdapter(mutableListOf())
        linearLayoutManager = LinearLayoutManager(view?.context)

        obtenerCobradores()
        rcvCobrador.apply {
            rcvCobrador.setHasFixedSize(true)
            rcvCobrador.layoutManager = linearLayoutManager
            rcvCobrador.adapter =cobradorAdapter
        }
    }
}
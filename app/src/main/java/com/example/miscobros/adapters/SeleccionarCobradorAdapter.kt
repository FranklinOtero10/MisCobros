package com.example.miscobros.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.miscobros.AgregarCobrosActivity
import com.example.miscobros.R
import com.example.miscobros.databinding.ItemSeleccionarCobradorListBinding
import com.example.miscobros.models.Cobrador

class SeleccionarCobradorAdapter(private var lstSelCobrador: MutableList<Cobrador>)
    : RecyclerView.Adapter<SeleccionarCobradorAdapter.ViewHolder>(){

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_seleccionar_cobrador_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lstSelCobrador[position]
        val cobrador: Cobrador = item
        with(holder){
            binding.txvSelNombreCobradorItem.text = cobrador.nombres
            binding.txvSelDireccionCobradorItem.text = cobrador.direccion
            binding.txvSelTelCobradorItem.text = cobrador.telefono
            binding.txvSelCorreoCobradorItem.text = cobrador.email

            binding.btnSelAddCobrador.setOnClickListener {
                /*val intent = Intent(itemView.context, AgregarCobrosActivity::class.java)
                intent.putExtra("selNombreCob", cobrador.nombres)
                itemView.context.startActivity(intent)*/

                val preferences = itemView.context.getSharedPreferences("cobradoresSel", Context.MODE_PRIVATE)
                with(preferences.edit()){
                    putString("selNombreCob", cobrador.nombres).apply()
                }
                Toast.makeText(itemView.context, "Cobrador Seleccionado, presione actualizar", Toast.LENGTH_SHORT).show()
                (context as Activity).finish()
            }
        }
    }

    fun setSelCobradores(cobradores: MutableList<Cobrador>){
        this.lstSelCobrador = cobradores
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = lstSelCobrador.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSeleccionarCobradorListBinding.bind(view)
    }

}
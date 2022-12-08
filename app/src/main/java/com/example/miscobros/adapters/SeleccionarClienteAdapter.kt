package com.example.miscobros.adapters

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.miscobros.R
import com.example.miscobros.SeleccionarClienteActivity
import com.example.miscobros.databinding.ItemSeleccionarClienteListBinding
import com.example.miscobros.models.Cliente

class SeleccionarClienteAdapter(private var lstSeleccionarClientes: MutableList<Cliente>)
    : RecyclerView.Adapter<SeleccionarClienteAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_seleccionar_cliente_list, parent, false )

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lstSeleccionarClientes[position]
        val cliente : Cliente = item
        with(holder){
            binding.txvSelNombreClienteItem.text = cliente.nombres
            binding.txvSelDireccionClienteItem.text = cliente.direccion
            binding.txvSelTelClienteItem.text = cliente.telefono
            binding.txvSelCorreoClienteItem.text = cliente.email

            binding.btnSelAddCliente.setOnClickListener {
                /*val intent = Intent(itemView.context, AgregarCobrosActivity::class.java)
                intent.putExtra("selNombre", cliente.nombres)
                itemView.context.startActivity(intent)*/

                val preferences = itemView.context.getSharedPreferences("clientesSel", MODE_PRIVATE)
                with(preferences.edit()){
                    putString("selNombre", cliente.nombres).apply()
                }
                Toast.makeText(itemView.context, "Cliente Seleccionado, presione actualizar", Toast.LENGTH_SHORT).show()
                (context as Activity).finish()
            }
        }
    }

    fun setSelClientes(clientes: MutableList<Cliente>){
        this.lstSeleccionarClientes = clientes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = lstSeleccionarClientes.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSeleccionarClienteListBinding.bind(view)
    }

}
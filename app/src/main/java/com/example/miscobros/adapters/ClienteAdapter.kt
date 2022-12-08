package com.example.miscobros.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.miscobros.AgregarClienteActivity
import com.example.miscobros.R
import com.example.miscobros.databinding.ItemClienteListBinding
import com.example.miscobros.models.Cliente
import com.google.firebase.database.FirebaseDatabase


class ClienteAdapter(private var lstClientes: MutableList<Cliente>)
    : RecyclerView.Adapter<ClienteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente_list, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lstClientes[position]
        val cliente : Cliente = item
        with(holder){
            binding.txvNombreClienteItem.text = cliente.nombres
            binding.txvDireccionClienteItem.text = cliente.direccion
            binding.txvTelClienteItem.text = cliente.telefono
            binding.txvCorreoClienteItem.text = cliente.email

            binding.btnElmClienteItem.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("¡Advertencia!")
                    .setMessage("¿Desea eliminar el registro?")
                    .setPositiveButton("Ok"){_, _, ->
                        borrarCliente(cliente.uId)}
                    .setNegativeButton("Cancelar", null)
                    .show()
            }

            binding.btnModClienteItem.setOnClickListener {
                val intent = Intent(itemView.context, AgregarClienteActivity::class.java)
                intent.putExtra("estado", 1)
                intent.putExtra("uid", cliente.uId)
                intent.putExtra("nombre", cliente.nombres)
                intent.putExtra("direccion", cliente.direccion)
                intent.putExtra("telefono", cliente.telefono)
                intent.putExtra("correo", cliente.email)
                itemView.context.startActivity(intent)
            }
        }
    }

    fun setClientes(clientes: MutableList<Cliente>){
        this.lstClientes = clientes
        notifyDataSetChanged()
    }

    fun borrarCliente(uId: String){
        val dbReference = FirebaseDatabase.getInstance().getReference("clientes").child(uId)
        dbReference.removeValue()
    }

    override fun getItemCount(): Int = lstClientes.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemClienteListBinding.bind(view)
    }

}
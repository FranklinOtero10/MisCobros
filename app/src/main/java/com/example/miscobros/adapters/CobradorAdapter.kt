package com.example.miscobros.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.miscobros.AgregarCobradorActivity
import com.example.miscobros.R
import com.example.miscobros.databinding.ItemCobradorListBinding
import com.example.miscobros.models.Cobrador
import com.google.firebase.database.FirebaseDatabase

class CobradorAdapter(private var lstCobrador: MutableList<Cobrador>)
    : RecyclerView.Adapter<CobradorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cobrador_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lstCobrador[position]
        val cobrador: Cobrador = item

        with(holder){
            binding.txvNombreCobradorItem.text = cobrador.nombres
            binding.txvDireccionCobradorItem.text = cobrador.direccion
            binding.txvTelCobradorItem.text = cobrador.telefono
            binding.txvCorreoCobradorItem.text = cobrador.email

            binding.btnElmCobradorItem.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("¡Advertencia!")
                    .setMessage("¿Desea eliminar el registro?")
                    .setPositiveButton("Ok"){_, _, ->
                        borrarCobrador(cobrador.uId)}
                    .setNegativeButton("Cancelar", null)
                    .show()
            }

            binding.btnModCobradorItem.setOnClickListener {
                val intent = Intent(itemView.context, AgregarCobradorActivity::class.java)
                intent.putExtra("estado", 1)
                intent.putExtra("uid", cobrador.uId)
                intent.putExtra("nombre", cobrador.nombres)
                intent.putExtra("direccion", cobrador.direccion)
                intent.putExtra("telefono", cobrador.telefono)
                intent.putExtra("correo", cobrador.email)
                itemView.context.startActivity(intent)
            }
        }
    }

    fun setCobradores(cobradores: MutableList<Cobrador>){
        this.lstCobrador = cobradores
        notifyDataSetChanged()
    }

    fun borrarCobrador(uId: String){
        val dbReference = FirebaseDatabase.getInstance().getReference("cobradores").child(uId)
        dbReference.removeValue()
    }

    override fun getItemCount(): Int = lstCobrador.size

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCobradorListBinding.bind(view)
    }
}
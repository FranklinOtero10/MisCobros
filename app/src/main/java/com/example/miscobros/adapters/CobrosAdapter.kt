package com.example.miscobros.adapters


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.miscobros.AgregarCobrosActivity
import com.example.miscobros.R
import com.example.miscobros.databinding.ItemCobrosListBinding
import com.example.miscobros.models.Cobros
import com.google.firebase.database.FirebaseDatabase

class CobrosAdapter(private var lstCobros: MutableList<Cobros>)
    : RecyclerView.Adapter<CobrosAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cobros_list, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lstCobros[position]
        val cobros: Cobros = item
        with(holder){
            binding.txvFechaCobroItem.text = cobros.fechaCobro
            binding.txvConceptoCobroItem.text = cobros.concepto
            binding.txvSaldoCobrosItem.text = cobros.saldoPagar.toString()
            binding.txvClienteCobrosItem.text = cobros.cliente
            binding.txvCobradorCobrosItem.text = cobros.cobrador

            binding.btnElmClienteItem.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("¡Advertencia!")
                    .setMessage("¿Desea eliminar el registro?")
                    .setPositiveButton("Ok"){_, _, ->
                        borrarCobro(cobros.uId)}
                    .setNegativeButton("Cancelar", null)
                    .show()
            }

            binding.btnModClienteItem.setOnClickListener {
                val intent = Intent(itemView.context, AgregarCobrosActivity::class.java)
                intent.putExtra("estado", 1)
                intent.putExtra("uid", cobros.uId)
                intent.putExtra("concepto", cobros.concepto)
                intent.putExtra("saldo", cobros.saldoPagar.toString())
                intent.putExtra("fecha", cobros.fechaCobro)
                intent.putExtra("cobrador", cobros.cobrador)
                intent.putExtra("cliente", cobros.cliente)
                itemView.context.startActivity(intent)
            }
        }
    }

    fun setCobros(cobros: MutableList<Cobros>){
        this.lstCobros = cobros
        notifyDataSetChanged()
    }

    fun borrarCobro(uId: String){
        val dbReference = FirebaseDatabase.getInstance().getReference("cobros").child(uId)
        dbReference.removeValue()
    }

    override fun getItemCount(): Int = lstCobros.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCobrosListBinding.bind(view)
    }
}
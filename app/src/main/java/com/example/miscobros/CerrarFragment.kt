package com.example.miscobros

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class CerrarFragment : Fragment() {

    lateinit var opcCerrar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cerrar, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cerrarSesion()
        super.onViewCreated(view, savedInstanceState)
    }

    fun cerrarSesion(){
        val builder= AlertDialog.Builder(requireActivity())
        builder.setTitle("¿Cerrar Sesión?")
        builder.setMessage("¿Desea cerrar la sesión?")
        builder.setPositiveButton("Aceptar",DialogInterface.OnClickListener { dialogInterface, i ->
            FirebaseAuth.getInstance().signOut()
            activity?.finish()
        })
        builder.setNegativeButton("Cancelar", null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        opcCerrar = view.findViewById(R.id.opcCerrarSesion)

        opcCerrar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //onAttach(MainActivity contex)


        }



    }


    ///metodo alternativo para regresar
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press or
                    // write your code which you want
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.opcCerrarSesion->{
                FirebaseAuth.getInstance().signOut()
                getActivity()?.onBackPressed();
              // onBackPressed()

            }
        }
        return super.onOptionsItemSelected(item)
    }
   */
/* fun onBackPressed(): Boolean {
        return true
    }*//*


*/
}
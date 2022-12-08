package com.example.miscobros

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class AcercaDeFragment : Fragment() {

    lateinit var imvLogo: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acerca_de, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imvLogo = view.findViewById(R.id.ivLogoAcercaDe)
        animacionLogo()
    }

    fun animacionLogo(){
        imvLogo.animate().apply {
            duration = 1000
            rotationY(360f)
        }.start()
    }
}
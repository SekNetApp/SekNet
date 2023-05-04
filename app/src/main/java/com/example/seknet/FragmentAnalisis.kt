package com.example.seknet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.seknet.databinding.FragmentAnalisisBinding
import com.example.seknet.databinding.FragmentPortscanBinding

class FragmentAnalisis : Fragment(R.layout.fragment_analisis) {

    private lateinit var binding : FragmentAnalisisBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalisisBinding.bind(view)
        activity?.title = "ANALISIS";
    }
}
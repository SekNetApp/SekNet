package com.example.seknet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton


class FragmentHome : Fragment(R.layout.fragment_home) {

    private lateinit var btnPortScanner: MaterialButton
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*val someInt = requireArguments().getInt("some_int")*/
        initComponents()
        initListeners()
    }

    fun initComponents() {
        btnPortScanner = requireView().findViewById<MaterialButton>(R.id.btnPortScanner)
    }

    fun initListeners() {
        btnPortScanner.setOnClickListener {
            val intent = Intent(requireContext(), PortScanActivity::class.java)
            startActivity(intent)
        }
    }
}

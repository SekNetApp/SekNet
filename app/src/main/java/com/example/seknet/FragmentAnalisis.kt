package com.example.seknet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class FragmentAnalisis : Fragment(R.layout.fragment_analisis) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = "ANALISIS";
    }
}
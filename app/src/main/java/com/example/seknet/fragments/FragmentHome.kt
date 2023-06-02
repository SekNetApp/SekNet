package com.example.seknet.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.seknet.BuildConfig
import com.example.seknet.R
import com.example.seknet.databinding.FragmentHomeBinding


class FragmentHome : Fragment(R.layout.fragment_home) {

    private lateinit var binding : FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        activity?.title = "HOME"
        binding.appVersion.text = "Version: " + BuildConfig.VERSION_NAME.toString()
    }
}

package com.example.seknet.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.seknet.R
import com.example.seknet.databinding.FragmentSpeedtestBinding

class FragmentSpeedtest : Fragment(R.layout.fragment_speedtest) {
    private lateinit var binding : FragmentSpeedtestBinding
    private lateinit var webView : WebView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSpeedtestBinding.bind(view)
        activity?.title = "SPEEDTEST"
        setWebView()
    }

    private fun setWebView(){
        webView = binding.webWindow
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://speedsmart.net/")
    }
}
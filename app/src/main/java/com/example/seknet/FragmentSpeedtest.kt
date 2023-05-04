package com.example.seknet

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.seknet.databinding.FragmentSpeedtestBinding

class FragmentSpeedtest : Fragment(R.layout.fragment_speedtest) {
    private lateinit var binding : FragmentSpeedtestBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSpeedtestBinding.inflate(layoutInflater)
        binding.speedTestButton.setOnClickListener {

            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nc = cm.getNetworkCapabilities(cm.activeNetwork)
            val downSpeed = (nc?.linkDownstreamBandwidthKbps)?.div(1000)
            val upSpeed = (nc?.linkUpstreamBandwidthKbps)?.div(1000)
            Toast.makeText(context,
                "Up Speed: $upSpeed Mbps \nDown Speed: $downSpeed Mbps",
                Toast.LENGTH_LONG).show()
        }
    }
}
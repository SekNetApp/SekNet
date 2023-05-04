package com.example.seknet

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.seknet.databinding.FragmentPortscanBinding
import com.example.seknet.databinding.FragmentSpeedtestBinding
class FragmentSpeedtest : Fragment(R.layout.fragment_speedtest) {
    private lateinit var binding : FragmentSpeedtestBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSpeedtestBinding.bind(view)
        activity?.title = "SPEEDTEST"

        binding.speedTestButton.setOnClickListener {
            val cm = activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nc = cm.getNetworkCapabilities(cm.activeNetwork)
            val downSpeed = (nc?.linkDownstreamBandwidthKbps)?.div(1000)
            val upSpeed = (nc?.linkUpstreamBandwidthKbps)?.div(1000)
            binding.speedTestResultsDownload.setText(downSpeed.toString())
            binding.speedTestResultsUpload.setText(upSpeed.toString())
        }
    }
}
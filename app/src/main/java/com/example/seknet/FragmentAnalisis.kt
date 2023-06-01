package com.example.seknet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.seknet.databinding.FragmentAnalisisBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.NetworkInterface


class FragmentAnalisis : Fragment() {
    private lateinit var binding: FragmentAnalisisBinding
    private lateinit var resultAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalisisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalisisBinding.bind(view)
        activity?.title = "NETWORK ANALIZER"
        setAdapter()
        setListeners()

    }

    private fun setAdapter() {
        resultAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        binding.lvAnalyzeResults.adapter = resultAdapter
    }

    private fun setListeners() {
        binding.btnAnalyze.setOnClickListener {
            resultAdapter.clear()
            performNetworkAnalysis()
        }

        binding.btnClear.setOnClickListener {
            resultAdapter.clear()
        }
    }

    private fun performNetworkAnalysis() {
        CoroutineScope(Dispatchers.IO).launch {
            val networkInterfaces =
                withContext(Dispatchers.IO) {
                    NetworkInterface.getNetworkInterfaces()
                }
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()

                if (networkInterface.displayName != "wlan0" || !networkInterface.isUp || networkInterface.isLoopback) {
                    continue
                }

                val interfaceAddresses = networkInterface.interfaceAddresses
                for (interfaceAddress in interfaceAddresses) {
                    val address = interfaceAddress.address
                    val inetAddress = address as? InetAddress ?: continue
                    val ip = inetAddress.hostAddress
                    val ipAddress =
                        if (ip.contains('%')) ip.substringBefore('%') else ip // Remove scope id if present
                    addResult("IP Address: $ipAddress")
                }
            }
        }
    }

    private suspend fun addResult(result: String) = withContext(Dispatchers.Main) {
        resultAdapter.add(result)
    }

}


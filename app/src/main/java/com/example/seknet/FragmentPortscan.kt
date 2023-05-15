package com.example.seknet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.seknet.databinding.FragmentPortscanBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject.NULL
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class FragmentPortscan : Fragment(R.layout.fragment_portscan) {

    private lateinit var binding: FragmentPortscanBinding
    private lateinit var btnScan: Button
    private lateinit var etTarget: EditText
    private lateinit var tvResult: TextView
    private lateinit var etStartPort: EditText
    private lateinit var etEndPort: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPortscanBinding.bind(view)
        activity?.title = "PORTSCAN"
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnScan = binding.btnScan
        tvResult = binding.tvResult
        etStartPort = binding.etStartPort
        etEndPort = binding.etEndPort
        etTarget = binding.etTarget
    }

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        btnScan.setOnClickListener {
            onScanButtonClicked()
        }
    }

    private suspend fun portScan(ipAddress: String, startPort: Int, endPort: Int): List<Int> = withContext(Dispatchers.IO) {
        val openPorts = mutableListOf<Int>()
        for (port in startPort..endPort) {
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress(ipAddress, port), 1000)
                socket.close()
                openPorts.add(port)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return@withContext openPorts

    }

@SuppressLint("SetTextI18n")
private fun onScanButtonClicked() {
    lifecycleScope.launch {
        try {
            val ipAddress = etTarget.toString()
            val startPort = etStartPort.toString().toIntOrNull()
            val endPort = etEndPort.toString().toIntOrNull()
            val openPorts = startPort?.let {
                if (endPort != null) {
                    portScan(ipAddress, it, endPort)
                }
            }
            if(openPorts.toString() == NULL) {
                tvResult.text = "ALL CLOSE"
            } else {
                tvResult.text = "Los puertos abiertos son: " + openPorts.toString()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

}

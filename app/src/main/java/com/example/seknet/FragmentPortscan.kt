package com.example.seknet

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.seknet.databinding.FragmentPortscanBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class FragmentPortscan : Fragment(R.layout.fragment_portscan) {

    private lateinit var binding: FragmentPortscanBinding
    private lateinit var btnScanTarget: Button
    private lateinit var btnScanRange: Button
    private lateinit var etTarget: EditText
    private lateinit var tvResultRange: TextView
    private lateinit var etFrom: EditText
    private lateinit var etTo: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPortscanBinding.inflate(layoutInflater)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnScanTarget = binding.btnScanTarget
        btnScanRange = binding.btnScanRange
        tvResultRange = binding.tvResultRange
        etTarget = binding.etFrom
        etFrom = binding.etFrom
        etTo = binding.etTo
    }

    private fun initListeners() {
        btnScanTarget.setOnClickListener {
            val target = etTarget.text.toString().toInt()
            scanTarget(target)
        }

        btnScanRange.setOnClickListener {
            val from = etFrom.text.toString().toInt()
            val to = etTo.text.toString().toInt()
            scanRange(from, to)
        }
    }
    @SuppressLint("SetTextI18n")
    fun scanTarget(target: Int) {
        val result = binding.tvResultTarget
        try {
            // Get the IP address of the local device
            val localhost = InetAddress.getLocalHost()
            // Define the port number to scan "target"
            // Create a new socket and attempt to connect to the port
            val socket = Socket()
            socket.connect(InetSocketAddress(localhost.hostAddress, target), 1000)
            result.setTextColor(Color.GREEN)
            result.text = "abierto"
            // Close the socket
            socket.close()
        } catch (e: Exception) {
            result.setTextColor(Color.RED)
            result.text = "cerrado"
        }
    }

    @SuppressLint("SetTextI18n")
    fun scanRange(from: Int, to: Int) {
        val result = tvResultRange
        try {
            // Get the IP address of the local device
            val localhost = InetAddress.getLocalHost()
            // Loop through the specified port range
            for (port in from..to) {
                try {
                    // Create a new socket and attempt to connect to the port
                    val socket = Socket()
                    runBlocking {
                        val job = launch(Dispatchers.Default) {
                            withContext(Dispatchers.IO) {
                                socket.connect(InetSocketAddress(localhost.hostAddress, port), 1000)
                            }
                            result.setTextColor(Color.GREEN)
                            result.text = "$port abierto"
                        }
                    }
                    // Close the socket
                    socket.close()
                } catch (e: Exception) {
                    result.setTextColor(Color.RED)
                    result.text = "cerrado"
                }
            }
        } catch (e: Exception) {
            result.setTextColor(Color.RED)
            result.text = "cerrados"
        }
    }
}
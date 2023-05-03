package com.example.seknet

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class PortScanActivity : AppCompatActivity() {

    private lateinit var btnScanTarget: Button
    private lateinit var btnScanRange: Button
    private lateinit var etTarget: EditText
    private lateinit var etFrom: EditText
    private lateinit var etTo: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_port_scan)
        initComponents()
        initListeners()
    }

    fun initComponents() {
        btnScanTarget = findViewById(R.id.btnScanTarget)
        btnScanRange = findViewById(R.id.btnScanRange)
        etTarget = findViewById(R.id.etTarget)
        etFrom = findViewById(R.id.etFrom)
        etTo = findViewById(R.id.etTo)
    }

    fun initListeners() {
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
        Thread {
            val result = findViewById<TextView>(R.id.tvResultTarget)
            try {
                // Get the IP address of the local device
                val localhost = InetAddress.getLocalHost()
                // Define the port number to scan
                val port = target
                // Create a new socket and attempt to connect to the port
                val socket = Socket()
                socket.connect(InetSocketAddress(localhost.hostAddress, port), 1000)
                runOnUiThread {
                    result.setTextColor(Color.GREEN)
                    result.text = "abierto"
                }
                // Close the socket
                socket.close()
            } catch (e: Exception) {
                runOnUiThread {
                    result.setTextColor(Color.RED)
                    result.text = "cerrado"
                }
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    fun scanRange(from: Int, to: Int) {
        Thread (Runnable{
            val result = findViewById<TextView>(R.id.tvResultRange)
            try {
                // Get the IP address of the local device
                val localhost = InetAddress.getLocalHost()
                // Loop through the specified port range
                for (port in from..to) {
                    try {
                        // Create a new socket and attempt to connect to the port
                        val socket = Socket()
                        socket.connect(InetSocketAddress(localhost.hostAddress, port), 1000)
                        runOnUiThread {
                            result.setTextColor(Color.GREEN)
                            result.text = "$port abierto"
                        }
                        // Close the socket
                        socket.close()
                    } catch (e: Exception) {
                        runOnUiThread {
                            result.setTextColor(Color.RED)
                            result.text = "cerrado"
                        }
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    result.setTextColor(Color.RED)
                    result.text = "cerrados"
                }
            }
        }).start()
    }
}
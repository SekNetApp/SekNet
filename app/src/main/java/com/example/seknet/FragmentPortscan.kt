package com.example.seknet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.seknet.databinding.FragmentPortscanBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class FragmentPortscan : Fragment(R.layout.fragment_portscan) {

    private lateinit var binding: FragmentPortscanBinding
    private var openPorts: ArrayList<String> = arrayListOf()
    private lateinit var portResultList: ArrayAdapter<String>
    private lateinit var listView: ListView
    private lateinit var s: Socket
    private var ip: String = ""
    private var to: Int = 0
    private var from: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPortscanBinding.bind(view)
        activity?.title = "PORTSCAN"
        setAdapter()
        initListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        binding.portscanButtonScan.setOnClickListener {
            if (emptyFields()) {
                binding.portscanButtonScan.isEnabled = false
                setPortParameters()
            }
        }
        binding.portscanButtonClear.setOnClickListener {
            portResultList.clear()
        }
    }

    private fun setPortParameters() {
        ip = binding.portscanTargetHost.text.toString()
        from = binding.portscanFromPort.text.toString().toInt()
        to = binding.portscanToPort.text.toString().toInt()
        val sb = Snackbar.make(requireView(), "Analizando...$ip", Snackbar.LENGTH_LONG)
        sb.show()
        lifecycleScope.launch {
            onScanButtonClicked(ip, from, to)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun onScanButtonClicked(ip: String, from: Int, to: Int) =
        withContext(Dispatchers.IO.limitedParallelism(1)) {
            for (port in from..to) {
                try {
                    s = Socket()
                    s.connect(InetSocketAddress(ip, port), 2000)
                    if (s.isConnected) {
                        requireActivity().runOnUiThread {
                            openPorts.add("Host $ip is OPEN on port $port")
                            portResultList.notifyDataSetChanged()
                        }
                        s.close()
                    }
                } catch (e: IOException) {
                    requireActivity().runOnUiThread {
                        openPorts.add("Host $ip is CLOSED on port $port")
                        portResultList.notifyDataSetChanged()
                    }
                } catch (e: Error) {
                    e.printStackTrace()
                    requireActivity().runOnUiThread {
                        portResultList.notifyDataSetChanged()
                    }
                    val sb =
                        Snackbar.make(requireView(), "Error desconocido", Snackbar.LENGTH_SHORT)
                    sb.show()
                }
            }
            activity?.runOnUiThread {
                finnishScan()
            }
        }

    private fun setAdapter() {
        portResultList = ArrayAdapter(requireActivity(), R.layout.listview_item, openPorts)
        listView = binding.portscanResultList
        listView.adapter = portResultList

    }

    private fun emptyFields(): Boolean {
        if (binding.portscanTargetHost.text.toString()
                .isEmpty() || binding.portscanFromPort.text.toString()
                .isEmpty() || binding.portscanToPort.text.toString().isEmpty()
        ) {
            val sb =
                Snackbar.make(requireView(), "Rellene los campos vacios", Snackbar.LENGTH_SHORT)
            sb.show()
            return false
        }
        if ((binding.portscanFromPort.text).toString().toInt() >= (binding.portscanToPort.text).toString().toInt()
        ) {
            val sb =
                Snackbar.make(
                    requireView(),
                    "Indique un rango valido de puertos desde < hasta",
                    Snackbar.LENGTH_SHORT
                )
            sb.show()
            return false
        }
        return true
    }

    private fun finnishScan() {
        val sb =
            Snackbar.make(requireView(), "Analisis finalizado", Snackbar.LENGTH_SHORT)
        sb.show()
        binding.portscanButtonScan.isEnabled = true

    }
}

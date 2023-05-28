package com.example.seknet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.seknet.databinding.FragmentPortscanBinding
import kotlinx.coroutines.Dispatchers
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
            setPortParameters()
        }
        binding.portscanButtonClear.setOnClickListener {
            portResultList.clear()
        }
    }

    private fun setPortParameters() {
        ip = binding.portscanTargetHost.text.toString()
        from = binding.portscanFromPort.text.toString().toInt()
        to = binding.portscanToPort.text.toString().toInt()
        lifecycleScope.launch {
            onScanButtonClicked(ip, from, to)
        }
    }

    private suspend fun onScanButtonClicked(ip: String, from: Int, to: Int) =
        withContext(Dispatchers.IO) {
            for (port in from..to) {
                try {
                    s = Socket()
                    s.connect(InetSocketAddress(ip, port), 2000)
                    if (s.isConnected) {
                        openPorts.add("Host $ip is OPEN on port $port")
                        requireActivity().runOnUiThread {
                            portResultList.notifyDataSetChanged()
                        }
                        s.close()
                    }
                } catch (e: IOException) {
                    openPorts.add("Host $ip is CLOSED on port $port")
                    requireActivity().runOnUiThread {
                        portResultList.notifyDataSetChanged()
                    }
                    s.close()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        }

    private fun setAdapter() {

        portResultList = ArrayAdapter(requireActivity(), R.layout.listview_item, openPorts)
        listView = binding.portscanResultList
        listView.adapter = portResultList

    }
}

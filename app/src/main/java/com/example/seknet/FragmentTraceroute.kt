package com.example.seknet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.seknet.databinding.FragmentTracerouteBinding
import utils.Ping
import utils.PingExt
import java.net.UnknownHostException


class FragmentTraceroute : Fragment(R.layout.fragment_traceroute) {
    private lateinit var binding : FragmentTracerouteBinding
    private lateinit var out : TextView
    private var stopProccess : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTracerouteBinding.bind(view)
        activity?.title = "TRACEROUTE"
        out = binding.output
        setListeners()
    }

    private fun setListeners() {
        binding.tracertButtonStart.setOnClickListener {
            try {
                traceroute(
                    requireContext(),
                    binding.tracertTargetHost.text.toString())
            } catch (e: RuntimeException) {

            }
        }
        binding.tracertButtonStop.setOnClickListener {
            stopProccess = true
        }
    }

    private fun traceroute(context: Context, host: String, count: Int = 30) {
        try {
            val address = Ping.getHostByName(host)
            if (address == null) {
                out.append("Unknown host: ")
                out.append(host)
                return
            }
            out.append(
                String.format(
                    "traceroute to %s (%s), 30 hops max",
                    host,
                    address.hostAddress
                )
            )
        } catch (e: UnknownHostException) {

        }
        out.append("\n")
        while(stopProccess) {
            for (i in 1..count) {
                val p1 = newPing(context, host, i)
                p1.ping()
                out.append(String.format("%d  ", i))
                if (p1.fail()) {
                    out.append("* ")
                } else {

                    if (p1.hoop != null) {
                        out.append(String.format("%s %s  ", p1.hoop, p1.hoopIp))
                    } else {
                        out.append(String.format("%s  ", p1.hoopIp))
                        out.append(String.format("%d ms ", p1.ms))
                    }
                }
            }
        }
        stopProccess = false
    }

    private fun newPing(context: Context?, host: String?, i: Int): Ping {
        val shared: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return if (shared.getBoolean("ping", false)) Ping(host, i) else PingExt(host, i)
    }
}

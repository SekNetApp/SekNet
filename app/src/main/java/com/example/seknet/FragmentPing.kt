package com.example.seknet

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.seknet.databinding.FragmentPingBinding
import com.github.axet.androidlibrary.widgets.ErrorDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import utils.Ping
import utils.PingExt


class FragmentPing : Fragment(R.layout.fragment_ping) {
    private lateinit var binding: FragmentPingBinding
    private var out: StringBuilder = StringBuilder()
    private val job: Job = Job()
    var TAG: String = FragmentPing::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPingBinding.bind(view)
        activity?.title = "PING"
        binding.output.text = out
        setListeners()
    }

    private fun setListeners() {
        binding.pingButtonStart.setOnClickListener {
                ping(
                    requireContext(),
                    binding.pingTargetHost.text.toString(),
                    binding.pingTargetCount.text.toString().toInt(),
                    out
                )
            binding.pingButtonClear.setOnClickListener {
                out.clear()
                binding.output.text = out
            }
        }
    }

    private fun newPing(context: Context, host: String?): Ping {
        val shared: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return if (shared.getBoolean("ping", false)) Ping(host) else PingExt(host)
    }

    @SuppressLint("DefaultLocale")
    private fun ping(
        context: Context,
        host: String?,
        count: Int,
        out: StringBuilder
    ) {
        try {
            val ping: Ping = newPing(context, host)
            var sent = 0
            var lost = 0
            var min = Double.MAX_VALUE
            var max = 0.0
            var avg = 0.0
            var mdev = 0.0
            var tsum = 0.0
            var tsum2 = 0.0
            val start = System.currentTimeMillis()
            val sb = Snackbar.make(requireView(), "Quering", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
            sb.show()
            for (i in 0 until count) {
                sent++
                ping.ping()
                if (sent == 1) {
                    this.out.append(java.lang.String.format("PING %s (%s).", host, ping.ip))
                    this.out.append("\n")
                    updateOut()
                }
                val sec: Double = ping.ms / 1000.0
                if (!ping.fail()) {
                    this.out.append(
                        java.lang.String.format(
                            "%s (%s): time=%.2f ms",
                            host,
                            ping.ip,
                            sec
                        )
                    )
                    updateOut()
                } else if (ping.unreachable) {
                    lost++
                    this.out.append(
                        java.lang.String.format(
                            "Destination unreachable %s (%s): time=%.2f ms",
                            host,
                            ping.hoopIp,
                            sec
                        )
                    )
                    updateOut()
                } else {
                    lost++
                    this.out.append(
                        java.lang.String.format(
                            "Timeout %s (%s): time=%.2f ms",
                            host,
                            ping.ip,
                            sec
                        )
                    )
                    updateOut()
                }
                avg += sec
                tsum += sec
                tsum2 += sec * sec
                min = min.coerceAtMost(sec)
                max = max.coerceAtLeast(sec)
                this.out.append("\n")
            }
            updateOut()
            avg = avg / sent
            val recived = sent - lost
            tsum /= (recived + sent).toDouble()
            tsum2 /= (recived + sent).toDouble()
            mdev = Math.sqrt(tsum2 - tsum * tsum)
            val end = System.currentTimeMillis()
            this.out.append("\n")
            this.out.append(String.format("--- %s ping statistics ---", host))
            this.out.append("\n")
            this.out.append(
                String.format(
                    "%d packets transmitted, %d received, %d%% packet loss, time %dms",
                    sent,
                    recived,
                    lost / sent,
                    end - start
                )
            )
            this.out.append("\n")
            this.out.append(
                String.format(
                    "rtt min/avg/max/mdev = %.3f/%.3f/%.3f/%.3f ms",
                    min,
                    avg,
                    max,
                    mdev
                )
            )
            this.out.append("\n")
        } catch (e: Exception) {
            Log.e(TAG, "Error", e)
            this.out.append(ErrorDialog.toMessage(e))
        }
        updateOut()
    }

    private fun updateOut(){
        binding.output.text = out
    }
}

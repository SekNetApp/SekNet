package com.example.seknet

import android.content.Context
import android.os.Handler
import androidx.fragment.app.Fragment
import utils.Ping
import java.net.UnknownHostException


class FragmentTraceroute : Fragment(R.layout.fragment_traceroute) {

    private fun traceroute(
        context: Context,
        host: String,
        ipv6: Boolean,
        count: Int,
        handler: Handler,
        run: Runnable,
        out: StringBuilder
    ) {
        try {
            val address = Ping.getHostByName(ipv6, host)
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
        /*        out.append("\n")
        handler.post(run)
        for (i in 1..count){

        }

        for (int i = 1; i < count; i++) {
            p1 = Ping.newPing (context, host, ipv6, i);
            p1.ping();
            out.append(String.format("%d  ", i));
            if (p1.fail()) {
                out.append("* ");
            } else {
                if (p1.hoop != null)
                    out.append(String.format("%s %s  ", p1.hoop, p1.hoopIp));
                else
                    out.append(String.format("%s  ", p1.hoopIp));
                out.append(String.format("%d ms ", p1.ms));
            }
        }*/
    }
}

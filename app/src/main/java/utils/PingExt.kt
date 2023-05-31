package utils

import org.apache.commons.io.IOUtils
import java.io.IOException
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.nio.charset.Charset
import java.util.regex.Pattern

class PingExt : Ping {
    var size: String? = null
    var res = 0
    var ping: ProcessBuilder

    constructor(host: String?, ttl: Int) {
        this.host = host
        val cmd = "ping"
        ping = ProcessBuilder(cmd, "-W", "" + timeout / 1000, "-c", "1", "-t", "" + ttl, host)
    }

    constructor(host: String?) {
        this.host = host
        val cmd = "ping"
        ping = ProcessBuilder(cmd, "-W", "" + timeout / 1000, "-c", "1", host)
    }

    override fun ping(): Boolean {
        return try {
            val now = System.currentTimeMillis()
            val pp = ping.start()
            res = pp.waitFor()
            ms = System.currentTimeMillis() - now
            val err = IOUtils.toString(pp.errorStream, Charset.defaultCharset())
            val out = IOUtils.toString(pp.inputStream, Charset.defaultCharset())
            if (any("Name or service not known", err, out) || any(
                    "bad address",
                    out,
                    err
                ) || any("unknown host", err, out)
            ) throw RuntimeException(
                String.format("Unknown host %s", host)
            ) // ping: unknown host a.com
            if (any(
                    "Network is unreachable",
                    err,
                    out
                )
            ) throw RuntimeException("Network is unreachable") // connect: Network is unreachable
            ttlex =
                out.contains("Time to live exceeded") || out.contains("Time exceeded: Hop limit") // From 10.173.0.110: icmp_seq=1 Time to live exceeded || From 2a06:a003:907b:1::1 icmp_seq=1 Time exceeded: Hop limit
            var m = HEAD.matcher(out)
            if (m.find()) {
                host = m.group(1)
                ip = m.group(2)
                size = m.group(3)
            }
            m = FROM_NAME.matcher(out)
            if (m.find()) {
                hoop = m.group(1)
                hoopIp = m.group(2)
            }
            m = BYTES_IP.matcher(out)
            if (m.find()) hoopIp = m.group(1)
            m = BYTES_NAME.matcher(out)
            if (m.find()) {
                hoop = m.group(1)
                hoopIp = m.group(2)
            }
            m = FROM_IP.matcher(out)
            if (m.find()) hoopIp = m.group(1)
            m = FROM_IP6.matcher(out)
            if (m.find()) hoopIp = m.group(1)
            m = TTLVAL.matcher(out)
            if (m.find()) {
                try {
                    ttlr = m.group(1).toInt()
                } catch (ignore: Exception) {
                }
            }
            m = ICMPSEQ.matcher(out)
            if (m.find()) {
                try {
                    seq = m.group(1).toInt()
                } catch (ignore: Exception) {
                }
            }
            unreachable =
                out.contains("Destination Host Unreachable") || out.contains("Destination Port Unreachable")
            true
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    override fun fail(): Boolean { // !(host reachable or ttl exceeded)
        if (unreachable) return true
        return if (res != 0 && !ttlex) true else false
    }

    companion object {
        var HEAD =
            Pattern.compile("PING ([^ ()]*)[ ]*\\(([^ ]*)\\) ([0-9]*)") // PING google.com (64.233.161.102) 56(84) bytes of data.
        var BYTES_IP =
            Pattern.compile(".* bytes from ([^ :]*):") // 64 bytes from 1.1.1.1: seq=0 ttl=56 time=36.380 ms
        var BYTES_NAME =
            Pattern.compile(".* bytes from ([^ ]*) \\(([^ ]*)\\)") // 64 bytes from axx01.distributed.zenon.net (213.189.197.1): icmp_seq=1 ttl=49 time=43.1 ms
        var FROM_NAME =
            Pattern.compile("From ([^ ]*) \\(([^ ]*)\\)") // From OpenWrt.lan (10.10.5.1): icmp_seq=1 Time to live exceeded
        var FROM_IP =
            Pattern.compile("From ([^ :]*):") // From 10.173.0.110: icmp_seq=1 Time to live exceeded
        var FROM_IP6 =
            Pattern.compile("From ([0-9a-zA-Z:.]*)") // From 2a06:a003:907b:1::1 icmp_seq=1 Time exceeded: Hop limit
        var TTLVAL = Pattern.compile("ttl=(\\d+)")
        var ICMPSEQ = Pattern.compile("icmp_seq=(\\d+)")
        fun any(s: String?, vararg aa: String): Boolean {
            for (a in aa) {
                if (a.contains(s!!)) return true
            }
            return false
        }

        fun getHostByName(host: String?): InetAddress? {
            val aa = InetAddress.getAllByName(host)
            for (a in aa) {
                if (a is Inet4Address) return a
                if (a is Inet6Address) return a
            }
            return null
        }
    }
}
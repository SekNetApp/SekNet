package utils

import java.io.IOException

open class Ping {
    @JvmField
    var host: String? = null
    @JvmField
    var timeout = 5000
    var ttl // set ttl
            = 0
    @JvmField
    var ip // host resolved ip
            : String? = null
    @JvmField
    var hoop // hoop or destination name
            : String? = null
    @JvmField
    var hoopIp // hoop (or destination) ip
            : String? = null
    @JvmField
    var ms // ping delay
            : Long = 0
    @JvmField
    var ttlex // ttl exceeded
            = false
    @JvmField
    var ttlr // ttl recived (host pinged)
            = 0
    @JvmField
    var seq // icmp_seq=387
            = 0
    @JvmField
    var unreachable // From 10.10.6.1: icmp_seq=1 Destination Port Unreachable
            = false
    var reachable // ping timeout or reachable
            = false

    constructor()

    constructor(host: String?) {
        this.host = host
    }

    constructor(host: String?, ttl: Int) {
        this.host = host
        this.ttl = ttl
    }

    open fun ping(): Boolean {
        try {
            val address = PingExt.getHostByName(host)
                ?: throw RuntimeException("Unknown host: $host")
            ip = address.hostAddress
            val now = System.currentTimeMillis()
            if (ttl != 0) {
                val r = address.isReachable(null, ttl, timeout)
                ms = System.currentTimeMillis() - now
                reachable = ms < timeout
                ttlex = !r
                hoopIp = host
            } else {
                reachable =
                    address.isReachable(timeout)
                ms = System.currentTimeMillis() - now
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return true
    }

    open fun fail(): Boolean {
        return !reachable
    }
}
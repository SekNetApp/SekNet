package utils

import java.io.IOException

open class Ping {
    @JvmField
    var host: String? = null

    @JvmField
    var timeout = 5000
    var ttl = 0

    @JvmField
    var ip
            : String? = null

    @JvmField
    var hoop
            : String? = null

    @JvmField
    var hoopIp
            : String? = null

    @JvmField
    var ms
            : Long = 0

    @JvmField
    var ttlex = false

    @JvmField
    var ttlr = 0

    @JvmField
    var seq = 0

    @JvmField
    var unreachable = false
    var reachable = false

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
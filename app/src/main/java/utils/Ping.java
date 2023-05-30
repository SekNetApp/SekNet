package utils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Ping {
    public String host;
    public int timeout = 5000;
    public int ttl; // set ttl
    public String ip; // host resolved ip
    public String hoop; // hoop or destination name
    public String hoopIp; // hoop (or destination) ip
    public long ms; // ping delay
    public boolean ttlex; // ttl exceeded
    public int ttlr; // ttl recived (host pinged)
    public int seq; // icmp_seq=387
    public boolean unreachable; // From 10.10.6.1: icmp_seq=1 Destination Port Unreachable
    public boolean reachable; // ping timeout or reachable

    public static InetAddress getHostByName(String host) throws UnknownHostException {
        InetAddress[] aa = InetAddress.getAllByName(host);
        for (InetAddress a : aa) {
            if (a instanceof Inet4Address)
                return a;
            if (a instanceof Inet6Address)
                return a;
        }
        return null;
    }

    public Ping() {
    }

    public Ping(String host) {
        this.host = host;
    }

    public Ping(String host, int ttl) {
        this.host = host;
        this.ttl = ttl;
    }

    public boolean ping() {
        try {
            InetAddress address = PingExt.getHostByName(host);
            if (address == null)
                throw new RuntimeException("Unknown host: " + host);
            ip = address.getHostAddress();
            long now = System.currentTimeMillis();
            if (ttl != 0) {
                boolean r = address.isReachable(null, ttl, timeout);
                ms = System.currentTimeMillis() - now;
                reachable = ms < timeout;
                ttlex = !r;
                hoopIp = host;
            } else {
                reachable = address.isReachable(timeout); // return true even if host is "Destination is Unreachable"
                ms = System.currentTimeMillis() - now;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean fail() {
        return !reachable;
    }
}

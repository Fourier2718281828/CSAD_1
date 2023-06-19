package org.example.utilities;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerUtils {
    public static final int PORT = 8080;
    public static final int MAX_PACKET_SIZE = 200;
    public static final InetAddress SERVER_IP;
    public static boolean TCP_SERVER_WILL_BREAK_DOWN = false;
    public static boolean UDP_PACKAGE_WILL_BE_LOST = false;

    static {
        try {
            SERVER_IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}

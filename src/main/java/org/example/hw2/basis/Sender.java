package org.example.hw2.basis;


import java.net.InetAddress;

public interface Sender {
    void sendMessage(byte[] message, InetAddress address);
}

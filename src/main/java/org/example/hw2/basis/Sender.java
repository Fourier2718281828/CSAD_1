package org.example.hw2.basis;

import org.example.exceptions.CodecException;

import java.net.InetAddress;

public interface Sender {
    void sendMessage(byte[] message, InetAddress address) throws CodecException;
}

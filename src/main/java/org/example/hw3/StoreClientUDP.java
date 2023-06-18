package org.example.hw3;

import org.example.hw2.basis.Receiver;
import org.example.hw2.basis.Sender;

import java.net.InetAddress;

public class StoreClientUDP implements Receiver, Sender {
    @Override
    public void receiveMessage() {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void sendMessage(byte[] message, InetAddress address) {

    }
}

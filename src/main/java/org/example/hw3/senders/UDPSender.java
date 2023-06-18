package org.example.hw3.senders;

import org.example.hw2.basis.Sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSender implements Sender {
    public UDPSender(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }
    @Override
    public void sendMessage(byte[] message, InetAddress address) {

        try {
            var response = new DatagramPacket(message, message.length, address, packet.getPort());
            socket.send(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final DatagramSocket socket;
    private final DatagramPacket packet;
}

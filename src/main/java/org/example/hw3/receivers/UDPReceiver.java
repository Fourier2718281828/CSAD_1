package org.example.hw3.receivers;

import org.example.hw2.basis.Decryptor;
import org.example.hw2.basis.Receiver;

import java.net.DatagramPacket;
import java.util.Arrays;

public class UDPReceiver implements Receiver {
    public UDPReceiver(DatagramPacket packet, Decryptor decryptor) {
        this.decryptor = decryptor;
        this.packet = packet;
    }

    @Override
    public void receiveMessage() {
        var encrypted = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
        decryptor.decrypt(encrypted);
    }

    @Override
    public void close() throws Exception {
        decryptor.close();
    }

    private final Decryptor decryptor;
    private final DatagramPacket packet;
}

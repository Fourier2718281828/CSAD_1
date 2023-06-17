package org.example.hw2.basis.impl;

import org.example.exceptions.CodecException;
import org.example.hw2.basis.Sender;
import org.example.packets.data.Packet;
import org.example.packets.encoding.Codec;

import java.net.InetAddress;

public class FakeSender implements Sender {
    public FakeSender(Codec<Packet> codec) {
        this.codec = codec;
    }
    @Override
    public void sendMessage(byte[] message, InetAddress address)  {
        Packet messageToSend;
        try {
            messageToSend = codec.decode(message);
            System.out.println("----------------------------------------------------------------------------------------------------" +
                    "\nSender: " + address +
                    "\nMessage: " + messageToSend);
        } catch (CodecException e) {
            System.out.println("----------------------------------------------------------------------------------------------------" +
            e.getMessage());
        }
    }

    private final Codec<Packet> codec;
}

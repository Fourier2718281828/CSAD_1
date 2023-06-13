package org.example.hw2.basis.impl;

import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.hw2.basis.Decryptor;
import org.example.hw2.operations.Operations;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;
import org.example.packets.encoding.Codec;

import java.net.InetAddress;

public class FakeCommunicator implements Communicator {
    public FakeCommunicator(Codec<Packet> codec, Decryptor decryptor) {
        this.codec = codec;
        this.decryptor = decryptor;
    }

    @Override
    public void receiveMessage() {
        try {
            var message = new Message(Operations.GET_GOOD_QUANTITY.ordinal(), 1, "milk _ _ _");
            var packet = new Packet((byte)1, 2, message);
            var encoded = new PacketCodecFactory().create().encode(packet);
            decryptor.decrypt(encoded);
        } catch (CodecException | CreationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(byte[] message, InetAddress address) throws CodecException {
        var messageToSend = codec.decode(message);
        System.out.println("Sender: " + messageToSend);
    }

    @Override
    public void close() {

    }

    private final Codec<Packet> codec;
    private final Decryptor decryptor;
}

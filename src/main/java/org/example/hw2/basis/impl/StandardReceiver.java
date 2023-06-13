package org.example.hw2.basis.impl;

import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.hw2.basis.Decryptor;
import org.example.hw2.basis.Receiver;
import org.example.hw2.operations.Operations;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;

public class StandardReceiver implements Receiver {
    public StandardReceiver(Decryptor decryptor) {
        this.decryptor = decryptor;
    }

    @Override
    public void receiveMessage() {
        try {
            var message = new Message(Operations.GET_GOOD_QUANTITY.ordinal(), 1, "milk _ _ _");
            var packet = new Packet((byte)1, 2, message);
            var encoded = new PacketCodecFactory().create().encrypt(packet);
            decryptor.decrypt(encoded);
        } catch (CodecException | CreationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        decryptor.close();
    }

    private final Decryptor decryptor;
}

package org.example.hw2.basis.impl;

import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.hw2.basis.Decryptor;
import org.example.hw2.basis.Receiver;

public class FakeReceiver implements Receiver {
    public FakeReceiver(FakeReceiverMessageChooser messageChooser, Decryptor decryptor) {
        this.decryptor = decryptor;
        this.messageChooser = messageChooser;
    }

    @Override
    public void receiveMessage() {
        try {
            var packet = messageChooser.getCurrentPacket();
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
    private final FakeReceiverMessageChooser messageChooser;
}

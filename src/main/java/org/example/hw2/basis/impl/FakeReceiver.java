package org.example.hw2.basis.impl;

import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.factories.hw2.PacketFactory;
import org.example.hw2.basis.Decryptor;
import org.example.hw2.basis.Receiver;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.packets.data.Message;

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
    public void receiveMessageTest(Operations type, OperationParams params) {
        try {
            var messageTxt = params.toString();
            var message = new Message(type, /*userId.getAndIncrement()*/0, messageTxt);
            var packet = new PacketFactory().create((byte) 0x0, message);
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

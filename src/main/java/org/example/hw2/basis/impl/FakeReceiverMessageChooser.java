package org.example.hw2.basis.impl;

import org.example.exceptions.CreationException;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class FakeReceiverMessageChooser {
    static {
        userId = new AtomicInteger(0);
    }

    public FakeReceiverMessageChooser(DoubleParamFactory<Packet, Byte, Message> packetFactory) {
        this.packetFactory = packetFactory;
    }

    public void setOperation(Operations type, OperationParams params) throws CreationException {
        var messageTxt = params.toString();
        var message = new Message(type, userId.getAndIncrement(), messageTxt);
        var packet = packetFactory.create((byte) 0x0, message);
        currentMessage = new AtomicReference<>(packet);
    }

    public Packet getCurrentPacket() {
        return currentMessage.get();
    }

    private AtomicReference<Packet> currentMessage;
    private final DoubleParamFactory<Packet, Byte, Message> packetFactory;
    private static AtomicInteger userId;
}

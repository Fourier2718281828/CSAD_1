package org.example.hw2.basis.impl;

import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.SingleParamFactory;
import org.example.hw2.basis.Encryptor;
import org.example.hw2.basis.Processor;
import org.example.hw2.basis.Sender;
import org.example.hw2.operations.Operation;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class StandardProcessor implements Processor {
    public StandardProcessor(
            Encryptor encryptor,
            Sender sender,
            SingleParamFactory<Operation, Operations> operationFactory) {
        this.encryptor = encryptor;
        this.sender = sender;
        this.operationFactory = operationFactory;
    }

    @Override
    public void process(Packet packet) {
        if(packet == null) {
            sendMessage("Failed to parse the packet.");
            return;
        }
        try {
            var operationId = Operations.values()[packet.message().type() - 1];
            var operation = operationFactory.create(operationId);
            var message = packet.message().message();
            operation.execute(new OperationParams(message));
            var result = operation.getResult();
            var messageToSend = "Ok";
            if(result.isPresent())
                messageToSend += ". Result = " + result.get();
            sendMessage(messageToSend);
        } catch (CreationException e) {
            sendMessage("Invalid operation id.");
        }
    }

    @Override
    public void close() {

    }

    private void sendMessage(String message) {
        try {
            var packetToSend = new Packet((byte) 1, 1, new Message(1, 1, message));
            var encrypted = encryptor.encrypt(packetToSend);
            sender.sendMessage(encrypted, InetAddress.getLocalHost());
        } catch (CodecException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private final Encryptor encryptor;
    private final Sender sender;
    private final SingleParamFactory<Operation, Operations> operationFactory;
}

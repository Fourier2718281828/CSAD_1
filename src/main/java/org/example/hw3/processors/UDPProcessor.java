package org.example.hw3.processors;

import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.exceptions.StorageException;
import org.example.factories.interfaces.SingleParamFactory;
import org.example.hw2.basis.Processor;
import org.example.hw2.basis.Sender;
import org.example.hw2.operations.Operation;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;
import org.example.packets.encoding.EncryptionProvider;
import org.example.utilities.ServerUtils;

import java.net.DatagramSocket;

public class UDPProcessor implements Processor {
    /*
        Sorry, that might be absolutely irrelevant to pass
        Socket as a parameter to Processor's constructor like that.
        It is done so that we can call Sender's sendMessage() method
        and get an InetAddress for it. Unfortunately, I haven't found
        an alternative solution that meets the provided Architecture
        Diagram in HW2. That seems to be rather inflexible.
    */
    public UDPProcessor(
            DatagramSocket socket,
            EncryptionProvider<Packet> encryptor,
            Sender sender,
            SingleParamFactory<Operation, Operations> operationFactory) {
        this.socket = socket;
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
            var operationId = packet.message().type();
            var operation = operationFactory.create(operationId);
            var message = packet.message().message();
            operation.execute(new OperationParams(message));
            var result = operation.getResult();
            var messageToSend = "Ok";
            if(result.isPresent())
                messageToSend += ". Result = " + result.get();
            sendMessage(messageToSend);
        } catch (CreationException e) {
            sendMessage("Operations factory not initialized!");
        } catch (StorageException e) {
            sendMessage(e.getMessage());
        }
    }

    @Override
    public void close() {

    }

    private void sendMessage(String message) {
        try {
            var packetToSend = new Packet((byte) 1, 1, new Message(Operations.GET_GOOD_QUANTITY, 1, message));
            var encrypted = encryptor.encrypt(packetToSend);
            sender.sendMessage(encrypted, ServerUtils.SERVER_IP);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        }
    }

    private final DatagramSocket socket;
    private final EncryptionProvider<Packet> encryptor;
    private final Sender sender;
    private final SingleParamFactory<Operation, Operations> operationFactory;

}
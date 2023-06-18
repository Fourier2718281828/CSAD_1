package org.example.hw3.receivers;

import org.example.exceptions.CreationException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.factories.hw3.BufferedIOProcessorsFactory;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.factories.operations.OperationFactory;
import org.example.hw2.basis.Receiver;
import org.example.hw2.basis.impl.*;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw3.processors.SocketedProcessor;
import org.example.hw3.senders.TCPSender;

import java.net.Socket;

public class ReceiverFactory implements DoubleParamFactory<Receiver, Socket, GroupedGoodStorage> {
    @Override
    public Receiver create(Socket socket, GroupedGoodStorage storage) throws CreationException {
        var codecFactory = new PacketCodecFactory();
        var codec = codecFactory.create();
        var operationFactory = new OperationFactory(storage);
        var sender = new TCPSender(socket);
        var socketedProcessor = new SocketedProcessor(socket, codec, sender, operationFactory);
        var standardDecryptor = new StandardDecryptor(codec, socketedProcessor);
        var readerFactory = new BufferedIOProcessorsFactory();
        return new StandardReceiver(socket, standardDecryptor);
    }
}

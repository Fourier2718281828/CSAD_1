package org.example.hw3.receivers;

import org.example.exceptions.CreationException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.factories.interfaces.TripleParamFactory;
import org.example.factories.operations.OperationFactory;
import org.example.hw2.basis.Receiver;
import org.example.hw2.basis.impl.StandardDecryptor;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw3.processors.UDPProcessor;
import org.example.hw3.senders.UDPSender;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiverFactory implements TripleParamFactory<Receiver, DatagramSocket, DatagramPacket, GroupedGoodStorage> {
    @Override
    public Receiver create(DatagramSocket socket, DatagramPacket packet, GroupedGoodStorage storage) throws CreationException {
        var codecFactory = new PacketCodecFactory();
        var codec = codecFactory.create();
        var operationFactory = new OperationFactory(storage);
        var sender = new UDPSender(socket, packet);
        var socketedProcessor = new UDPProcessor(socket, codec, sender, operationFactory);
        var standardDecryptor = new StandardDecryptor(codec, socketedProcessor);
        return new UDPReceiver(packet, standardDecryptor);
    }
}
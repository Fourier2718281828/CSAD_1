package org.example.factories.hw2;

import org.example.exceptions.CreationException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.factories.operations.OperationFactory;
import org.example.hw2.basis.Receiver;
import org.example.hw2.basis.impl.*;
import org.example.hw2.storages.Storage;

public class MultyThreadedFakeReceiverFactory implements DoubleParamFactory<Receiver, Storage, FakeReceiverMessageChooser> {
    public Receiver create(Storage storage, FakeReceiverMessageChooser messageChooser) throws CreationException {
        var codecFactory = new PacketCodecFactory();
        var codec = codecFactory.create();
        var fakeSender = new FakeSender(codec);
        var operationFactory = new OperationFactory(storage);
        var processor = new StandardProcessor(codec, fakeSender, operationFactory);
        var multyThreadedProcessor = new MultyThreadedProcessor(processor);
        var standardDecryptor = new StandardDecryptor(codec, multyThreadedProcessor);
        var multyThreadedDecryptor = new MultyThreadedDecryptor(standardDecryptor);
        var receiver = new FakeReceiver(messageChooser, multyThreadedDecryptor);
        return new MultyThreadedFakeReceiver(receiver);
    }
}

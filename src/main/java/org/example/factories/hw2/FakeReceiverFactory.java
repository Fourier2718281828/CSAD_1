package org.example.factories.hw2;

import org.example.exceptions.CreationException;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.factories.codec.PacketCodecFactory;
import org.example.factories.operations.OperationFactory;
import org.example.hw2.basis.Receiver;
import org.example.hw2.basis.impl.*;
import org.example.hw2.storages.GroupedGoodStorage;

public class FakeReceiverFactory implements DoubleParamFactory<Receiver, GroupedGoodStorage, FakeReceiverMessageChooser> {
    @Override
    public Receiver create(GroupedGoodStorage storage, FakeReceiverMessageChooser messageChooser) throws CreationException {
        var codecFactory = new PacketCodecFactory();
        var codec = codecFactory.create();
        var fakeSender = new FakeSender(codec);
        var operationFactory = new OperationFactory(storage);
        var multyThreadedProcessor = new StandardProcessor(codec, fakeSender, operationFactory);
        var multyThreadedDecryptor = new StandardDecryptor(codec, multyThreadedProcessor);
        return new FakeReceiver(messageChooser, multyThreadedDecryptor);
    }
}

package org.example.hw2.basis.impl;

import org.example.exceptions.CodecException;
import org.example.hw2.basis.Decryptor;
import org.example.hw2.basis.Processor;
import org.example.packets.data.Packet;
import org.example.packets.encoding.Codec;

public class StandardDecryptor implements Decryptor {
    public StandardDecryptor(Codec<Packet> codec, Processor processor) {
        this.codec = codec;
        this.processor = processor;
    }

    @Override
    public void decrypt(byte[] message) {
        Packet decryptedMessage;
        try {
            decryptedMessage = codec.decode(message);
        } catch (CodecException e) {
            decryptedMessage = null;
        }
        processor.process(decryptedMessage);
    }

    @Override
    public void close() throws Exception {
        processor.close();
    }

    private final Codec<Packet> codec;
    private final Processor processor;
}

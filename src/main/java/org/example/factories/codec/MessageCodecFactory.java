package org.example.factories.codec;

import org.example.exceptions.CreationException;
import org.example.factories.interfaces.Factory;
import org.example.packets.data.Message;
import org.example.packets.encoding.Codec;
import org.example.packets.encoding.MessageCodec;
import org.example.packets.encoding.encryption.CipherCryptographer;
import org.example.utilities.bitwise.BigEndianBytePutter;

import java.security.InvalidKeyException;

public class MessageCodecFactory implements Factory<Codec<Message>> {
    @Override
    public Codec<Message> create() throws CreationException {
        try {
            var bytePutter = new BigEndianBytePutter();
            var messageCryptographer = new CipherCryptographer();
            return new MessageCodec(bytePutter, messageCryptographer);
        } catch (InvalidKeyException e) {
            throw new CreationException("Failed to construct messageCryptographer while creating MessageCodec.");
        }
    }
}

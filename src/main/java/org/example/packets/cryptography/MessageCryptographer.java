package org.example.packets.cryptography;

import org.example.exceptions.CryptographicalError;
import org.example.packets.data.Message;
import org.example.utilities.TypeTraits;
import org.example.utilities.bitwise.ByteGetter;
import org.example.utilities.bitwise.IntegralBytePutter;


public class MessageCryptographer implements Cryptographer<Message> {
    public MessageCryptographer(IntegralBytePutter bytesPutter) {
        this.bytesPutter = bytesPutter;
    }

    @Override
    public byte[] encrypt(Message messagePOJO) {
        final var type = messagePOJO.type();
        final var userId = messagePOJO.userId();
        final var message = messagePOJO.message();

        final var cTypeSize = TypeTraits.sizeof(type);
        final var bUserIdSize = TypeTraits.sizeof(userId);
        final var messageSize = message.length();
        final var resSize = cTypeSize + bUserIdSize + messageSize;

        var res = new byte[resSize];
        bytesPutter.putToBytes(0, res, type);
        bytesPutter.putToBytes(cTypeSize, res, userId);
        bytesPutter.putToBytes(cTypeSize + bUserIdSize, res, message.getBytes());

        return res;
    }

    @Override
    public Message decrypt(byte[] bytes) throws CryptographicalError {
        try {
            final var type = ByteGetter.getInt(0, bytes);
            final var userId = ByteGetter.getInt(4, bytes);
            final var offset = TypeTraits.sizeof(type) + TypeTraits.sizeof(userId);
            final var bMessage = ByteGetter.getBytes(offset, bytes, bytes.length - offset);
            final var message = new String(bMessage);
            return new Message(type, userId, message);
        } catch (RuntimeException e) {
            throw new CryptographicalError("Invalid packet.");
        }
    }

    private final IntegralBytePutter bytesPutter;
}

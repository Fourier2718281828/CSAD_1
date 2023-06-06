package org.example.packets.encoding;

import org.example.exceptions.CodecException;
import org.example.exceptions.CryptographicException;
import org.example.packets.data.Message;
import org.example.packets.encoding.encryption.Cryptographer;
import org.example.utilities.TypeTraits;
import org.example.utilities.bitwise.ByteGetter;
import org.example.utilities.bitwise.IntegralBytePutter;

import java.util.Optional;


public class MessageCodec implements Codec<Message> {
    public MessageCodec(IntegralBytePutter bytesPutter, Cryptographer messageCryptographer) {
        this.bytesPutter = bytesPutter;
        this.messageCryptographer = messageCryptographer;
    }

    @Override
    public Optional<byte[]> encode(Message encodable) {
        final var type = encodable.type();
        final var userId = encodable.userId();
        final var message = encodable.message();


        final var cTypeSize = TypeTraits.sizeof(type);
        final var bUserIdSize = TypeTraits.sizeof(userId);
        final var messageSize = message.length();
        final var resSize = cTypeSize + bUserIdSize + messageSize;

        var res = new byte[resSize];
        bytesPutter.putToBytes(0, res, type);
        bytesPutter.putToBytes(cTypeSize, res, userId);
        bytesPutter.putToBytes(cTypeSize + bUserIdSize, res, message.getBytes());

        try {
            res = messageCryptographer.encrypt(res);
        } catch (CryptographicException e) {
            return Optional.empty();
        }

        return Optional.of(res);
    }

    @Override
    public Optional<Message> decode(byte[] input) throws CodecException {
        try {
            final var bytes = messageCryptographer.decrypt(input);
            final var type = ByteGetter.getInt(0, bytes);
            final var userId = ByteGetter.getInt(4, bytes);
            final var offset = TypeTraits.sizeof(type) + TypeTraits.sizeof(userId);
            final var bMessage = ByteGetter.getBytes(offset, bytes, bytes.length - offset);
            final var message = new String(bMessage);
            return Optional.of(new Message(type, userId, message));
        } catch (CryptographicException | RuntimeException e) {
            return Optional.empty();
        }
    }

    private final IntegralBytePutter bytesPutter;
    private final Cryptographer messageCryptographer;
}

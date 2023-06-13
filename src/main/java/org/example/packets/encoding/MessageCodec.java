package org.example.packets.encoding;

import org.example.exceptions.CodecException;
import org.example.exceptions.CryptographicException;
import org.example.packets.data.Message;
import org.example.packets.encoding.encryption.CryptographicService;
import org.example.utilities.TypeTraits;
import org.example.utilities.bitwise.ByteGetter;
import org.example.utilities.bitwise.IntegralBytePutter;

public class MessageCodec implements Codec<Message> {
    public MessageCodec(IntegralBytePutter bytesPutter, CryptographicService messageCryptographer) {
        this.bytesPutter = bytesPutter;
        this.messageCryptographer = messageCryptographer;
    }

    @Override
    public byte[] encrypt(Message encryptable) throws CodecException {
        final var type = encryptable.type();
        final var userId = encryptable.userId();
        final var message = encryptable.message();


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
            throw new CodecException("Failed to encrypt a message: " + e.getMessage());
        }

        return res;
    }

    @Override
    public Message decode(byte[] input) throws CodecException {
        try {
            final byte[] bytes = messageCryptographer.decrypt(input);
            final var type = ByteGetter.getInt(0, bytes);
            final var userId = ByteGetter.getInt(4, bytes);
            final var offset = TypeTraits.sizeof(type) + TypeTraits.sizeof(userId);
            final var bMessage = ByteGetter.getBytes(offset, bytes, bytes.length - offset);
            final var message = new String(bMessage);
            return new Message(type, userId, message);
        } catch (CryptographicException e) {
            throw new CodecException("Message decryption failed");
        } catch (RuntimeException e) {
            throw new CodecException("ByteGetter exception: " + e.getMessage());
        }

    }

    private final IntegralBytePutter bytesPutter;
    private final CryptographicService messageCryptographer;
}

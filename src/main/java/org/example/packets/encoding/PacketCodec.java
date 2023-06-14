package org.example.packets.encoding;

import org.example.exceptions.CodecException;
import org.example.packets.encoding.checksum.Checksum16;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;
import org.example.utilities.TypeTraits;
import org.example.utilities.bitwise.ByteGetter;
import org.example.utilities.bitwise.IntegralBytePutter;

public class PacketCodec implements Codec<Packet> {
    public PacketCodec(
            Codec<Message> messageCodec,
            Checksum16 checksumEvaluator,
            IntegralBytePutter bytePutter) {
        this.messageCodec = messageCodec;
        this.checksumEvaluator = checksumEvaluator;
        this.bytePutter = bytePutter;
    }
    @Override
    public byte[] encrypt(Packet encryptable) throws CodecException {
        try {
            final var message = encryptable.message();
            final var encryptedMessage = messageCodec.encrypt(message);
            final var bMagic = (byte) 0x13;
            final var source = encryptable.source();
            final var packetId = encryptable.packetId();

            final var wLen = encryptedMessage.length;
            final var bMagicSize = TypeTraits.sizeof(bMagic);
            final var bSrcSize = TypeTraits.sizeof(source);
            final var bPktIdSize = TypeTraits.sizeof(packetId);
            final var wLenSize = TypeTraits.sizeof(wLen);
            final var crcSize = 2;
            final var resLength = bMagicSize + bSrcSize + bPktIdSize + wLenSize + crcSize + wLen + crcSize;

            byte[] res = new byte[resLength];
            bytePutter.putToBytes(0, res, bMagic);
            bytePutter.putToBytes(bMagicSize, res, source);
            bytePutter.putToBytes(bMagicSize + bSrcSize, res, packetId);
            bytePutter.putToBytes(bMagicSize + bSrcSize + bPktIdSize, res, wLen);
            bytePutter.putToBytes(bMagicSize + bSrcSize + bPktIdSize + wLenSize + crcSize, res, encryptedMessage);

            final var wCrc16First = checksumEvaluator.evaluateChecksumRange(res, 0, 14);
            final var wCrc16Second = checksumEvaluator.evaluateChecksum(encryptedMessage);
            bytePutter.putToBytes(bMagicSize + bSrcSize + bPktIdSize + wLenSize, res, wCrc16First);
            bytePutter.putToBytes(res.length - crcSize, res, wCrc16Second);

            assert (TypeTraits.sizeof(wCrc16First) == crcSize);
            assert (TypeTraits.sizeof(wCrc16Second) == crcSize);

            return res;
        } catch (CodecException e) {
            throw new CodecException("Failed to encode a message while encoding a packet: " + e.getMessage());
        }
    }

    @Override
    public Packet decode(byte[] bytes) throws CodecException {
        try {
            final var bMagic = bytes[0];
            final var bSrc = bytes[1];
            final var bPktId = ByteGetter.getLong(2, bytes);
            final var wLen = ByteGetter.getInt(10, bytes);
            final var wCrc16First = ByteGetter.getShort(14, bytes);
            final var bMsq = ByteGetter.getBytes(16, bytes, wLen);
            final var wCrc16Second = ByteGetter.getShort(16 + wLen, bytes);

            assert(wLen >= 0);

            final var fstChecksumEvaluated = checksumEvaluator.evaluateChecksumRange(bytes, 0, 14);
            final var sndChecksumEvaluated = checksumEvaluator.evaluateChecksum(bMsq);

            if(16 + wLen > bytes.length)
                throw new CodecException("Packet length limit exceeded.");
            if(bMagic != (byte) 0x13)
                throw new CodecException("Magic byte mismatch.");
            if(wCrc16First != fstChecksumEvaluated)
                throw new CodecException("First checksum mismatch.");
            if(wCrc16Second != sndChecksumEvaluated)
                throw new CodecException("Second checksum mismatch.");

            final var decryptedMessage = messageCodec.decode(bMsq);
            return new Packet(bSrc, bPktId, decryptedMessage);

        } catch (RuntimeException e) {
            throw new CodecException("Invalid packet length.");
        }
    }

    private final Codec<Message> messageCodec;
    private final Checksum16 checksumEvaluator;
    private final IntegralBytePutter bytePutter;
}

package org.example.packets.encoding;

import static org.junit.jupiter.api.Assertions.*;

import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.codec.MessageCodecFactory;
import org.example.packets.data.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CodecTest {
    @Test
    @DisplayName("Invalid packet decoding: bMagic")
    void invalidMessageDecoding() {

    }

    @Test
    @DisplayName("Invalid packet decoding: CRC")
    void invalidPacketDecoding() {

    }

    @Test
    @DisplayName("Decoding an encoded packet")
    void decodingEncodedPacket() {

    }

    @Test
    @DisplayName("Decoding an encoded empty message")
    void decodingEncodedEmptyMessageTest() {
        Message empty = new Message(0, 0, "");
        decodingEncodedMessage(empty);
    }

    @Test
    @DisplayName("Decoding an encoded random message")
    void decodingEncodedRandomMessageTest() {
        Message empty = new Message(2, 100, "asdadvasdfasdfwefadf");
        decodingEncodedMessage(empty);
    }

    void decodingEncodedMessage(Message message) {
        try {
            var messageCodec = new MessageCodecFactory().create();
            var encoded = messageCodec.encode(message);
            var decoded = messageCodec.decode(encoded);
            assertEquals(decoded, message);
        } catch (CreationException | CodecException e) {
            fail(e.getMessage());
        }
    }

    void decodeInvalidMessage(Codec<Message> codec, Message message) {
    }
}
package org.example.packets.encoding;

import static org.junit.jupiter.api.Assertions.*;
import org.example.exceptions.CryptographicException;
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
    @DisplayName("Decoding an encoded message")
    void decodingEncodedMessage() {

    }

    void decodeInvalidMessage(Codec<Message> codec, Message message) {
    }
}
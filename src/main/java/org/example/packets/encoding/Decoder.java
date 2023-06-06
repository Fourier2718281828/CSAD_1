package org.example.packets.encoding;

import org.example.exceptions.CodecException;
import org.example.packets.data.Message;

import java.util.Optional;

public interface Decoder<Decodable> {
    Optional<Decodable> decode(byte[] bytes) throws CodecException;
}

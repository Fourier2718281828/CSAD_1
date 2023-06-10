package org.example.packets.encoding;

import org.example.exceptions.CodecException;

public interface DecryptionProvider<Decodable> {
    Decodable decode(byte[] bytes) throws CodecException;
}

package org.example.packets.encoding;

import org.example.exceptions.CodecException;

public interface EncryptionProvider<Encodable> {
    byte[] encode(Encodable encodable) throws CodecException;
}

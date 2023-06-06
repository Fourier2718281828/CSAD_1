package org.example.packets.encoding.encryption;

import org.example.exceptions.CryptographicException;

public interface Decryptor {
    byte[] decrypt(byte[] bytes) throws CryptographicException;
}

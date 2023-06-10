package org.example.packets.encoding.encryption;

import org.example.exceptions.CryptographicException;

public interface Cipherer {
    byte[] encrypt(byte[] bytes) throws CryptographicException;
}

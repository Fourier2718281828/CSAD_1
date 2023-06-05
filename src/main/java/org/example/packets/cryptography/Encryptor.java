package org.example.packets.cryptography;

public interface Encryptor<Encryptable> {
    byte[] encrypt(Encryptable encryptable);
}

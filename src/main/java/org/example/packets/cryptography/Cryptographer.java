package org.example.packets.cryptography;

public interface Cryptographer<CryptographicEssence> extends
        Encryptor<CryptographicEssence>,
        Decryptor<CryptographicEssence> {

}

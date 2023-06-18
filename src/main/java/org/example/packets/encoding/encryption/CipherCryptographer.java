package org.example.packets.encoding.encryption;

import org.example.exceptions.CryptographicException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class CipherCryptographer implements KeyCryptographer {
    static {
        //key = KeyGenerator.getInstance("AES").generateKey();
        byte[] keyBytes = {
                (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07,
                (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F
        };
        key = new SecretKeySpec(keyBytes, "AES");
    }

    public CipherCryptographer() throws InvalidKeyException {
        try {
            this.encryptor = Cipher.getInstance("AES");
            this.decryptor = Cipher.getInstance("AES");
            encryptor.init(Cipher.ENCRYPT_MODE, key);
            decryptor.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] encrypt(byte[] bytes) throws CryptographicException {
        return cipherDoFinal(encryptor, bytes);
    }

    @Override
    public byte[] decrypt(byte[] bytes) throws CryptographicException {
        return cipherDoFinal(decryptor, bytes);
    }

    private static byte[] cipherDoFinal(Cipher cipher, byte[] input) throws CryptographicException {
        try {
            return cipher.doFinal(input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptographicException("Invalid input bytes.");
        }
    }

    @Override
    public Key getKey() {
        return key;
    }

    private final Cipher encryptor;
    private final Cipher decryptor;
    private static final Key key;
}

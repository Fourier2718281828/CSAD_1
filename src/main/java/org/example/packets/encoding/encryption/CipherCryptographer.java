package org.example.packets.encoding.encryption;

import org.example.exceptions.CryptographicException;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class CipherCryptographer implements KeyCryptographer {

    public CipherCryptographer() throws InvalidKeyException, NoSuchAlgorithmException {
        this(KeyGenerator.getInstance("AES").generateKey());
    }

    public CipherCryptographer(Key key) throws InvalidKeyException {
        try {
            this.key = key;
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
    private final Key key;
}

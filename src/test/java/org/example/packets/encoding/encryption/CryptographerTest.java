package org.example.packets.encoding.encryption;
import static org.junit.jupiter.api.Assertions.*;

import org.example.exceptions.CryptographicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class CryptographerTest {
    @Test
    @DisplayName("Decrypt the encrypted random data")
    void decryptEncryptedRandomDataTest() {
        var data = new byte[] {0x32, 0x42, 0x78, 0x13, 0x24};
        decryptEncryptedMultiple(data);
    }

    @Test
    @DisplayName("Decrypt the encrypted zero data")
    void decryptEncryptedZeroDataTest() {
        var data = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00};
        decryptEncryptedMultiple(data);
    }

    @Test
    @DisplayName("Decrypt the encrypted 255-valued data")
    void decryptEncryptedMaxDataTest() {
        var data = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        decryptEncryptedMultiple(data);
    }

    @Test
    @DisplayName("Multiple data decrypt-encrypted")
    void decryptEncryptedMultipleDataTest() {
        var data1 = new byte[] {0x32, 0x42, 0x78, 0x13, 0x24};
        var data2 = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00};
        var data3 = new byte[] {0x11, 0x20, 0x03, 0x23, 0x79};
        var data4 = new byte[] {0x10, 0x00, 0x00, 0x00, 0x00};
        decryptEncryptedMultiple(data1, data2, data3, data4);
    }

    void decryptEncryptedMultiple(byte[]... datas)
    {
        try {
            CryptographicService cryptographer = new CipherCryptographer();
            for(var data : datas) {
                decryptEncrypted(cryptographer, data);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | CryptographicException e) {
            fail(e.getMessage());
        }
    }

    void decryptEncrypted(CryptographicService cryptographer, byte[] data) throws CryptographicException {
        var encrypted = cryptographer.encrypt(data);
        var decrypted = cryptographer.decrypt(encrypted);
        assertArrayEquals(data, decrypted);
    }
}
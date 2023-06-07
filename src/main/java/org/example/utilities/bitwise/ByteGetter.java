package org.example.utilities.bitwise;

import org.example.utilities.TypeTraits;

import java.util.Arrays;

public class ByteGetter {
    public static byte getByte(int position, byte[] bytes) throws RuntimeException {
        try {
            return bytes[position];
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static short getShort(int position, byte[] bytes) throws RuntimeException {
        short res = 0;
        final var resSize = TypeTraits.sizeof(res);
        checkSize(position + resSize, bytes);

        for(var i = position; i < position + resSize; ++i) {
            res <<= 8;
            res |= ((short) 0xFF) & bytes[i];
        }
        return res;
    }

    public static int getInt(int position, byte[] bytes) throws RuntimeException {
        int res = 0;
        final var resSize = TypeTraits.sizeof(res);
        checkSize(resSize, bytes);

        for(var i = position; i < position + resSize; ++i) {
            res <<= 8;
            res |= 0xFF & bytes[i];
        }
        return res;
    }

    public static long getLong(int position, byte[] bytes) throws RuntimeException {
        long res = 0;
        final var resSize = TypeTraits.sizeof(res);
        checkSize(TypeTraits.sizeof(res), bytes);

        for(var i = position; i < position + resSize; ++i) {
            res <<= 8;
            res |= 0xFFL & bytes[i];
        }
        return res;
    }

    public static byte[] getBytes(int position, byte[] bytes, int count) throws RuntimeException {
        if(position + count > bytes.length)
            throw new RuntimeException("Not enough bytes array length");
        return Arrays.copyOfRange(bytes, position, position + count);
    }

    private static void checkSize(int size, byte[] bytes) throws RuntimeException {
        if(size > bytes.length)
            throw new RuntimeException("Not enough bytes array length");
    }
}

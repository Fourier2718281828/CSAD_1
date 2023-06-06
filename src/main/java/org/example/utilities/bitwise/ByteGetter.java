package org.example.utilities.bitwise;

import org.example.utilities.TypeTraits;

import java.util.Arrays;

public class ByteGetter {
    public static byte getByte(int position, byte[] bytes) {
        return bytes[position];
    }

    public static short getShort(int position, byte[] bytes){
        short res = 0;
        checkSize(TypeTraits.sizeof(res), bytes);

        for(var i = position; i < bytes.length; ++i) {
            res |= ((short) 0xFF) & bytes[i];
            res <<= 8;
        }
        return res;
    }

    public static int getInt(int position, byte[] bytes){
        int res = 0;
        checkSize(TypeTraits.sizeof(res), bytes);

        for(var i = position; i < bytes.length; ++i) {
            res |= 0xFFFF & bytes[i]; //Byte.toUnsignedInt(bytes[i]);
            res <<= 8;
        }
        return res;
    }

    public static long getLong(int position, byte[] bytes) {
        long res = 0;
        checkSize(TypeTraits.sizeof(res), bytes);

        for(var i = position; i < bytes.length; ++i) {
            res |= 0xFFFFFFFFL & bytes[i]; //Byte.toUnsignedLong(bytes[i]);
            res <<= 8;
        }
        return res;
    }

    public static byte[] getBytes(int position, byte[] bytes, int count) {
        if(position + count > bytes.length)
            throw new RuntimeException("Not enough bytes array length");
        return Arrays.copyOfRange(bytes, position, position + count);
    }

    private static void checkSize(int size, byte[] bytes) throws RuntimeException {
        if(size < bytes.length)
            throw new RuntimeException("Not enough bytes array length");
    }
}

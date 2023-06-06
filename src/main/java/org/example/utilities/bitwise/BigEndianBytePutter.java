package org.example.utilities.bitwise;

import org.example.utilities.TypeTraits;


public class BigEndianBytePutter implements IntegralBytePutter {
    @Override
    public void putToBytes(int position, byte[] bytes, byte num) {
        bytes[position] = num;
    }

    @Override
    public void putToBytes(int position, byte[] bytes, short num) {
        bytes[position] = (byte) (num >>> 8);
        bytes[position + 1] = (byte) num;
    }

    @Override
    public void putToBytes(int position, byte[] bytes, int num) {
        var size = TypeTraits.sizeof(num);
        var i = size - 1;
        var j = position;
        var offsetCount = 8 * i;
        for(; i >= 0; --i, ++j, offsetCount -= 8)
        {
            bytes[j] = (byte) (num >>> offsetCount);
        }
    }

    @Override
    public void putToBytes(int position, byte[] bytes, long num) {
        var size = TypeTraits.sizeof(num);
        var i = size - 1;
        var j = position;
        var offsetCount = 8 * i;
        for(; i >= 0; --i, ++j, offsetCount -= 8)
        {
            bytes[j] = (byte) (num >>> offsetCount);
        }
    }

    @Override
    public void putToBytes(int position, byte[] bytes, byte[] val) {
        System.arraycopy(val, 0, bytes, position, val.length);
    }
}

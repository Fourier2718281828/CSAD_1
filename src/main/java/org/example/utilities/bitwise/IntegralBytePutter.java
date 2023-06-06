package org.example.utilities.bitwise;

public interface IntegralBytePutter {
    void putToBytes(int position, byte[] bytes, byte num);
    void putToBytes(int position, byte[] bytes, short num);
    void putToBytes(int position, byte[] bytes, int num);
    void putToBytes(int position, byte[] bytes, long num);
    void putToBytes(int position, byte[] bytes, byte[] val);
}

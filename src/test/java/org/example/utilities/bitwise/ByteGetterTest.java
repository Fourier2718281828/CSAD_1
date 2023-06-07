package org.example.utilities.bitwise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteGetterTest {
    @Test
    @DisplayName("GetByte testing")
    void getByte() {
        byte[] data1 = new byte[] { 0x13, 0x5, 0x50, 0x00 };
        byte[] data2 = new byte[] {};
        byte[] data3 = new byte[] { 0x14 };
        var x13 = ByteGetter.getByte(0, data1);
        var x50 = ByteGetter.getByte(2, data1);
        var x14 = ByteGetter.getByte(0, data3);

        assertEquals(x13, 0x13);
        assertEquals(x50, 0x50);
        assertEquals(x14, 0x14);

        try {
            ByteGetter.getByte(0, data2);
            fail("Index out of bounds not processed.");
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    @DisplayName("GetShort testing")
    void getShort() {
        byte[] data1 = new byte[] { 0x13, 0x5, 0x50, 0x00 };
        byte[] data2 = new byte[] {};
        byte[] data3 = new byte[] { 0x14 };
        var x1305 = ByteGetter.getShort(0, data1);
        var x5000 = ByteGetter.getShort(2, data1);

        assertEquals((short) 0x1305, x1305);
        assertEquals((short) 0x5000, x5000);

        try {
            ByteGetter.getShort(0, data2);
            fail("Index out of bounds not processed.");
        } catch (RuntimeException ignored) {
        }

        try {
            ByteGetter.getShort(0, data3);
            fail("Index out of bounds not processed.");
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    @DisplayName("GetInt testing")
    void getInt() {
        byte[] data1 = new byte[] { 0x13, 0x5, 0x50, 0x00, 0x32, 0x45, 0x78, 0x50 };
        byte[] data2 = new byte[] {};
        byte[] data3 = new byte[] { 0x14, 0x13, 0x14, 0x00 };
        var x13055000 = ByteGetter.getInt(0, data1);
        var x50003245 = ByteGetter.getInt(2, data1);
        var x32457850 = ByteGetter.getInt(4, data1);

        assertEquals(0x13055000, x13055000);
        assertEquals(0x50003245, x50003245);
        assertEquals(0x32457850, x32457850);

        try {
            ByteGetter.getInt(0, data2);
            fail("Index out of bounds not processed.");
        } catch (RuntimeException ignored) {
        }

        try {
            ByteGetter.getInt(1, data3);
            fail("Index out of bounds not processed.");
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    @DisplayName("GetLong testing")
    void getLong() {
        byte[] data1 = new byte[] { 0x13, 0x5, 0x50, 0x00, 0x32, 0x45, 0x78, 0x50, 0x12 };
        byte[] data2 = new byte[] {};
        byte[] data3 = new byte[] { 0x14, 0x13, 0x14, 0x00, 0x58, 0x39, 0x16, 0x02 };
        var x1305500032457850 = ByteGetter.getLong(0, data1);
        var x0550003245785012 = ByteGetter.getLong(1, data1);

        assertEquals(0x1305500032457850L, x1305500032457850);
        assertEquals(0x0550003245785012L, x0550003245785012);

        try {
            ByteGetter.getLong(0, data2);
            fail("Index out of bounds not processed.");
        } catch (RuntimeException ignored) {
        }

        try {
            ByteGetter.getLong(1, data3);
            fail("Index out of bounds not processed.");
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    @DisplayName("GetBytes testing")
    void getBytes() {
        var data1 = new byte[] { 0x13, 0x5, 0x50, 0x00, 0x32, 0x45, 0x78, 0x50, 0x12 };
        var data2 = new byte[] {};

        var data1_04 = ByteGetter.getBytes(0, data1, 4);
        var data1_35 = ByteGetter.getBytes(3, data1, 5);
        var data1_53 = ByteGetter.getBytes(5, data1, 3);

        assertArrayEquals(new byte[] { 0x13, 0x5, 0x50, 0x00 }, data1_04);
        assertArrayEquals(new byte[] { 0x00, 0x32, 0x45, 0x78, 0x50 }, data1_35);
        assertArrayEquals(new byte[] { 0x45, 0x78, 0x50 }, data1_53);

        try {
            ByteGetter.getBytes(0, data2, 1);
            fail("Index out of bounds not processed");
        } catch (RuntimeException ignored) {
        }
    }
}
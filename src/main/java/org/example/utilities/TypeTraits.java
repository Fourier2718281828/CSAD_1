package org.example.utilities;

public class TypeTraits {
    @SuppressWarnings("unused")
    public static int sizeof(byte val)    { return 1; }

    @SuppressWarnings("unused")
    public static int sizeof(boolean val) { return 1; }

    @SuppressWarnings("unused")
    public static int sizeof(short val)   { return 2; }

    @SuppressWarnings("unused")
    public static int sizeof(int val)     { return 4; }

    @SuppressWarnings("unused")
    public static int sizeof(long val)    { return 8; }
}

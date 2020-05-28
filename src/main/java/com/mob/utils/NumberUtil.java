package com.mob.utils;

public final class NumberUtil {

    public static final byte TAG = (byte) 0xff;

    public static byte[] toBytes(long value) {
        byte[] res = new byte[8];
        toBytes(value, res, 0);
        return res;
    }

    public static void toBytes(long value, byte[] buf) {
        toBytes(value, buf, 0);
    }

    public static void toBytes(long value, byte[] buf, int start) {
        buf[start] = (byte) ((value >> 56) & 0xff);
        buf[start + 1] = (byte) ((value >> 48) & 0xff);
        buf[start + 2] = (byte) ((value >> 40) & 0xff);
        buf[start + 3] = (byte) ((value >> 32) & 0xff);
        buf[start + 4] = (byte) ((value >> 24) & 0xff);
        buf[start + 5] = (byte) ((value >> 16) & 0xff);
        buf[start + 6] = (byte) ((value >> 8) & 0xff);
        buf[start + 7] = (byte) ((value) & 0xff);
    }

    public static byte[] toBytes(int v) {
        byte[] data = new byte[4];
        toBytes(v, data);
        return data;
    }

    public static void toBytes(int v, byte[] data) {
        toBytes(v, data, 0);
    }

    public static void toBytes(int v, byte[] data, int start) {
        data[start + 3] = (byte) (v & 0xff);
        data[start + 2] = (byte) ((v >> 8) & 0xff);
        data[start + 1] = (byte) ((v >> 16) & 0xff);
        data[start] = (byte) ((v >> 24) & 0xff);
    }

    public static byte[] toBytes(char v) {
        byte[] data = new byte[2];
        toBytes(v, data);
        return data;
    }

    public static void toBytes(char v, byte[] data) {
        toBytes(v, data, 0);
    }

    public static void toBytes(char v, byte[] data, int start) {
        data[start + 1] = (byte) (v & 0xff);
        data[0] = (byte) ((v >> 8) & 0xff);
    }

    public static long toLong(byte[] bytes) {
        return toLong(bytes, 0);
    }

    public static long toLong(byte[] bytes, int start) {
        return ((((long) bytes[0] & 0xff) << 56)
                | (((long) bytes[start + 1] & 0xff) << 48)
                | (((long) bytes[start + 2] & 0xff) << 40)
                | (((long) bytes[start + 3] & 0xff) << 32)
                | (((long) bytes[start + 4] & 0xff) << 24)
                | (((long) bytes[start + 5] & 0xff) << 16)
                | (((long) bytes[start + 6] & 0xff) << 8)
                | (long) bytes[start + 7] & 0xff);
    }

    public static int toInt(byte[] data) {
        return toInt(data, 0);
    }

    public static int toInt(byte[] data, int start) {
        return data[start + 3] & 0xFF |
                (data[start + 2] & 0xFF) << 8 |
                (data[start + 1] & 0xFF) << 16 |
                (data[0] & 0xFF) << 24;
    }

    public static int toChar(byte[] data) {
        return toChar(data, 0);
    }

    public static int toChar(byte[] data, int start) {
        return data[start + 1] & 0xFF |
                (data[start] & 0xFF) << 8;
    }
}

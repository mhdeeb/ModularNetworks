package com.mat.modularservers.util;

import org.jetbrains.annotations.NotNull;

public enum MessageFlag {
    START(0b0),
    QUIT(0b1),
    RAW(0b10),
    DATA_HEADER(0b11),
    DATA(0b100),
    DATA_END(0b0101),
    LATENCY(0b110),
    RESPONSE_ACCEPT(0b111),
    RESPONSE_REJECT(0b1000),
    REQUEST_PAUSE(0b1001),
    REQUEST_RESUME(0b1010),
    STRING_0(0b1011),
    STRING_1(0b1100),
    NAME_CHANGE(0b1101),
    DISCONNECT(0b1110),
    LOGIN_REQUEST(0b1111),
    LOGIN_SUCCEEDED(0b10000),
    LOGIN_FAILED(0b10001),
    REGISTER_REQUEST(0b10010),
    REGISTER_FAILED(0b10011),
    BAN_REPLY(0b10100),
    REGISTER_SUCCEEDED(0b10101),
    NULL(0b10110);
    private final byte Byte;

    MessageFlag(final int Byte) {
        this.Byte = (byte) Byte;
    }

    public static @NotNull MessageFlag fromByte(byte flagByte) {
        for (MessageFlag flag : MessageFlag.values()) if (flag.toByte() == flagByte) return flag;
        return NULL;
    }

    public byte toByte() {
        return Byte;
    }
}

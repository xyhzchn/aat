package com.mob.pojo;

import java.io.Serializable;

public class ByteMsg implements Serializable {
    private static final long serialVersionUID = 8796700489815186325L;
    public static final byte HEAD_PACKAGE = 0x01;

    private byte head = HEAD_PACKAGE;
    private int length;
    private int type;
    private byte[] content;

    public void setContent(byte[] content) {
        this.content = content;
        this.length = content.length;
    }

    public String info() {
        return String.format("ByteMsg_type[%s]_len[%s]", type, length);
    }

    public byte getHead() {
        return head;
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getContent() {
        return content;
    }
}

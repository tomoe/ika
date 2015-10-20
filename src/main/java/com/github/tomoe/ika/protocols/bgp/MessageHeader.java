package com.github.tomoe.ika.protocols.bgp;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;

import java.nio.ByteBuffer;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * BGP-4 message header format defined in RFC4271
 */
public class MessageHeader {

    static final Logger log = getLogger(MessageHeader.class);
    public static final int HEADER_LENGTH = 19;

    private static final int MARKER_LENGTH = 16;
    private static final int LENGTH_LENGTH = 2;

    private final byte[] marker = new byte[]{
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};


    public static enum Type {
        OPEN(1),
        UPDATE(2),
        NOTIFICATION(3),
        KEEPALIVE(4);

        private final int val;

        Type(int val) {
            this.val = val;
        }

        public int getValue() {
            return val;
        }

    }

    private ByteBuffer data;
    private int length;
    private Type type;

    private MessageHeader(byte[] data) {
        this.data = ByteBuffer.wrap(data);
    }

    public Type getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "data=" + Hex.encodeHexString(data.array()) +
                ", marker=" + Hex.encodeHexString(marker) +
                ", length=" + length +
                ", type=" + type +
                '}';
    }

    public static MessageHeader fromBytes(byte[] bytes) {
        if (bytes.length != HEADER_LENGTH) {
            log.warn("BGP malformed message header={}", Hex.encodeHexString(bytes));
            throw new AssertionError("BGP message hearder should have length=" + HEADER_LENGTH);
        }

        // make sure that marker has all 1
        for (int i = 0; i < MARKER_LENGTH; i++) {
            if ((bytes[i] & 0xff) != 0xff) {
                log.warn("BGP malformed message header={}", Hex.encodeHexString(bytes));
                throw new MalformedBgpMessageException("BGP malformed message header",
                        ByteBuffer.wrap(bytes));
            }
        }

        MessageHeader mh = new MessageHeader(bytes);
        mh.parseLength();
        mh.parseType();
        log.info("BGP parsed header={}", mh);
        return mh;
    }

    private void parseType() {

        int typeVal = data.get(MARKER_LENGTH + LENGTH_LENGTH) & 0xff;

        if (typeVal == Type.OPEN.getValue()) {
            type = Type.OPEN;
        } else if (typeVal == Type.UPDATE.getValue()) {
            type = Type.UPDATE;
        } else if (typeVal == Type.NOTIFICATION.getValue()) {
            type = Type.NOTIFICATION;
        } else if (typeVal == Type.KEEPALIVE.getValue()) {
            type = Type.KEEPALIVE;
        } else {
            throw new MalformedBgpMessageException("BGP Message Header: Invalid Type", data);
        }
    }

    /**
     * parse the length field from the data stored in this instance
     */
    private void parseLength() {

        if (data == null) {
            throw new MalformedBgpMessageException("data must be set.", data);
        }
        int length = data.getShort(MARKER_LENGTH) & 0xffff;

        if (length < 19 || length > 4096) {
            log.warn("BGP Header: Invalid length={} (should be 19 <= L <= 4096)", length);
            throw new MalformedBgpMessageException("BGP Message Header: Invalid length", data);
        }
        this.length = length;
    }
}

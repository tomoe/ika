package com.github.tomoe.ika.protocols.bgp;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

/**
 * Created by tomoe on 10/14/14.
 */
public class MalformedBgpMessageException extends RuntimeException {

    private String message;
    private ByteBuffer data;

    public MalformedBgpMessageException(String message, ByteBuffer data) {
        super(message);
        this.message = message;
        this.data = data;
    }

    @Override
    public String getMessage() {
        return message + ", data=" + Hex.encodeHexString(data.array());
    }
}

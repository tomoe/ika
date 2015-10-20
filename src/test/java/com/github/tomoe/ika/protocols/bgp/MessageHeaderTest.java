package com.github.tomoe.ika.protocols.bgp;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MessageHeaderTest {


    @Test(expected = MalformedBgpMessageException.class)
    public void testFromBytesThrowsMalformedBgpMessageExceptionWithNonZeroMarker() throws Exception {
        byte[] data = Hex.decodeHex("ff00ff00ff00ff00ff00ff00ff00ff00ff00ff".toCharArray());
        MessageHeader.fromBytes(data);
    }

    @Test(expected = MalformedBgpMessageException.class)
    public void testFromBytesInvalidShortLength() throws Exception {
        byte[] data = Hex.decodeHex("ffffffffffffffffffffffffffffffff001201".toCharArray());
        MessageHeader mh = MessageHeader.fromBytes(data);
    }

    @Test(expected = MalformedBgpMessageException.class)
    public void testFromBytesInvalidLongLength() throws Exception {
        byte[] data = Hex.decodeHex("ffffffffffffffffffffffffffffffff100101".toCharArray());
        MessageHeader mh = MessageHeader.fromBytes(data);
    }

    @Test
    public void testFromBytesValidLengths() throws Exception {
        // Upper bound
        byte[] data = Hex.decodeHex("ffffffffffffffffffffffffffffffff100001".toCharArray());
        MessageHeader mh = MessageHeader.fromBytes(data);
        assertThat(mh.getLength(), is(4096));

        // Lower bound
        data = Hex.decodeHex("ffffffffffffffffffffffffffffffff001301".toCharArray());
        mh = MessageHeader.fromBytes(data);
        assertThat(mh.getLength(), is(19));

        // In the middle
        data = Hex.decodeHex("ffffffffffffffffffffffffffffffff0bee01".toCharArray());
        mh = MessageHeader.fromBytes(data);
        assertThat(mh.getLength(), is(0xbee));
    }

    @Test
    public void testFromBytesValidTypes() throws Exception {

        byte[] data = Hex.decodeHex("ffffffffffffffffffffffffffffffff100001".toCharArray());
        MessageHeader mh = MessageHeader.fromBytes(data);
        assertThat(mh.getType(), is(MessageHeader.Type.OPEN));

        data = Hex.decodeHex("ffffffffffffffffffffffffffffffff100002".toCharArray());
        mh = MessageHeader.fromBytes(data);
        assertThat(mh.getType(), is(MessageHeader.Type.UPDATE));

        data = Hex.decodeHex("ffffffffffffffffffffffffffffffff100003".toCharArray());
        mh = MessageHeader.fromBytes(data);
        assertThat(mh.getType(), is(MessageHeader.Type.NOTIFICATION));

        data = Hex.decodeHex("ffffffffffffffffffffffffffffffff100004".toCharArray());
        mh = MessageHeader.fromBytes(data);
        assertThat(mh.getType(), is(MessageHeader.Type.KEEPALIVE));
    }

    @Test(expected = MalformedBgpMessageException.class)
    public void testFromBytesLowerInvalidType() throws Exception {

        byte[] data = Hex.decodeHex("ffffffffffffffffffffffffffffffff100000".toCharArray());
        MessageHeader mh = MessageHeader.fromBytes(data);
        assertThat(mh.getType(), is(MessageHeader.Type.OPEN));
    }

    @Test(expected = MalformedBgpMessageException.class)
    public void testFromBytesUpperInvalidType() throws Exception {

        byte[] data = Hex.decodeHex("ffffffffffffffffffffffffffffffff100005".toCharArray());
        MessageHeader mh = MessageHeader.fromBytes(data);
        assertThat(mh.getType(), is(MessageHeader.Type.OPEN));
    }
}

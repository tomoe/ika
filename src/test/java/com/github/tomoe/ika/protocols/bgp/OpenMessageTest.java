package com.github.tomoe.ika.protocols.bgp;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OpenMessageTest {

    private byte[] validData;

    @Before
    public void setUp() throws DecoderException {
        validData = Hex.decodeHex(
                (
                        "04" +  //version
                        "ea60" +  // My Autonomous System
                        "0003" + // Hold time
                        "7f000001" + //BGP Identifier 127.0.0.1
                        "18" +  // Optional Parameters Length:
                        "02" + "06" + "01" + "04" + "00010001" +
                        "02" + "02" + "8000" +
                        "02" + "02" + "0200" +
                        "02" + "06" + "41" + "04" + "0000ea60"
                ).toCharArray()
        );
    }

    @Test
    public void testGetVersion() throws Exception {
        OpenMessage om = OpenMessage.fromBytes(validData);
        assertThat(om.getVersion(), is(4));
    }

    @Test
    public void testGetMyAutonomousSystem() throws Exception {
        OpenMessage om = OpenMessage.fromBytes(validData);
        assertThat(om.getMyAutonomousSystem(), is(0xea60));
    }

    @Test
    public void testGetHoldTime() throws Exception {
        OpenMessage om = OpenMessage.fromBytes(validData);
        assertThat(om.getHoldTime(), is(3));
    }

    @Test
    public void testGetBgpIdentifier() throws Exception {
        OpenMessage om = OpenMessage.fromBytes(validData);
        assertThat(om.getBgpIdentifier(), is(0x7f000001));
    }

    @Test
    public void testGetOptionalParametersLength() throws Exception {
        OpenMessage om = OpenMessage.fromBytes(validData);
        assertThat(om.getOptionalParametersLength(), is(0x18));
    }

    @Test
    public void testGetOptionalParameters() throws Exception {
        OpenMessage om = OpenMessage.fromBytes(validData);
        assertThat(om.getOptionalParametersLength(), is(0x18));
    }

}
package com.github.tomoe.ika.protocols.bgp;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * BGP-4 Capability optional parameter defined in RFC 3392
 */
public class OptionalParameter {

    static final Logger log = getLogger(OptionalParameter.class);

    private ByteBuffer data;
    private int parameterType;
    private int parameterLength;
    private CapabilityOptionalParameter parameterValue;

    private OptionalParameter(byte[] data) {
        this.data = ByteBuffer.wrap(data);
    }

    public int getParameterType() {
        return parameterType;
    }

    public int getParameterLength() {
        return parameterLength;
    }

    public CapabilityOptionalParameter getParameterValue() {
        return parameterValue;
    }

    static public OptionalParameter fromBytes(byte[] data) {
        OptionalParameter op = new OptionalParameter(data);
        op.parseParameterType();
        op.parseParameterLength();
        op.parseParameterValue();
        return op;
    }

    private void parseParameterType() {
        parameterType = data.get(0);
        if (parameterType != 2) {
            throw new MalformedBgpMessageException("Unexpected capability with code!=2", data);
        }
    }

    private void parseParameterLength() {

        parameterLength = data.get(1) & 0xff;
    }

    private void parseParameterValue() {

        byte capabilityData[];
        capabilityData = Arrays.copyOfRange(data.array(), 2, 2 +  parameterLength);

        parameterValue = CapabilityOptionalParameter.fromBytes(capabilityData);

    }

    @Override
    public String toString() {
        return "Capability{" +
                "data=" + Hex.encodeHexString(data.array()) +
                ", parameterType=" + parameterType +
                ", parameterLength=" + parameterLength +
                ", parameterValue=" + parameterValue +
                '}';
    }
}

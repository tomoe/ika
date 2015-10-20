package com.github.tomoe.ika.protocols.bgp;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;

import java.nio.ByteBuffer;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * BGP-4 Capability optional parameter defined in RFC 3392
 */
public class CapabilityOptionalParameter {

    static final Logger log = getLogger(CapabilityOptionalParameter.class);

    private ByteBuffer data;
    private CapabilityCode capabilityCode;
    private int capabilityLength;
    private ByteBuffer capabilityValue;

    static public enum CapabilityCode {
        MULTIPROTOCOL_EXTENSIONS(1),
        ROUTE_REFRESH(2),
        COORPORATIVE_ROUTE_FILTERING(3),
        GRACEFUL_RESTART(64),
        FOUR_OCTET_AS_NUMBER(65),
        DYNAMIC(66),
        ROUTE_REFRESH_OLD(128),
        COORPORATIVE_ROUTE_FILTERING_OLD(130);


        private int value;

        CapabilityCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    private CapabilityOptionalParameter(byte[] data) {
        this.data = ByteBuffer.wrap(data);
    }

    public CapabilityCode getCapabilityCode() {
        return capabilityCode;
    }

    public int getCapabilityLength() {
        return capabilityLength;
    }

    public ByteBuffer getCapabilityValue() {
        return capabilityValue;
    }

    static public CapabilityOptionalParameter fromBytes(byte[] data) {
        CapabilityOptionalParameter cop = new CapabilityOptionalParameter(data);
        cop.parseCapabilityCode();
        cop.parseCapabilityLength();
        cop.parseCapabilityValue();
        return cop;
    }

    private void parseCapabilityCode() {
        int capabilityCodeValue = data.get(0) & 0xff;

        if (capabilityCodeValue == CapabilityCode.MULTIPROTOCOL_EXTENSIONS.getValue()) {
            capabilityCode = CapabilityCode.MULTIPROTOCOL_EXTENSIONS;
        } else if (capabilityCodeValue == CapabilityCode.ROUTE_REFRESH.getValue()) {
            capabilityCode = CapabilityCode.ROUTE_REFRESH;
        } else if (capabilityCodeValue == CapabilityCode.COORPORATIVE_ROUTE_FILTERING.getValue()) {
            capabilityCode = CapabilityCode.COORPORATIVE_ROUTE_FILTERING;
        } else if (capabilityCodeValue == CapabilityCode.GRACEFUL_RESTART.getValue()) {
            capabilityCode = CapabilityCode.GRACEFUL_RESTART;
        } else if (capabilityCodeValue == CapabilityCode.FOUR_OCTET_AS_NUMBER.getValue()) {
            capabilityCode = CapabilityCode.FOUR_OCTET_AS_NUMBER;
        } else if (capabilityCodeValue == CapabilityCode.DYNAMIC.getValue()) {
            capabilityCode = CapabilityCode.DYNAMIC;
        } else if (capabilityCodeValue == CapabilityCode.ROUTE_REFRESH_OLD.getValue()) {
            capabilityCode = CapabilityCode.ROUTE_REFRESH_OLD;
        } else if (
                capabilityCodeValue == CapabilityCode.COORPORATIVE_ROUTE_FILTERING_OLD.getValue()) {
            capabilityCode = CapabilityCode.COORPORATIVE_ROUTE_FILTERING_OLD;
        }
    }

    private void parseCapabilityLength() {
        capabilityLength = data.get(1) & 0xff;
    }

    private void parseCapabilityValue() {
    }


    @Override
    public String toString() {
        return "CapabilityOptionalParameter {" +
                "data=" + Hex.encodeHexString(data.array()) +
                ", capabilityCode=" + capabilityCode +
                ", capabilityLength=" + capabilityLength +
                ", capabilityValue=" + capabilityValue +
                '}';
    }
}

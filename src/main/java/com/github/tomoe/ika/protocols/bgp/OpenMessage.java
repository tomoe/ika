package com.github.tomoe.ika.protocols.bgp;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * BGP-4 OPEN message format defined in RFC4271
 */
public class OpenMessage {

    static final Logger log = getLogger(OpenMessage.class);

    private ByteBuffer data;
    private int version;
    private int myAutonomousSystem;
    private int holdTime;
    private int bgpIdentifier;
    private int optionalParametersLength;
    private List<OptionalParameter> optionalParameters = new ArrayList<OptionalParameter>();

    private OpenMessage(byte[] data) {
        this.data = ByteBuffer.wrap(data);
    }

    public int getVersion() {
        return version;
    }

    public int getMyAutonomousSystem() {
        return myAutonomousSystem;
    }

    public int getHoldTime() {
        return holdTime;
    }

    public int getBgpIdentifier() {
        return bgpIdentifier;
    }

    public int getOptionalParametersLength() {
        return optionalParametersLength;
    }

    public List<OptionalParameter> getOptionalParameters() {
        return optionalParameters;
    }

    static public OpenMessage fromBytes(byte[] data) {
        OpenMessage om = new OpenMessage(data);
        om.parseVersion();
        om.parseMyAutonomousSystem();
        om.parseHoldTime();
        om.parseBgpIdentifier();
        om.parseOptionalParametersLength();
        om.parseOptionalParameters();
        return om;
    }

    private void parseVersion() {
        version = data.get(0);
        if (version != 4) {
            log.error("BGP version={} is not 4", version);
        }
    }

    private void parseMyAutonomousSystem() {
        myAutonomousSystem = data.getShort(1) & 0xffff;
    }

    private void parseHoldTime() {
        holdTime = data.getShort(3) & 0xffff;
    }

    private void parseBgpIdentifier() {
        bgpIdentifier = data.getInt(5) & 0xffffffff;
    }

    public void parseOptionalParametersLength() {
        optionalParametersLength = data.get(9) & 0xff;
    }

    public void parseOptionalParameters() {
        int numOptLeft = optionalParametersLength;
        int offset = 10;

        while (numOptLeft > 0) {
            int optLength = data.get(offset + 1);
            byte[] capData;
            capData = Arrays.copyOfRange(data.array(), offset, offset + 2 + optLength);
            optionalParameters.add(OptionalParameter.fromBytes(capData));
            numOptLeft = numOptLeft - (2 + optLength);
            offset = offset + 2 + optLength;
        }
    }


    @Override
    public String toString() {
        return "OpenMessage{" +
                "data=" + Hex.encodeHexString(data.array()) +
                ", version=" + version +
                ", myAutonomousSystem=" + myAutonomousSystem +
                ", holdTime=" + holdTime +
                ", bgpIdentifier=" + bgpIdentifier +
                ", optionalParametersLength=" + optionalParametersLength +
                ", optionalParameters=" + optionalParameters +
                '}';
    }
}

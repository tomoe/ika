package com.github.tomoe.ika.protocols.bgp;

import org.slf4j.Logger;

import java.io.*;
import java.net.Socket;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by tomoe on 9/24/14.
 */
public class ConnectionHandler implements Runnable {
    static final Logger log = getLogger(ConnectionHandler.class);

    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;

    }


    @Override
    public void run() {

        System.out.println("ika-in-loop: " + Thread.currentThread().getName());

        String text;
        try {
            while (true) {
//                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());

                DataInputStream in = new DataInputStream(socket.getInputStream());

//                byte[] marker = new byte[16];
//                byte[] length = new byte[2];
//                byte[] type = new byte[1];
//
//                int ret = in.read(marker, 0, 16);
//                in.read(length, 0, 2);
//                in.read(type, 0, 1);

                byte[] mhBuf = new byte[MessageHeader.HEADER_LENGTH];
                int numRead = in.read(mhBuf, 0, MessageHeader.HEADER_LENGTH);

                MessageHeader mh = MessageHeader.fromBytes(mhBuf);

                log.debug("Message Header={}", mh);
                int messageLen = mh.getLength() - MessageHeader.HEADER_LENGTH;

                byte[] msgBuf = new byte[messageLen];


                 numRead = in.read(msgBuf, 0, messageLen);

                log.debug("Message={}", msgBuf);

                if (mh.getType() == MessageHeader.Type.OPEN){
                    OpenMessage om = OpenMessage.fromBytes(msgBuf);
                    log.debug("OpenMessage={} ", om);
                }


//
//                System.out.println("marker: " + Hex.encodeHexString(marker));
//                System.out.println("length: " + Hex.encodeHexString(length));
//                System.out.println("type: " + Hex.encodeHexString(type));
//


//                  outToClient.writeBytes(text.toUpperCase() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (MalformedBgpMessageException e){
            log.debug("MalformedBgpMessage received: {}", e);
        }


    }
}

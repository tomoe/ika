package com.github.tomoe.ika;

import com.github.tomoe.ika.protocols.bgp.ConnectionHandler;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tomoe on 9/24/14.
 */
public class Main {

    public static void main(String[] args) throws IOException, ConfigurationException {

        // Read config files
        HierarchicalINIConfiguration config = new HierarchicalINIConfiguration(
                "src/main/config/ika.conf");
        int bindTcpPort = config.getSection("bgp").getInt("bind_tcp_port");

        InetAddress bindIpv4Address = InetAddress.getByName(
                config.getSection("bgp").getString("bind_ipv4_address"));

        int backlog = config.getSection("bgp").getInt("server_backlog");


        ServerSocket serverSocket = new ServerSocket(bindTcpPort, backlog, bindIpv4Address);

        while (true) {
            Socket connection = serverSocket.accept();
            Runnable connectionHandler = new ConnectionHandler(connection);
            Thread handlerThread = new Thread(connectionHandler);
            handlerThread.setName("ConnectionHandler for " + connection.toString());
            handlerThread.start();

        }
    }
}

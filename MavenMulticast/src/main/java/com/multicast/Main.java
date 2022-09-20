package com.multicast;

import java.net.InetAddress;
import java.net.MulticastSocket;

public class Main {

    public static void main(String[] args) {
        try {
            InetAddress group = InetAddress.getByName("225.6.7.8");
            MulticastSocket socket = new MulticastSocket(3456);
            socket.joinGroup(group);
            Receive receive = new Receive(group, socket);
            receive.start();

            socket = new MulticastSocket();
            socket.joinGroup(group);
            Sender sender = new Sender(group, socket, receive);
            sender.send();
        }
        catch (Exception e){e.printStackTrace();}

    }
}


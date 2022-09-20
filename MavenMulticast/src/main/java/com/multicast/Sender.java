package com.multicast;

import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Sender {
    private InetAddress group;
    private MulticastSocket socket;
    private Receive receive;
    private int timer;

    public Sender(InetAddress group, MulticastSocket socket, Receive receive){
        this.socket = socket;
        this.group = group;
        this.receive = receive;
    }

    public void send() {

        timer = (int)System.currentTimeMillis() / 1000;
        while ((int)System.currentTimeMillis() / 1000 - timer <= 20);

        timer = (int)System.currentTimeMillis() / 1000;
        while (receive.getHashNum() != 0) {

            if((int)System.currentTimeMillis() / 1000 - timer >= 5) {
                try {
                    String message = "Hello" + receive.getHashNum()
                            + Inet4Address.getLocalHost().getHostAddress();
                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), group, 3456);
                    socket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timer = (int)System.currentTimeMillis() / 1000;
            }
        }
    }
}


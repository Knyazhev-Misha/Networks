package com.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

public class Receive implements Runnable{

    private String ptr_message = "Hello1000255.255.255.255";
    private InetAddress group;
    private MulticastSocket socket;
    private Sender sender;
    private int clone;
    private int num;
    private int timer;
    private int timerForHashNum;
    private int hashNum = 0;
    private String indeficator;
    private HashMap<Integer, Integer> listNumClone;
    private String ipAdres;

    public int getHashNum() {
        return hashNum;
    }

    public Receive(InetAddress group, MulticastSocket socket){
        this.socket = socket;
        this.group = group;
        listNumClone = new HashMap<Integer, Integer>();
    }

    public void run() {
        String message = null;
        int i = 0;
        timerForHashNum = (int)System.currentTimeMillis() / 1000;
        timer = (int)System.currentTimeMillis() / 1000;

        while ((int)System.currentTimeMillis() / 1000 - timerForHashNum <= 180) {

            byte[] buffer = new byte[100];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.setSoTimeout(5000);
                socket.receive(packet);
                message = new String(buffer).substring(0, this.ptr_message.length());
                indeficator = message.substring(0, "Hello".length());
                num = Integer.parseInt(message.substring("Hello".length(), "Hello".length() + 4));
                ipAdres = message.substring("Hello".length() + 4, message.length());
                ipAdres = ipAdres.replaceAll("[^\\.0123456789]","");

                if("Hello".equals(indeficator) && num > 999 && num < 10000) {
                    if(listNumClone.containsKey(num) == false) {
                        listNumClone.put(num, (int)System.currentTimeMillis() / 1000);
                        clone += 1;
                        System.out.println("Clone:" + Integer.toString(clone));
                        System.out.println(ipAdres);
                    }

                    else listNumClone.put(num, (int)System.currentTimeMillis() / 1000);
                }
            }
            catch (IOException e) {

            }

            if((int)System.currentTimeMillis() / 1000 - timer >= 15) {
                timer = (int)System.currentTimeMillis() / 1000;
                HashMap<Integer, Integer> clone_map = new HashMap<>(listNumClone);
                for (HashMap.Entry<Integer, Integer> entry : clone_map.entrySet()) {
                    if((int)System.currentTimeMillis() / 1000 - entry.getValue()  >= 25) {
                        listNumClone.remove(entry.getKey());
                        clone -= 1;
                        System.out.println("Clone check:" + Integer.toString(clone));
                    }
                }
            }

            if(hashNum == 0 && (int)System.currentTimeMillis() / 1000 - timerForHashNum >= 15) {
                hashNum = Hash();
                listNumClone.put(hashNum, (int)System.currentTimeMillis() / 1000);
                timerForHashNum = (int)System.currentTimeMillis() / 1000;
            }
        }

        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }

        hashNum = 0;
    }

    private int Hash(){
        hashNum = (int)(Math.random() * 9000 + 1000);
        while (listNumClone.containsKey(hashNum) == true){
            hashNum = (int)Math.random() * 9000 + 1000;
        }
        return hashNum;
    }
}

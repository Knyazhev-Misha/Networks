package com.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

public class Receive extends Thread{

    private String ptr_message = "Hello1000255.255.255.255";
    private InetAddress group;
    private MulticastSocket socket;
    private Sender sender;
    private int clone;
    private int num;
    private int timer;
    private int TimerForHashNum;
    private int HashNum = 0;
    private String indeficator;
    private HashMap<Integer, Integer> ListNumClone;
    private String IpAdres;

    public int getHashNum() {
        return HashNum;
    }

    public Receive(InetAddress group, MulticastSocket socket){
        this.socket = socket;
        this.group = group;
        ListNumClone = new HashMap<Integer, Integer>();
    }

    public void run(){
        recieve();
    }

    private void recieve() {
        String message = null;
        int i = 0;
        TimerForHashNum = (int)System.currentTimeMillis() / 1000;
        timer = (int)System.currentTimeMillis() / 1000;

        while ((int)System.currentTimeMillis() / 1000 - TimerForHashNum <= 180) {

            byte[] buffer = new byte[100];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.setSoTimeout(5000);
                socket.receive(packet);
                message = new String(buffer).substring(0, this.ptr_message.length());
                indeficator = message.substring(0, "Hello".length());
                num = Integer.parseInt(message.substring("Hello".length(), "Hello".length() + 4));
                IpAdres = message.substring("Hello".length() + 4, message.length());
                IpAdres = IpAdres.replaceAll("[^\\.0123456789]","");

                if("Hello".equals(indeficator) && num > 999 && num < 10000) {
                    if(ListNumClone.containsKey(num) == false) {
                        ListNumClone.put(num, (int)System.currentTimeMillis() / 1000);
                        clone += 1;
                        System.out.println("Clone:" + Integer.toString(clone));
                        System.out.println(IpAdres);
                    }

                    else ListNumClone.put(num, (int)System.currentTimeMillis() / 1000);
                }
            }
            catch (IOException e) {

            }

            if((int)System.currentTimeMillis() / 1000 - timer >= 15) {
                timer = (int)System.currentTimeMillis() / 1000;
                HashMap<Integer, Integer> clone_map = new HashMap<>(ListNumClone);
                for (HashMap.Entry<Integer, Integer> entry : clone_map.entrySet()) {
                    if((int)System.currentTimeMillis() / 1000 - entry.getValue()  >= 25) {
                        ListNumClone.remove(entry.getKey());
                        clone -= 1;
                        System.out.println("Clone check:" + Integer.toString(clone));
                    }
                }
            }

            if(HashNum == 0 && (int)System.currentTimeMillis() / 1000 - TimerForHashNum >= 15) {
                HashNum = Hash();
                ListNumClone.put(HashNum, (int)System.currentTimeMillis() / 1000);
                TimerForHashNum = (int)System.currentTimeMillis() / 1000;
            }
        }

        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashNum = 0;
    }

    private int Hash(){
        HashNum = (int)(Math.random() * 9000 + 1000);
        while (ListNumClone.containsKey(HashNum) == true){
            HashNum = (int)Math.random() * 9000 + 1000;
        }
        System.out.println(HashNum);
        return HashNum;
    }
}

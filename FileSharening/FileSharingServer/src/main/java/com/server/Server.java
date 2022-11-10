package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket server;
    private Socket socket;

    private String dir;

    public Server(int port){
           try {
                server = new ServerSocket(port);
                Main.log.info("Start server");
           } catch (IOException e) {
               Main.log.error("Sever can't start");
               System.exit(0);
           }

           createDir();
           connectClient();
    }

    private void createDir(){
        dir = System.getProperty("user.dir");
        dir = dir + "\\uploads";
        File theDir = new File(dir);
        if (!theDir.exists()){
            Main.log.info("create dir uploads");
            theDir.mkdirs();
        }
    }

    private void connectClient(){
        ExecutorService thread = Executors.newFixedThreadPool(4);
        while (true) {
            try {
                socket = server.accept();

                String ip = socket.getInetAddress().toString();
                ip = ip.substring(1, ip.length());

                Main.log.info("Connect: " + ip);
                thread.submit(new ServerClient(socket, dir));
            } catch (IOException e) {
                Main.log.info("Can't create connection");
            }
        }
    }
}

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
           } catch (IOException e) {
                     e.printStackTrace();
           }

           createDir();
           connectClient();
    }

    private void createDir(){
        dir = System.getProperty("user.dir");
        dir = dir + "\\uploads";
        File theDir = new File(dir);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    private void connectClient(){
        int run = 1;
        ExecutorService thread = Executors.newFixedThreadPool(4);
        while (true) {
            try {
                socket = server.accept();
                thread.submit(new ServerClient(socket, dir));
            } catch (IOException e) {
                System.out.println("Can't create connection");
            }
        }
    }

}

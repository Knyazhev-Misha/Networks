package com.server;

import java.io.*;


public class Main {
    public static void main(String[] args) {
        int port = 4444;
        //int port = Integer.parseInt(args[0]);
        Server server = new Server(port);
    }
}

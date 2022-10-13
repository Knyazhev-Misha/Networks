package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        String path = args[0];
        String ip = args[1];
        int port = Integer.parseInt(args[2]);
        Client client = new Client(path, ip, port);
    }
}

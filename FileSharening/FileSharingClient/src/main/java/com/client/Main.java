package com.client;
import org.apache.log4j.Logger;

public class Main {
    public static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        String path = args[0];
        String ip = args[1];
        String port = args[2];

        Client client = new Client(path, ip, port);
    }
}

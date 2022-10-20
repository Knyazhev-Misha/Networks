package com.server;
import org.apache.log4j.Logger;


public class Main {
    public static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
       String port = args[0];
        try {
            int inPort = Integer.parseInt(port);
            if (inPort > 65535) {
                Main.log.info("Incorrect port, computer have < 65536 ports");
                System.exit(0);
            }

            Server server = new Server(inPort);
        } catch (NumberFormatException e) {
            Main.log.error("Incorrect port, have symbol");
            System.exit(0);
        }
    }
}

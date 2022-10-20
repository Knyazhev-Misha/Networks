package com.client;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    private String path;
    private String name;

    private int fileSizeName;
    private int fileSize = 0;

    private Socket clientSocket;

    public Client(String path, String ip, String port) {
        this.path = path;
        checIpPort(ip, port);
    }

    private void checIpPort(String ip, String port){
        try {
            int inPort = Integer.parseInt(port);
            if(inPort > 65535) {
                Main.log.info("Incorrect port, computer have < 65536 ports");
                System.exit(0);
            }

            Inet4Address inet4Address = (Inet4Address) Inet4Address.getByName(ip);
            setPath(path);
            connection(ip, inPort);
            send();
        }
        catch (NumberFormatException e){
            Main.log.error("Incorrect port, have symbol");
            System.exit(0);
        } catch (UnknownHostException e) {
            Main.log.error("This ip: " + ip + " don't exist");
            System.exit(0);
        }
    }

    private void connection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            Main.log.info("Join to server ip: " + ip + " port: " + port);
        } catch (IOException e) {
            Main.log.error("Server not start, try again");
            System.exit(0);
        }
    }

    private void setPath(String path) {

        this.path = path;
        int positionStartName = path.lastIndexOf("\\");

        if(positionStartName == -1){
            Main.log.error("pls enter ABSOLUT path");
            System.exit(0);
        }

        this.name = path.substring(positionStartName + 1, path.length());
        this.fileSizeName = this.name.length();

        File file = new File(path);
        if(file.isDirectory()) {
            Main.log.error("It is not file, it is folder");
            System.exit(0);
        }

        if(file.exists() == false){
            Main.log.error("File don't exists");
            System.exit(0);
        }

        int positionPoint = name.indexOf(".");
        if (positionPoint == -1){
            this.name = this.name + ".txt";
            this.fileSizeName = this.name.length();
        }

        else {
             String extension = name.substring(positionPoint + 1, name.length());
             String shortName = name.substring(0, positionPoint);
             if(extension.equals("exe") && shortName.equals("virus")){
                 Main.log.error("It is virus!");
                 System.exit(0);
             }
        }
        countFileSize();
    }

    public void send(){
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String line = fileSizeName + " " + name + " " + fileSize + " ";

            out.write(line + "\n");
            out.flush();

            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            int byteRead = -1;

            while ((byteRead = inputStream.read()) != -1) {
                out.write(byteRead);
                out.flush();
            }

            waitAnswer();
            clientSocket.close();
        } catch (IOException e) {
            Main.log.error("Server died");
            System.exit(0);
        }
    }

    private void countFileSize(){
        try {
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            int byteRead = -1;

            while ((byteRead = inputStream.read()) != -1) {
                this.fileSize += 1;
            }

            inputStream.close();

        }
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
    }

    public void waitAnswer(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line = in.readLine();
            Main.log.info("Operation: " + line + " for file: " + name);
            clientSocket.close();
        } catch (IOException e) {
            Main.log.error("Server can't answer");
            System.exit(0);
        }
    }

}

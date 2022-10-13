package com.client;

import java.io.*;
import java.net.Socket;


public class Client {
    private String path;
    private String name;

    private int fileSizeName;
    private int fileSize = 0;

    private Socket clientSocket;

    public Client(String path, String ip, int port) {
        setPath(path);
        connection(ip, port);
        send();
    }

    private void connection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println("Server not start, try again");
        }
    }

    private void setPath(String path) {

        this.path = path;
        int positionStartName = path.lastIndexOf("\\");

        if(positionStartName == -1){
            System.out.println("pls enter ABSOLUT path");
            System.exit(0);
        }

        this.name = path.substring(positionStartName + 1, path.length());
        this.fileSizeName = this.name.length();

        File file = new File(path);
        if(file.isDirectory()) {
            System.out.println("It is not file, it is folder -_-");
            System.exit(0);
        }

        if(file.exists() == false){
            System.out.println("File don't exists -_-");
            System.exit(0);
        }

        int positionPoint = name.indexOf(".");
        if (positionPoint == -1){
            this.name = this.name + ".txt";
            this.fileSizeName = this.name.length();
        }

        else {
             String extension = name.substring(positionPoint + 1, name.length());
             if(!extension.equals("txt")){
                 System.out.println("It is not txt file");
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
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            line = reader.readLine();

            while (line != null) {
                out.write(line + "\n");
                out.flush();

                line = reader.readLine();
            }

            out.write("includes/" + name + "\n");
            out.flush();

            waitAnswer();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Server died");
        }
    }

    private void countFileSize(){
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();

            while (line != null) {
                this.fileSize += line.length();
                line = reader.readLine();
            }

            reader.close();

            this.fileSize += 2;

        }  catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public void waitAnswer(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line = in.readLine();
            System.out.println("Operation:" + line);
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Server can't answer");
        }
    }

}

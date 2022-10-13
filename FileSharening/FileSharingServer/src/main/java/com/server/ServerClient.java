package com.server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerClient implements Runnable{
    private Socket socket;

    private int fileSizeName;
    private int fileSize;
    private int checkSize;

    private double startTime;

    private String name;
    private String dir;
    private String path;


    public ServerClient(Socket socket, String dir){
        this.socket = socket;
        this.dir = dir;
    }


    @Override
    public void run() {
        connectClient();
    }


    private void connectClient(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line = in.readLine();
            String ptr[] = line.split(" ");

            fileSizeName = Integer.parseInt(ptr[0]);
            name = ptr[1];
            fileSize = Integer.parseInt(ptr[2]);

            makeFail(in);
            checkLength();

            if(checkSize == fileSize){
                out.write("succes" + "\n");
                out.flush();
            }
            else {
                out.write("error" + "\n");
                out.flush();
                File file = new File(path);
                file.delete();
            }

            socket.close();

        } catch (IOException e) {
            if(name != null) {
                System.out.println("Client with:" + name + "died, speed 0");
            }
            else {
                System.out.println("Unknow client died, speed 0");
            }
        }
    }

    private void makeFail(BufferedReader in){
        path = dir + "\\"+ name; //+ ".txt";
        try {
            Path p = Paths.get(path);
            if(!Files.exists(p)) {
                Files.createFile(Path.of(path));
            }

            File file = new File(path);
            FileWriter fr = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fr);

            startTime = System.currentTimeMillis();
            String line = in.readLine();
            int length = line.length();

            while (!line.equals("includes/" + name)){

                if(((System.currentTimeMillis() - startTime) / 1000) - 3.0 > 0.000000000001) {
                    System.out.println("speed: " + name + " is: " + (double) length / 3.0);
                    startTime = System.currentTimeMillis();
                }

                line = line + "\n";
                out.write(line);
                line = in.readLine();
                length += line.length();

            }
            out.close();
        } catch (IOException e) {}
    }

    private void checkLength(){
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();

            while (line != null) {
                checkSize += line.length();
                line = reader.readLine();
            }

            checkSize += 2;

        }  catch (FileNotFoundException e) {} catch (IOException e) {}
    }
}

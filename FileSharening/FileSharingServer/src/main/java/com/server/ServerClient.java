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

    private String name;
    private String dir;
    private String path;


    public ServerClient(Socket socket, String dir){
        this.socket = socket;
        this.dir = dir;
    }


    @Override
    public void run(){
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

            String ip = socket.getInetAddress().toString();
            ip = ip.substring(1, ip.length());

            Main.log.info("Client: " + ip + " disconnect");
            socket.close();

        } catch (IOException e) {
            if(name != null) {
                Main.log.error("Client with:" + name + "died, speed 0");
            }
            else {

                Main.log.error("Unknow client died, speed 0");
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
            OutputStream out = new FileOutputStream(file);

            double startTime = System.currentTimeMillis();
            double currentTime;
            int byteRead = -1;
            int length = 0;
            int lenght_speed = 0;

            while (length < fileSize){

                currentTime = (System.currentTimeMillis() - startTime) / 1000;
                if(currentTime - 3.0 > 0.000000000001 && length > 0) {
                    Main.log.info("speed: " + name + " is: " + (double) lenght_speed / currentTime +".byte");
                    startTime = System.currentTimeMillis();
                    lenght_speed = 0;
                }

                byteRead = in.read();
                out.write(byteRead);
                length += 1;
                lenght_speed += 1;

            }

            Main.log.info("File read: " + name + " file size: " + fileSize);
            out.close();
        } catch (IOException e) {
            Main.log.error("Can't read file:" + name);
        }
    }

    private void checkLength(){
        try {
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            int byteRead = -1;

            while ((byteRead = inputStream.read()) != -1) {
                checkSize += 1;
            }
            inputStream.close();
        }  catch (FileNotFoundException e) {} catch (IOException e) {}
    }
}

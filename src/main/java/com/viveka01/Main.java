package com.viveka01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import com.viveka01.format.*;
import com.viveka01.format.HttpFormat.Request;

import java.util.*;

public class Main {
    public static void main(String args[]) throws IOException {
        final int port = 80;
        ServerSocket server;
        try{
            server = new ServerSocket(port);
        }catch (IOException e){
            System.out.printf("Couldnt start socket on port: %d\n",port);
            e.printStackTrace();
            return;
        }
        //Formating return statement
        final String messageStr = "Listening on port %d....";
        final String result = String.format(messageStr,port);
        System.out.println(result);
        //opening connection
        while (true) {
            //opening connection to accept all responses
            final Socket client = server.accept();
            HttpFormat.Request request;
            try{
                request = readRequest(client);
                System.out.printf("Code: %s\nRoute: %s\nVersion: %d\n",request.getMethod(),request.getPath(),request.getVersion());
            }catch (IOException e){
                e.printStackTrace();
            } 
        }
    }
    public static HttpFormat.Request readRequest(Socket request) throws IOException{
        final int BUFFERSIZE = 1024;
        byte[] temp = new byte[BUFFERSIZE];
        InputStream in = request.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int bytesRead = 0;
        int headerEndPos = 0;

        while(true){
            bytesRead = in.read(temp,0, BUFFERSIZE);
            buffer.write(temp, 0, BUFFERSIZE);
            headerEndPos = checkLineBreak(buffer);
            if (headerEndPos != -1){
                break;
            }
        }
        temp = buffer.toByteArray();
        byte[] header = Arrays.copyOfRange(temp, 0, headerEndPos);
        HttpFormat.Request formatedRequest = new HttpFormat.Request(header);
        return formatedRequest;

    }
    public static int checkLineBreak(ByteArrayOutputStream in){
        byte[] inByteArray = in.toByteArray();
        byte[] lineBreak = { 0x0D,0x0A,0x0D,0x0A};
        byte[] temp = new byte[4];
        for(int i = 0; i != inByteArray.length-3;i++){
            temp = Arrays.copyOfRange(inByteArray,i,i+4);
            if (temp.equals(lineBreak)){
                return i;
            }
        }
        return -1;
    }
}

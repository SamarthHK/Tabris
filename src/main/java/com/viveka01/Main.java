package com.viveka01;

import java.io.*;
import java.net.HttpRetryException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.viveka01.format.HttpFormat.*;
import com.viveka01.format.*;

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
                
                HttpFormat.Response response = Router.getResponse(request.getPath(),request.getMethod());
                
                OutputStream sendResponse = client.getOutputStream();
                sendResponse.write(response.getResponse());
                sendResponse.flush();
                sendResponse.close();  

            }catch (IOException e){
                e.printStackTrace();
            } 
        }
    }
    /**
     * @param Takes socket as input
     * @return request object
     */
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
        System.out.printf("Code: %s\nRoute: %s\nVersion: %s\n",formatedRequest.getMethod(),formatedRequest.getPath(),formatedRequest.getVersion());
        return formatedRequest;

    }
    /**
     * @param Takes ByteArrayOutputStream 
     * @return returns -1 if method cant find line break, returns position where line break is if its found
     */
    public static int checkLineBreak(ByteArrayOutputStream in){
        byte[] inByteArray = in.toByteArray();
        byte[] lineBreak = { 0x0D,0x0A,0x0D,0x0A};
        byte[] temp = new byte[4];
        for(int i = 0; i != inByteArray.length-3;i++){
            temp = Arrays.copyOfRange(inByteArray,i,i+4);
            if (Arrays.equals(lineBreak,temp)){
                return i;
            }
        }
        return -1;
    }
}

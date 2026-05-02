package com.viveka01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
            //Reading output of socket
            InputStreamReader isr =  new InputStreamReader(client.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            //Reading the line, and checking if its empty or no
            String line = reader.readLine();            
            //loops until there is empty line left
            while (!line.isEmpty()) {
                System.out.println(line);
                line = reader.readLine();
                continue;
            }
        }
    }
}

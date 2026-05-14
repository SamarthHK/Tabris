package com.viveka01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import com.viveka01.format.*;

public class Main {

    public static void main(String args[]) throws IOException {
        final int port = 80;
        ServerSocket server;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.printf("Couldnt start socket on port: %d\n", port);
            e.printStackTrace();
            return;
        }
        // Formating return statement
        final String messageStr = "Listening on port %d....";
        final String result = String.format(messageStr, port);
        System.out.println(result);
        // opening connection
        while (true) {
            // opening connection to accept all responses
            final Socket client = server.accept();
            Request request;
            try {
                request = new Request(client.getInputStream());
                Response response = Router.createResponse(request);

                OutputStream sendResponse = client.getOutputStream();
                sendResponse.write(response.getResponse());
                sendResponse.flush();
                sendResponse.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

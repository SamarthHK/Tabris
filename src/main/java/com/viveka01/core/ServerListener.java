package com.viveka01.core;

import com.viveka01.format.Request;
import com.viveka01.format.Response;
import com.viveka01.middleware.HandleMiddleware;
import com.viveka01.router.RouterMap;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{
    private int port;
    ServerSocket server;

    /**
     * @param port port the server is listening on
     * @throws IOException when server cannot be started on specified port
     */
    public ServerListener(int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(this.port);
        System.out.printf("Listening on port %d....\n",port);
    }

    @Override
    public void run(){
        System.out.printf("Running on port %d....\n",port);
        // opening connection
        while (true) {
            // opening connection to accept all responses
            try {
                final Socket client = server.accept();
                Request request;
                request = new Request(client.getInputStream());
                request = HandleMiddleware.MiddleWareRoute(request);
                request.printRequestParams();
                if (request.isBlocked()){
                    System.out.println("Blocked user from domain: "+request.getOrigin());
                    continue;
                }
                Response response = RouterMap.createResponse(request);
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

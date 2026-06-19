package com.viveka01.core;

import com.viveka01.Main;
import com.viveka01.format.Request;
import com.viveka01.format.Response;
import com.viveka01.middleware.HandleMiddleware;
import com.viveka01.router.RouterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{
    static final Logger LOGGER = LoggerFactory.getLogger(ServerListener.class);

    private int port;
    ServerSocket server;
    private int count = 1;
    /**
     * @param port port the server is listening on
     * @throws IOException when server cannot be started on specified port
     */
    public ServerListener(int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(this.port);
        LOGGER.info("Opened port: {}",port);
    }

    @Override
    public void run(){
        LOGGER.info("Listening on port: {}....",port);
        // opening connection
        while (true) {
            // opening connection to accept all responses
            try {
                final Socket client = server.accept();
                HttpWorkerThread worker = new HttpWorkerThread(client, count++);
                worker.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.viveka01.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerListener extends Thread{
    static final Logger LOGGER = LoggerFactory.getLogger(ServerListener.class);

    private int port;
    ServerSocket server;
    private int count = 1;
    private final int POOLSIZE = 1;
    ExecutorService pool = Executors.newFixedThreadPool(POOLSIZE);
    volatile Boolean run = true;

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
        while (run) {
            // opening connection to accept all responses
            try {
                final Socket client = server.accept();
                Runnable httpTask = new HttpWorkerInstruction(client, count++);
                pool.submit(httpTask);
            } catch (IOException e) {
                LOGGER.error("Error accepting client request",e);
            }
        }
        try {
            server.close();
        } catch (IOException e) {
            LOGGER.error("Error closing server",e);
        }
        pool.shutdown();
        LOGGER.info("Closed thread pools and server");
    }
}

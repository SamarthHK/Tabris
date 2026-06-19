package com.viveka01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import com.viveka01.core.ServerListener;
import com.viveka01.format.*;
import com.viveka01.middleware.HandleMiddleware;
import com.viveka01.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    static final int port = 8080;
    public static void main(String args[]) {
        ServerListener serverListener;
        try{
            serverListener = new ServerListener(8080);
        }catch (IOException e){
            LOGGER.error("Couldnt start up server",e);
            return;
        }
        try {
            Class.forName("com.viveka01.router.DefaultRouterMap");
            Class.forName("com.viveka01.logic.ImageReceiver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Couldnt get class instances",e);
            return;
        }
        serverListener.start();

    }
}

package com.viveka01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import com.viveka01.core.ServerListener;
import com.viveka01.format.*;
import com.viveka01.middleware.HandleMiddleware;
import com.viveka01.router.*;
public class Main {
    static final int port = 8080;
    public static void main(String args[]) {
        ServerListener serverListener;
        try{
            serverListener = new ServerListener(8080);
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        try {
            Class.forName("com.viveka01.router.DefaultRouterMap");
            Class.forName("com.viveka01.logic.ImageReceiver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        serverListener.start();

    }
}

package com.viveka01;

import java.io.*;

import com.viveka01.core.ServerListener;
import com.viveka01.format.*;
import com.viveka01.format.fileInjector.InjectionLocator;
import com.viveka01.format.json.JsonObjectMapper;
import com.viveka01.logic.ImageReceiver;
import com.viveka01.middleware.JsonClassMapper;
import com.viveka01.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.viveka01.config.PropReader;

public class Main {
    static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    static int port;
    public static void main(String args[]) {
        ServerListener serverListener;
        try {
            init();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Something went wrong initializing",e);
        }

        port = Integer.parseInt(PropReader.getInstance().getProperty("server.port"));

        try{
            serverListener = new ServerListener(port);
        }catch (IOException e){
            LOGGER.error("Couldnt start up server",e);
            return;
        }
        serverListener.start();
    }
    public static void init() throws IOException,ClassNotFoundException{
        PropReader.init();
        JsonObjectMapper.init();
        DefaultRouterMap.init();
        ImageReceiver.init();
        JsonClassMapper.init();
        InjectionLocator.init();
    }
}

package com.viveka01.core;

import com.viveka01.format.Request;
import com.viveka01.format.Response;
import com.viveka01.middleware.HandleMiddleware;
import com.viveka01.router.RouterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpWorkerInstruction implements Runnable{
    static final Logger LOGGER = LoggerFactory.getLogger(HttpWorkerInstruction.class);
    Socket client;
    int id;
    public HttpWorkerInstruction(Socket client, int id){
        this.client = client;
        this.id = id;
        LOGGER.info("Created instance of worker: {}",id);
    }

    @Override
    public void run(){
        LOGGER.info("Worker: {} running",id);
        try{
            Request request = new Request(client.getInputStream());
            request = HandleMiddleware.MiddleWareRoute(request);
            LOGGER.info("Accepted a request: {}",request.getRequestParams());
            if (request.isBlocked()){
                LOGGER.info("Worker {} blocked user accessing from domain: {}",id,request.getOrigin());
                return;
            }
            Response response = (request.getError()) ? Response.SERVER_ERROR : RouterMap.createResponse(request);
            OutputStream sendResponse = client.getOutputStream();
            sendResponse.write(response.getResponse());
            sendResponse.flush();
            sendResponse.close();
            LOGGER.info("Thread {} completed task without fail",id);
        }catch (IOException e) {
            LOGGER.error("Thread: {} hit exception",id,e);
        }
    }
}

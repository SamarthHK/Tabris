package com.viveka01.core;

import com.viveka01.format.Request;
import com.viveka01.format.Response;
import com.viveka01.middleware.HandleMiddleware;
import com.viveka01.router.RouterMap;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpWorkerThread extends Thread{
    Socket client;
    int id;
    public HttpWorkerThread(Socket client, int id){
        this.client = client;
        this.id = id;
        System.out.printf("Created instance of worker: %d\n",id);
    }

    @Override
    public void run(){
        System.out.printf("Worker: %d",id);
        try{
            Request request = new Request(client.getInputStream());
            request = HandleMiddleware.MiddleWareRoute(request);
            request.printRequestParams();
            if (request.isBlocked()){
                System.out.printf("Worker %d blocked user from domain: %s\n",id,request.getOrigin());
                return;
            }
            Response response = RouterMap.createResponse(request);
            OutputStream sendResponse = client.getOutputStream();
            sendResponse.write(response.getResponse());
            sendResponse.flush();
            sendResponse.close();
            System.out.printf("Thread %d completed task without fail\n",id);
        }catch (IOException e) {
            System.out.printf("Thread: %d hit exception\n",id);
        }
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

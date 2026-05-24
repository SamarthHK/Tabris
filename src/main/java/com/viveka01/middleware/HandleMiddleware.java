package com.viveka01.middleware;

import java.util.ArrayList;

import com.viveka01.format.ContentType;
import com.viveka01.format.Request;

public class HandleMiddleware{
    public static Request MiddleWareRoute(Request request){
        ContentType content = request.getContent();
        switch(content){
            case FORM:
                WebFormParser parser = new WebFormParser("--"+request.getBoundary(),request.getBody());
                request.setElements(parser.getElements());
                break;
            //TODO: JSON handlers
        }
        return request;
    }
}

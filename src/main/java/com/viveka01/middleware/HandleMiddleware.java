package com.viveka01.middleware;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viveka01.format.ContentType;
import com.viveka01.format.Request;
import com.viveka01.format.Response;
import com.viveka01.format.json.JsonHandler;
import com.viveka01.logic.ServerError;

public class HandleMiddleware{
    public static Request MiddleWareRoute(Request request){
        request.setBlock(!Cors.corsInspect(request.getOrigin(),request.getHost()));
        if (request.isBlocked()) return request;
        ContentType content = request.getContent();
        switch(content){
            case FORM:
                WebFormParser parser = new WebFormParser("--"+request.getBoundary(),request.getBody());
                request.setElements(parser.getElements());
                break;
            case JSON:
                String jsonString = new String(request.getBody(), StandardCharsets.UTF_8);
                try {
                    JsonHandler jsonBody = new JsonHandler(jsonString);
                    String schema = jsonBody.getValue("schema",String.class).toString();
                    jsonBody.setClass(JsonClassMapper.getClass(schema));
                    request.setJsonHandler(jsonBody);
                } catch (JsonProcessingException e) {
                    request.setError(true);
                }

        }
        return request;
    }
}

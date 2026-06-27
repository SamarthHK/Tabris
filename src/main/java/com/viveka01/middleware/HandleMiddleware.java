package com.viveka01.middleware;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viveka01.format.http.ContentType;
import com.viveka01.format.http.Request;
import com.viveka01.format.json.JsonHandler;

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

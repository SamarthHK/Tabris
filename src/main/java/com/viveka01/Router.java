package com.viveka01;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.viveka01.format.*;
import com.viveka01.logic.*;


public class Router {
    //TODO: make it so paths can take in some sort of regex, create a class to read and parse the regex
    //TODO: Make it so handler takes both URL and request so it can do its own parsing
    private static Map<String, Map<Method, RouteHandler>> route = new HashMap<>();
    private static final String frontEndDir = "src\\main\\resources\\static";
    static{
        addRoute("/",Method.GET,StaticFileHandler::getFrontEndPage);
        addRoute("/staticFile",Method.GET,StaticFileHandler::getFrontEndPage);
        addRoute("/cors",Method.OPTIONS,CorsAccept::handleCors);
        addRoute("/upload", Method.POST, ImageReciever::storeImage);
    }
    /**
     * @param path url of path
     * @param code HTTP method
     * @param method the method referenced that will be called. Request is passed into it
     */
    public static void addRoute(String path, Method code, RouteHandler method) {
        Map<Method, RouteHandler> temp = new Hashtable<>();
        temp.put(code, method);
        route.put(path, temp);
    }
    /**
     * @param request takes request object (that contains CORS header values)
     * Meant for CORS response only, doesnt work with anything else (Or it wont work as intended)
     */
    public static Response createResponse(Request request) throws IOException {
        String path = request.getPath();
        Method code = request.getMethod();
        path = getCors(code, path);
        path = getFile(path);
        try {
            RouteHandler handler = route.get(path).get(code);
            return handler.handle(request);
        } catch (Exception e) {
            System.out.println("Yea error....");
            System.out.printf("path: %s,code: %s\n",path,code.toString());
            e.printStackTrace();
        }
        return Response.SERVER_ERROR;
    }
    private static String getCors(Method method, String path){
        if (method == Method.OPTIONS){
            return "/cors";
        }
        return path;
    }
    /**
     * @param path url in string form
     * returning /staticFile route or the path itself
     */
    private static String getFile(String path){
        String[] sections = path.split("/");
        int index = sections.length-1;
        if (index == -1){
            return path;
        }
        if (sections[index].contains(".")){
            return "/staticFile";
        }
        return path;
    }
    //TODO: Learn Interface indepth
    @FunctionalInterface
    public interface RouteHandler{
        Response handle(Request request) throws Exception;
    }
}

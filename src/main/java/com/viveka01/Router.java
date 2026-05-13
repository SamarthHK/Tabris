package com.viveka01;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.viveka01.format.*;
import com.viveka01.logic.*;


public class Router {
    private static Map<String, Map<Method, RouteHandler>> route = new HashMap<>();
    private static final String frontEndDir = "src\\main\\resources\\static";
    static{
        addRoute("/staticFile",Method.GET,StaticFileHandler::getFrontEndPage);
        addRoute("/cors",Method.OPTIONS,CorsAccept::handleCors);
        addRoute("/upload", Method.POST, ImageReciever::storeImage);
    }
    public static void addRoute(String path, Method code, RouteHandler method) {
        Map<Method, RouteHandler> temp = new Hashtable<>();
        temp.put(code, method);
        route.put(path, temp);
    }
    public static HttpFormat.Response createResponse(HttpFormat.Request request) throws IOException {
        request.printRequestParams();
        String path = request.getPath();
        Method code = request.getMethod();
        path = getCors(code, path);
        path = getFile(path);
        try {
            System.out.println("Content: "+request.getContent());
            RouteHandler handler = route.get(path).get(code);
            return handler.handle(request);
        } catch (Exception e) {
            System.out.println("Yea error....");
            System.out.printf("path: %s,code: %s\n",path,code.toString());
            e.printStackTrace();
        }
        return HttpFormat.Response.SERVER_ERROR;
    }
    private static String getCors(Method method, String path){
        if (method == Method.OPTIONS){
            return "/cors";
        }
        return path;
    }
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
        HttpFormat.Response handle(HttpFormat.Request request) throws Exception;
    }
}

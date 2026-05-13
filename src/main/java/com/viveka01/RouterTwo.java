package com.viveka01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.viveka01.format.*;
import com.viveka01.format.HttpFormat.Response;
import com.viveka01.logic.staticFileHandler;

import java.lang.NullPointerException;

public class RouterTwo {
    private static Map<String, Map<Method, RouteHandler>> route = new HashMap<>();
    private static final String frontEndDir = "src\\main\\resources\\static";
    static{
        addRoute("/staticFile",Method.GET,staticFileHandler::getFrontEndPage);
    }
    public static void addRoute(String path, Method code, RouteHandler method) {
        Map<Method, RouteHandler> temp = new Hashtable<>();
        temp.put(code, method);
        route.put(path, temp);
    }
    public static HttpFormat.Response createResponse(HttpFormat.Request request) throws IOException {
        String path = request.getPath();
        Method code = request.getMethod();
        path = getFile(path);
        System.out.println(path);
        System.out.println(code.name());
        System.out.println("Inside method??");
        try {
            RouteHandler handler = route.get(path).get(code);
            System.out.println("Created handler?");
            return handler.handle(request);
        } catch (Exception e) {
            System.out.println("Yea error....");
        }
        return HttpFormat.Response.SERVER_ERROR;
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

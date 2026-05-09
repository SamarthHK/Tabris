package com.viveka01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.util.Dictionary;
import java.util.Hashtable;
import com.viveka01.format.*;
import com.viveka01.format.HttpFormat.Response;
import java.lang.NullPointerException;

public class Router {
    private static Dictionary<String, Dictionary<Method, Object>> route = new Hashtable<>();
    private static final String frontEndDir = "src\\main\\resources\\static";
    static {
        addRoute("/", Method.GET, "src\\main\\resources\\static\\premain.html");
        // addRoute("/upload", Method.POST, new FileMapper("ImageReciever","StoreFile"));
    }
    
    /**
     * @param Takes client path, http code, and server file path
     */
    public static void addRoute(String path, Method code, String FilePath) {
        Dictionary<Method, Object> temp = new Hashtable<>();
        temp.put(code, FilePath);
        route.put(path, temp);
    }
    public static void addRoute(String path, Method code, FileMapper obj) {
        Dictionary<Method, Object> temp = new Hashtable<>();
        temp.put(code, obj);
        route.put(path, temp);
    }
    /**
     * @param Takes client path and http code
     * @return Returns response object
     */
    public static HttpFormat.Response createResponse(HttpFormat.Request request) throws IOException {
        String path = request.getPath();
        Method code = request.getMethod();
        switch (code) {
            case GET:
                return getResponse(path);
            // case POST:
            //     return postResponse(request);
            default:
                return new Response(500, "Unsupported response type");
        }
    }

    private static void postResponse(HttpFormat.Request request){
        System.out.println();
        //TODO
    }

    /**
     * @param Takes client path and http code
     * @return Returns response object
     */
    private static HttpFormat.Response getResponse(String path) throws IOException {
        String filePath;
        try {
            filePath = (String) route.get(path).get(Method.GET);
        } catch (NullPointerException e) {
            try {
                filePath = Paths.get(frontEndDir, path).toString();
            } catch (InvalidPathException er) {
                filePath = "src\\main\\resources\\static\\fileNotFound.html";
            }

        }
        System.out.printf("Retrieving file: %s\n", filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            return new Response(404, "File Not Found");
        }
        String fileType = filePath.split("\\.")[1].toUpperCase();
        return new Response(200, ContentType.valueOf(fileType), getFileBytes(filePath));
    }

    private static byte[] getFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream readFile = new FileInputStream(file);
        byte[] body = new byte[readFile.available()];
        readFile.read(body);
        readFile.close();
        return body;
    }

}

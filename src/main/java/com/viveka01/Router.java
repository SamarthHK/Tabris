package com.viveka01;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.Hashtable;
import com.viveka01.format.*;
import com.viveka01.format.HttpFormat.Response;
import java.lang.NullPointerException;

public class Router {
    private static Dictionary<String,Dictionary<Method,String>> route = new Hashtable<>();
    private static final String frontEndDir = "src\\main\\resources\\static";
    static{
        addRoute("/",Method.GET,"src\\main\\resources\\static\\premain.html");
    }
    /**
     * @param Takes client path, http code, and server file path
     */
    public static void addRoute(String path,Method code,String FilePath){
        Dictionary<Method,String> temp = new Hashtable<>();
        temp.put(code,FilePath);
        route.put(path,temp);
    }

    private static byte[] getFileBytes(String filePath) throws IOException{
        File file = new File(filePath);
        FileInputStream readFile = new FileInputStream(file);
        byte[] body = new byte[readFile.available()];
        readFile.read(body);
        readFile.close();
        return body;
    }

    /**
     * @param Takes client path and http code
     * @return Returns response object
     */
    public static HttpFormat.Response createResponse(String path,Method code) throws IOException{
        switch (code) {
            case GET:
                return getResponse(path);        
            default:
                return new Response(500,"Unsupported response type");
        }
    }
    /**
     * @param Takes client path and http code
     * @return Returns response object
     */
    private static HttpFormat.Response getResponse(String path) throws IOException{
        String filePath;
        try{
            filePath = route.get(path).get(Method.GET);
        }catch (NullPointerException e){
            filePath = Paths.get(frontEndDir,path).toString();
        }
        System.out.printf("Retrieving file: %s\n",filePath);
        File file = new File(filePath);
        if(!file.exists()){
            return new Response(404,"File Not Found");
        }
        String fileType = filePath.split("\\.")[1].toUpperCase();
        return new Response(200, ContentType.valueOf(fileType), getFileBytes(filePath));
    }
    
}

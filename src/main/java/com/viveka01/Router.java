package com.viveka01;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Hashtable;
import com.viveka01.format.*;
import com.viveka01.format.HttpFormat.Response;
import java.lang.NullPointerException;

public class Router {
    private static Dictionary<String,Dictionary<Method,String>> route = new Hashtable<>();
    static{
        addRoute("/",Method.GET,"src/main/frontEnd/Remixxer-Art-Demo-Front-End/mainRemixxerPage.html");
        addRoute("/favicon.ico",Method.GET,"src\\main\\frontEnd\\Remixxer-Art-Demo-Front-End\\kaoru.png");
    }
    /**
     * @param Takes client path, http code, and server file path
     */
    public static void addRoute(String path,Method code,String FilePath){
        Dictionary<Method,String> temp = new Hashtable<>();
        temp.put(code,FilePath);
        route.put(path,temp);
    }
    /**
     * @param Takes client path and http code
     * @return Returns response object
     */
    public static HttpFormat.Response getResponse(String path,Method code) throws IOException{
        String filePath = "";
        try{
            filePath = route.get(path).get(code);
        }catch (NullPointerException e){
            return new Response(404, ContentType.PLAIN, "File Not Found".getBytes(StandardCharsets.UTF_8));
        }
        
        System.out.println(filePath);
        File file = new File(filePath);
        if(!file.exists()){
            return new Response(500,ContentType.PLAIN,"File Not Found".getBytes(StandardCharsets.UTF_8));
        }
        String fileType = filePath.split("\\.")[1].toUpperCase();

        FileInputStream readFile = new FileInputStream(file);
        byte[] body = new byte[readFile.available()];
        readFile.read(body);
        readFile.close();

        return new Response(200, ContentType.valueOf(fileType), body);
    }
}

package com.viveka01.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import com.viveka01.Router;
import com.viveka01.format.*;

public class ImageReciever {
    static final String root = "src\\main\\java\\com\\viveka01\\database\\images";
    static HashMap<ContentType,Integer> imageCount = new HashMap<>();
    static HashMap<String,String> codeToPath = new HashMap<>();
    static final int namingSize = 3;
    static int imgNumber = 0;
    static{
        imageCount.put(ContentType.PNG,0);
        imageCount.put(ContentType.JPEG,0);
        imageCount.put(ContentType.GIF,0);
        imageCount.put(ContentType.WEBP,0);
    }
    /**
     * @param request request object containing CORS params and no body
     */
    static public Response storeImage(Request request){
        String outputPath = storeImage(request.getContent(),request.getBody());
        return new Response(200, request.getHost() + outputPath);
    }
    /**
     * @param format takes type of content in ContentType enum
     * @param imageByte whole image that should be stored in byte array
     */
    static public String storeImage(ContentType format,byte[] imageByte){
        System.out.println(format.toString());
        Integer imageNumber = imageCount.get(format);
        String name = getName(imageNumber)+"."+format.toString();
        String path = root+"\\"+name;
        imageCount.put(format,++imageNumber);
        File image = new File(path);
        if(image.exists()){
            image.delete();
        }
        try {
            image.createNewFile();
            FileOutputStream out = new FileOutputStream(image);
            out.write(imageByte);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputPath = "/"+getName(imgNumber,namingSize+1); 
        Router.addRoute(outputPath, Method.GET,ImageReciever::getImage);
        codeToPath.put(outputPath,path);
        imgNumber++;
        return outputPath;
    }
    /**
     * @param request request object containing url for code/ name of required file in db
     */
    public static Response getImage(Request request){
        String path = request.getPath();
        try{
            path = codeToPath.get(path);
        }catch (Exception e){
            return Response.SERVER_ERROR;
        }
        String temp = path.substring(path.lastIndexOf(".")+1);
        ContentType imgType = ContentType.valueOf(temp);
        byte [] image;
        try {
            image = StaticFileHandler.getFileBytes(path);
        } catch (IOException e) {
            System.out.println("Tried to acsess file: "+path);
            return Response.SERVER_ERROR;
        }
        return new Response(200,imgType, image);
    } 

    static private String getName(int number,int namingSize){
        String output = String.valueOf(number);
        String space = "";
        for(int i = namingSize; i != 0;i--){
            if (i == output.length()){
                return space + output;
            }
            space += "0";
        }
        return output;
    }
    static private String getName(int number){
        String output = String.valueOf(number);
        String space = "";
        for(int i = namingSize; i != 0;i--){
            if (i == output.length()){
                return space + output;
            }
            space += "0";
        }
        return output;
    }
}

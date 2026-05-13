package com.viveka01.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashMap.*;

import com.viveka01.RouterOld;
import com.viveka01.format.*;

public class ImageReciever {
    static final String root = "src\\main\\java\\com\\viveka01\\database\\images";
    static HashMap<ContentType,Integer> imageCount = new HashMap<>();
    static final int namingSize = 3;
    static int imgNumber = 0;
    static{
        imageCount.put(ContentType.PNG,0);
        imageCount.put(ContentType.JPEG,0);
        imageCount.put(ContentType.GIF,0);
        imageCount.put(ContentType.WEBP,0);
    }

    static public String storeImage(ContentType format,byte[] imageByte){
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
        RouterOld.addRoute(outputPath, Method.GET,path);
        imgNumber++;
        return outputPath;
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

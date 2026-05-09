package com.viveka01.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashMap.*;
import com.viveka01.format.*;

public class ImageReciever {
    static final String root = "src\\main\\java\\com\\viveka01\\database\\images";
    static HashMap<ContentType,Integer> imageCount = new HashMap<>();
    static final int namingSize = 3;
    static{
        imageCount.put(ContentType.PNG,0);
        imageCount.put(ContentType.JPEG,0);
        imageCount.put(ContentType.GIF,0);
        imageCount.put(ContentType.WEBP,0);
    }

    static public void storeImage(ContentType format,byte[] imageByte){
        Integer imageNumber = imageCount.get(format);
        String name = getName(imageNumber)+"."+format.toString();
        imageCount.put(format,++imageNumber);
        File image = new File(root+"\\"+name);
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

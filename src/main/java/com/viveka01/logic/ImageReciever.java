package com.viveka01.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.viveka01.format.ContentType;

public class ImageReciever {
    static final String root = "src\\main\\java\\com\\viveka01\\database\\images";
    static int imageNumber = 0;

    static public void storeImage(ContentType format,byte[] imageByte){
        String name = getName(imageNumber)+"."+format.toString();
        imageNumber ++;
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
        if (output.length() == 1){
            return "00" + output;
        }
        if (output.length() == 2){
            return "0" +output;
        }
        return output;
    }
}

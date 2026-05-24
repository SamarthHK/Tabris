package com.viveka01.logic;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.viveka01.format.ContentDisposition;
import com.viveka01.format.ContentType;
import com.viveka01.format.Request;
import com.viveka01.format.Response;
import com.viveka01.middleware.Element;
//TODO: Create single and bulk handling to store files
public class ImageRecieverTwo {
    static final String[] winRoot = "src\\main\\resources\\database\\images".split("\\\\");
    static String sysRoot; 
    static HashMap<Integer,String> nameToNumber = new HashMap<>();
    static final int namingSize = 3;
    static final AtomicInteger imgNumber = new AtomicInteger(getInitNumber());
    static{
        Path path = Paths.get(winRoot[0],Arrays.copyOfRange(winRoot, 1, winRoot.length));
        sysRoot = path.toString();
        System.out.println(sysRoot);
    }
    // static public Response storeImage(Request request){

    // }

    static public void singleFileHandle(String fileName,byte[] body){
        String storeName = getName(imgNumber.addAndGet(1), namingSize);
        Path path = Paths.get(sysRoot,storeName);
        
        // Response response = new Response(200, "Dihh");
        // response.setContentDisposition(ContentDisposition.INLINE, fileName);
        // return response;
    }

    static private void bulkFileHandle(ArrayList<Element> element){
        
    }

    static private int getInitNumber(){
        File dir = new File(sysRoot);
        File[] files = dir.listFiles();
        int biggestNum = 0;
        for (File file: files){
            String fileName = file.getName();
            System.out.println(fileName);
            int num = 0;
            try{
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
                num = Integer.valueOf(fileName);
            }catch (Exception e){
                num = 0;
            }
            if (biggestNum < num){
                biggestNum = num;
            }
        }
        return biggestNum;
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
}

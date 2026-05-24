package com.viveka01.logic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.viveka01.format.ContentDisposition;
import com.viveka01.format.ContentType;
import com.viveka01.format.Request;
import com.viveka01.format.Response;
import com.viveka01.middleware.Element;
//TODO: Create single and bulk handling to store files
public class ImageRecieverTwo {
    static final String[] root = "src\\main\\resources\\database\\images".split("\\\\");
    static HashMap<Integer,String> nameToNumber = new HashMap<>();
    static final int namingSize = 3;
    static int imgNumber = 0;

    // static public Response storeImage(Request request){

    // }

    static public Response singleFileHandle(String fileName,byte[] body){
        String[] location = new String[root.length+1];
        System.arraycopy(root, 0, location,0, root.length);
        location[root.length] = fileName;
        Path path = Paths.get(location[0],Arrays.copyOfRange(location, 1, root.length+1));

        Response response = new Response(200, "Dihh");
        response.setContentDisposition(ContentDisposition.INLINE, fileName);
        return response;
    }

    static private void bulkFileHandle(ArrayList<Element> element){
        
    }

    // static public int getImgNumber(){
    // }
}

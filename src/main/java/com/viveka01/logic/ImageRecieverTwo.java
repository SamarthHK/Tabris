package com.viveka01.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

import com.viveka01.format.ContentType;
import com.viveka01.format.Request;
import com.viveka01.format.Response;
import com.viveka01.middleware.Element;

public class ImageRecieverTwo {
    static final String[] winRoot = "src\\main\\resources\\database\\images".split("\\\\");
    static String sysRoot; 
    static HashMap<Integer,String> nameToNumber = new HashMap<>();
    static final Random random = new Random();
    static final int idSize = 10;
    static final String extension = ".raw";
    static{
        Path path = Paths.get(winRoot[0],Arrays.copyOfRange(winRoot, 1, winRoot.length));
        sysRoot = path.toString();
        System.out.println(sysRoot);
    }
    static public Response storeImage(Request request){
        if (request.getContent() == ContentType.FORM){
            String[] files = bulkFileHandle(request.getElements());
        }
        else
        {
            String file = singleFileHandle(extension, null)
        }
    }

    static public String singleFileHandle(String fileName,byte[] body){
        byte[] code = new byte[idSize];
        random.nextBytes(code);
        String storeName = Base64.getUrlEncoder().withoutPadding().encodeToString(code)+extension;
        Path path = Paths.get(sysRoot,storeName);

        byte[] fileNameByte = fileName.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(4 + fileNameByte.length + body.length);

        buffer.putInt(fileNameByte.length);
        buffer.put(fileNameByte);
        buffer.put(body);

        byte[] formatBody = buffer.array();
        try {
            System.out.write(formatBody);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(path.toString());
        try(FileOutputStream write = new FileOutputStream(file)){
            write.write(formatBody);
            System.out.println("Saved file!!!");
        }catch (IOException e){
            e.printStackTrace();
        }

        return storeName;
    }

    static private String[] bulkFileHandle(ArrayList<Element> elements){
        ArrayList<String> files = new ArrayList<>();
        for(Element element: elements){
            if(element.getFileName() == null) continue;
            files.add(singleFileHandle(element.getFileName(),element.getBody()));
        }
        return (String[]) files.toArray();
    }
}

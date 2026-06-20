package com.viveka01.logic;

import com.viveka01.Main;
import com.viveka01.config.PropReader;
import com.viveka01.format.ContentType;
import com.viveka01.format.FilePathHandler;
import com.viveka01.format.Request;
import com.viveka01.format.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ImageReceiver {
    static final Logger LOGGER = LoggerFactory.getLogger(ImageReceiver.class);

    static final String root = FilePathHandler.getAbsolutePath(PropReader.getInstance().getProperty("image.image-storage-location"));
    static ConcurrentHashMap<String,String> idToExtension = new ConcurrentHashMap<>();
    static final int namingSize = Integer.parseInt(PropReader.getInstance().getProperty("image.naming-size"));
    static{
        loadImages();
        LOGGER.info("Loaded all images into hashmap");
    }

    /**
     * Loads all images from root directory of images into hashmap
     */
    static private void loadImages(){
        File folder = new File(root);
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file: files){
           String fileName = file.getName();
           String id = fileName.substring(0,fileName.indexOf("."));
           String ext = fileName.substring(fileName.indexOf(".")+1);
           LOGGER.info("loaded: {} {}",id,ext);
           idToExtension.put(id,ext);
        }
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
    static public String storeImage(ContentType format, byte[] imageByte){
        File image;
        String name;
        String path;
        String id;
        do{
            id = UUID.randomUUID().toString().substring(0,namingSize);
            name = id+"."+format.toString();
            path = FilePathHandler.getAbsolutePath(root+"|"+name);
            image = new File(path);
        }while (image.exists());
        try {
            image.createNewFile();
            FileOutputStream out = new FileOutputStream(image);
            out.write(imageByte);
            out.close();
        } catch (IOException e) {
            LOGGER.error("Caught an exception creating files in storeImage",e);
        }
        String outputPath = "/viewImage/"+id;
        LOGGER.info("added: {} {}",id,format.toString());
        idToExtension.put(id,format.toString());
        return outputPath;
    }

    /**
     * @param request request object containing url for code/ name of required file in db
     */
    static public Response getImage(Request request){
        String path = request.getPath();
        LOGGER.info("Retriving image: {}",path);
        String id = path.substring(path.lastIndexOf("/")).replace("/","");
        String ext = idToExtension.get(id);
        ContentType imgType = ContentType.valueOf(ext);
        path = root + "|" + id + "." + ext;
        path = FilePathHandler.getAbsolutePath(path);
        byte [] image;
        try {
            image = StaticFileHandler.getFileBytes(path);
        } catch (IOException e) {
            LOGGER.error("Tried to acsess file: {}",path,e);
            return Response.SERVER_ERROR;
        }
        return new Response(200,imgType, image);
    }
}

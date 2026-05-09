package com.viveka01.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import com.viveka01.format.ContentType;
import com.viveka01.format.HttpFormat;
import com.viveka01.format.HttpFormat.Response;

public class staticFileHandler {
    private static final String frontEndDir = "src\\main\\resources\\static";
    private static final String fileNotFoundPath = "src\\main\\resources\\static\\fileNotFound.html";

    public static HttpFormat.Response getFrontEndPage(HttpFormat.Request request) throws IOException, FileNotFoundException{
        String path = request.getPath();
        String filePath;
        try{
            filePath = Paths.get(frontEndDir,path).toString();
        }catch (InvalidPathException e){
            return HttpFormat.Response.SERVER_ERROR;
        }

        System.out.printf("Retrieving file: %s\n", filePath);
        File file = new File(filePath);

        if (!file.exists()) {
            filePath = fileNotFoundPath;
            file = new File(filePath);
        }

        String fileType = filePath.split("\\.")[1].toUpperCase();
        try {
            return new Response(200, ContentType.valueOf(fileType), getFileBytes(filePath));
        } catch (IOException e) {
            return HttpFormat.Response.SERVER_ERROR;
        }
    }

    private static byte[] getFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream readFile = new FileInputStream(file);
        byte[] body = new byte[readFile.available()];
        readFile.read(body);
        readFile.close();
        return body;
    }
}

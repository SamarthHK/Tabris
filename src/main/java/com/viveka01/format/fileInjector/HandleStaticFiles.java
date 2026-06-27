package com.viveka01.format.fileInjector;

import com.viveka01.config.PropReader;
import com.viveka01.format.Initialize;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class HandleStaticFiles {
    private static final String src = PropReader.getInstance().getProperty("static-file.storage-location");
    private final byte[] info;

    public HandleStaticFiles(String fileName) throws IOException{
        File file = new File(String.valueOf(Paths.get(src,fileName)));
        try(FileInputStream read = new FileInputStream(file)){
            this.info =  read.readAllBytes();
        }
    }

    public byte[] getFileByteArray(){
        return info;
    }
    public String getFileString(){
        return new String(info, StandardCharsets.UTF_8);
    }
}

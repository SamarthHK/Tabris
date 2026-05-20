package com.viveka01.middleware;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.viveka01.format.ContentType;

public class Element {
    String contentDisposition;
    String name;
    String fileName;
    ContentType content = ContentType.PLAIN;

    public Element(byte[] header){
        String stringHeader = new String(header,StandardCharsets.UTF_8);
        String[] splitLines = stringHeader.split("\\r\\n");
        String[] dispositionLine;
        String contentLine;

        dispositionLine = splitLines[0].split(";");

        for(String part:dispositionLine){
            System.out.printf("Part: %s",part);
            part = part.toLowerCase().strip();
            if (part.startsWith("content-disposition")){
                contentDisposition = part.substring(part.lastIndexOf(":")+1).strip();
            }
            else if (part.startsWith("name")){
                contentDisposition = part.substring(part.lastIndexOf("=")+1).strip();
            }
            else if (part.startsWith("filename")){
                contentDisposition = part.substring(part.lastIndexOf("=")+1).strip();
            }
        }

        if(splitLines.length != 1){
            contentLine = splitLines[0];
            this.content = ContentType.stringToContentType(
                contentLine.substring(contentLine.lastIndexOf(":")+1)
                .strip()); 
        }
    }

    public void printAllValues(){
        System.out.printf("Content-Disposition: %s\nName: %s\nFile Name: %s\nContent-Type: %s\n",contentDisposition,name,fileName,content);
    }

}

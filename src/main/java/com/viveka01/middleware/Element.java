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
            String compare = part.toLowerCase().strip();
            System.out.printf("Part: %s\n",compare);

            if (compare.startsWith("content-disposition")){
                contentDisposition = part.substring(part.lastIndexOf(":")+1).strip();
            }
            else if (compare.startsWith("name")){
                name = part.substring(part.lastIndexOf("=")+1)
                           .replace("\""," ")
                           .strip();
            }
            else if (compare.startsWith("filename")){
                fileName = part.substring(part.lastIndexOf("=")+1)
                               .replace("\""," ")
                               .strip();
            }
        }
        System.out.println();

        if(splitLines.length >= 2){
            contentLine = splitLines[1];
            this.content = ContentType.stringToContentType(
                contentLine.substring(contentLine.lastIndexOf(":")+1)
                .strip()); 
        }
    }

    public void printAllValues(){
        System.out.printf("Content-Disposition: %s\nName: %s\nFile Name: %s\nContent-Type: %s\n",contentDisposition,name,fileName,content);
    }

}

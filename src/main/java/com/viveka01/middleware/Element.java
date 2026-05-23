package com.viveka01.middleware;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.viveka01.format.ContentType;

public class Element {
    String contentDisposition;
    String name;
    String fileName;
    ContentType content = ContentType.PLAIN;
    int crlfPos;
    byte[] body;

    //TODO: When making full parsing, check if you include CRLF and boundary or you remove both or you keep CRLF only, important so body doesnt contain it
    /**
     * @param element byte array containing ONLY one element of the webform, element shouldnt contain ending CRLF or boundy, only headers, double CRLF and boundary
     * @exception Error throws a error message when there is not double CRLF detected, needs it to seperate body and headers
     */
    public Element(byte[] element){
        crlfPos = checkDoubleLineBreak(element);
        if (crlfPos == -1){
            throw new Error("WHERE IS THE FRIGGAN DOUBLE CRLF CHUD???");
        }    
        byte[] header = Arrays.copyOf(element, crlfPos);
        System.out.println("Full Header: ");
        try {
            System.out.write(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n");
        body = Arrays.copyOfRange(element, crlfPos+4, element.length);
        readHeader(new String(header,StandardCharsets.UTF_8));
        
    }

    /**
     * @param header String containing the header lines of the element, no body (can parse even with body)
     * Takes String of header and stores in contentDispotion, name, fileName, and content variables 
     */
    public void readHeader(String header){
        String[] splitLines = header.split("\\r\\n");
        String[] dispositionLine;
        String contentLine;

        dispositionLine = splitLines[0].split(";");

        for(String part:dispositionLine){
            String compare = part.toLowerCase().strip();
            // System.out.printf("Part: %s\n",compare);

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

    /**
     * @param read byte array of the request
     * @return returns position of CRLF, else returns -1
     */
    private int checkLineBreak(byte[] read) {
        for (int i = 0; i <= read.length - 2; i++) {
            if (read[i] == 0x0D &&  // /r
                read[i + 1] == 0x0A)// /n
                {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param read byte array of the request
     * @return returns position of double CRLF, else returns -1
     */
    private int checkDoubleLineBreak(byte[] read) {
        for (int i = 0; i <= read.length - 4; i++) {
            if (read[i] == 0x0D &&    // \r
                read[i + 1] == 0x0A &&// \n
                read[i + 2] == 0x0D &&// \r
                read[i + 3] == 0x0A) {// \n

                return i;
            }
        }
        return -1;
    }

    public void printAllValues(){
        System.out.printf("Content-Disposition: %s\nName: %s\nFile Name: %s\nContent-Type: %s\n",contentDisposition,name,fileName,content);
    }

    public byte[] getBody(){
        return body;
    }

    public String getFileName(){
        return fileName;
    }
}

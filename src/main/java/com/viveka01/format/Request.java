package com.viveka01.format;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.swing.text.AbstractDocument.Content;

public class Request {
    byte[] packet;
    byte[] header;
    byte[] body;
    //Request line
    Method method;
    String path;
    String version;
    //Header
    String host;
    ContentType content = ContentType.EMPTY;
    int contentLength = 0;
    String origin;
    String accessControlRequestMethod;
    String accessControlRequestHeaders;
    String boundary;
    //Header Checks
    int amountRead = 0;
    int lineBreakPos = 0;
    //Constants
    final int BUFFER_SIZE = 1024; 
    /**
     * @param in socket InputStream
     * Takes whole InputStream and parse the http request
     */
    public Request(InputStream in) throws IOException{
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        RingBuffer temp = new RingBuffer(BUFFER_SIZE*2);
        byte[] read = new byte[BUFFER_SIZE];
        int bytesRead;
        //Reading header in chunks
        while (true){
            bytesRead = in.read(read);
            amountRead += bytesRead;
            if (bytesRead == -1) break;

            buffer.write(read,0,bytesRead);
            temp.addData(read,bytesRead);
            //If line break detected end loop
            lineBreakPos = checkLineBreak(temp.toArray());
            if (lineBreakPos != -1){
                lineBreakPos += temp.getGlobal();
                break;
            }
        }
        //parsing header values
        this.header = buffer.toByteArray();
        parseHeader(this.header);
        //Getting how much to read
        int remainingBytes = amountRead-lineBreakPos+4;
        remainingBytes = contentLength-remainingBytes;
        //Reading in chunks till there are no more remaining bytes left
        while(remainingBytes > 0){
            bytesRead = in.read(read);
            if (bytesRead == -1) break;
            remainingBytes -= bytesRead;
            buffer.write(read,0,bytesRead);
        }
        this.packet = buffer.toByteArray();
        this.body = new byte[contentLength];
        System.arraycopy(this.packet, lineBreakPos+4, this.body, 0,contentLength);

        if (content == ContentType.FORM){
            writePacketToFile("src\\test\\java\\testPacket");
        }
    }

    //TODO: delete printing methods when done with testing
    //Created for testing will remove later
    public void printPacket(){
        printRequestParams();
        printBody();
    }

    public void printBody(){
        try{
            System.out.println("Body:");
            System.out.write(this.body);
            System.out.println();
        }
        catch (IOException e){
            System.out.println("COULDNT PRINT");
            e.printStackTrace();
        }
    }

    private void writePacketToFile(String filePath) throws IOException{
        File file = new File(filePath+"\\debug.raw");
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();
        FileOutputStream write = new FileOutputStream(file);
        write.write(packet);
        System.out.printf("Wrote %s packet to %s\n",content.toString(),filePath+"debug.raw");
    }

    public void printRequestParams(){
        System.out.printf("Method: %s, Path: %s, Version: %s \nHost: %s, Content-Type: %s, Content-Length: %d, boundary: %s, Request-Method: %s, Request-Header: %s\n",method,path,version,host,content.getContentType(),contentLength,boundary,accessControlRequestMethod,accessControlRequestHeaders);
    }

    /**
     * @param read byte array of the request
     * @return returns position of lineBreak, else returns -1
     */
    private int checkLineBreak(byte[] read) {
        for (int i = 0; i <= read.length - 4; i++) {
            if (read[i] == 0x0D &&
                read[i + 1] == 0x0A &&
                read[i + 2] == 0x0D &&
                read[i + 3] == 0x0A) {

                return i;
            }
        }
        return -1;
    }

    /**
     * @param lines takes string array of http request seperated by \r\n
     */
    private void parseHeader(String[] lines){
        getRequestLine(lines[0]);
        for(int i = 1;i != lines.length;i++){
            getHeaders(lines[i]);
        }
    }

    /**
     * @param roughBody Takes a byte[] that contains a header and potentially some body
     * parseHeader parses the header part of input and puts the header values in variables (of whatever is supported)
     */
    private void parseHeader(byte[] roughBody){
        String[] header = new String(roughBody,StandardCharsets.UTF_8).substring(0, lineBreakPos).split("\r\n");
        parseHeader(header);
    }

    /**
     * @param line takes string input of the line and gets method, path and http version of client
     */
    private void getRequestLine(String line){
        String[] parts = line.split(" ");
        method = Method.valueOf(parts[0]);
        path = parts[1];
        version = parts[2];
    }

    //TODO: Get rid of this later on.... Meant for testing only
    private Request(){
        System.out.println("Private initializer");
    }

    static public Request testRequest(){
        System.out.println("Created dummy request object for testing!!!");
        return new Request();
    }
    //TODO:TESTING getHeaders, TURN TO private void after done
    /**
     * @param line single line from http request in string format
     * Assigns host, content, and contentLenght values from line
     */
    public void getHeaders(String line){
        String[] parts = line.split(":",2);
        parts[0] = parts[0].trim().toLowerCase();

        switch(parts[0]){
            case "host":
                this.host = parts[1].strip();
                break;

            case "content-type":
                String contentType = parts[1].split(";")[0].strip().toLowerCase();
                this.content = ContentType.stringToContentType(contentType);
                
                System.out.println("Content type is:"+content.toString());
                if (content == ContentType.FORM){
                    String boundary = parts[1];
                    boundary = boundary.substring(boundary.lastIndexOf("=")+1);
                    this.boundary = boundary;
                }
                System.out.printf("The content type is: %s, and the boundary is: %s\n",content.toString(),boundary);
                break;

            case "content-length":
                this.contentLength = Integer.parseInt(parts[1].strip());
                break;

            case "origin":
                this.origin = parts[1].strip();
                break;

            case "access-control-request-method":
                this.accessControlRequestMethod = parts[1].strip();
                break;

            case "access-control-request-headers":
                this.accessControlRequestHeaders = parts[1].strip();
                break;
        }
    }
    //Getters
    public String getPath(){
        return path;
    }
    public Method getMethod(){
        return method;
    }
    public byte[] getBody(){
        return this.body;
    }
    public ContentType getContent(){
        return content;
    }
    public String getHost(){
        return host;
    }
    public String getRequestMethod(){
        return accessControlRequestMethod;
    }
    public String getRequestHeader(){
        return accessControlRequestHeaders;
    }
}

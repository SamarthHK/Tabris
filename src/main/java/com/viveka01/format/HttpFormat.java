package com.viveka01.format;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.text.AbstractDocument.Content;

import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;

public class HttpFormat {
    /**
     * Holds method, path, and version params for incoming request
     */
    public static class Request {
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
        //Header Checks
        Boolean headerFinished = false;
        int amountRead = 0;
        //body checks
        Boolean bodyFinished = false;
        //Constants
        final int BUFFER_SIZE = 32; 

        public Request(InputStream in) throws IOException{
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] read = new byte[BUFFER_SIZE];
            int lineBreakPos = 0;
            RingBuffer temp = new RingBuffer(BUFFER_SIZE*2);
            while (true){
                int bytesRead = in.read(read);
                amountRead += bytesRead;
                if (bytesRead == -1) break;

                buffer.write(read,0,bytesRead);
                temp.addData(read,bytesRead);

                lineBreakPos = checkLineBreak(temp.toArray());
                if (lineBreakPos != -1){
                    System.out.println(temp.getGlobal());
                    lineBreakPos += temp.getGlobal();
                    
                    break;
                }
            }
            String[] header = buffer.toString(StandardCharsets.UTF_8).substring(0, lineBreakPos).split("\r\n");
            parseHeader(header);
            System.out.printf("contentLength: %d\nlineBreakPos: %d\namountRead: %d\n",contentLength,lineBreakPos,amountRead);
            System.out.printf("Length of array: %d\n",buffer.toByteArray().length);
        }

        public void printRequestParams(){
            System.out.printf("Method: %s, Path: %s, Version: %s \nHost: %s, Content-Type: %s, Content-Length: %d\n",method,path,version,host,content.getContentType(),contentLength);
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
         * @param line takes string input of the line and gets method, path and http version of client
         */
        private void getRequestLine(String line){
            String[] parts = line.split(" ");
            method = Method.valueOf(parts[0]);
            path = parts[1];
            version = parts[2];
        }
        /**
         * @param line single line from http request in string format
         * Assigns host, content, and contentLenght values from line
         */
        private void getHeaders(String line){
            String[] parts = line.split(":",2);
            parts[0] = parts[0].trim().toLowerCase();

            switch(parts[0]){
                case "host":
                    this.host = parts[1];
                    break;
                case "content-type":
                    this.content = ContentType.stringToContentType(parts[1]);
                    break;
                case "content-length":
                    this.contentLength = Integer.parseInt(parts[1].strip());
                    break;
            }
        }
    }


    public static class Response{
        //Status Line
        final String VERSION = "1.1";
        int statusCode;
        String reasonPhrase;
        //Headers
        ContentType contentType;
        int contentLength;
        ConnectionCodes connection;
        String date;
        final String SERVER = "Munna-01";
        //Body
        byte[] body;
        /**
         * @param statusCode http response code
         * @param contentType Type of content
         * @param body body of response in byte[]
         */
        public Response(int statusCode, ContentType contentType, byte[] body){
            this.statusCode = statusCode;
            this.contentType = contentType;
            this.contentLength = body.length;
            this.connection = ConnectionCodes.CLOSE;
            this.body = body;

            switch(statusCode){
                case 200:
                    reasonPhrase = "OK";
                    break;
                case 404:
                    reasonPhrase = "Not Found";
                    break;
                case 500:
                    reasonPhrase = "Internal Server Error"; 
            }
        }
        /**
         * @param statusCode http response code
         * @param message body of response, PLAIN content
         */
        public Response(int statusCode,String message){
            this.statusCode = statusCode;
            this.contentType = ContentType.PLAIN;
            this.connection = ConnectionCodes.CLOSE;
            this.body = message.getBytes(StandardCharsets.UTF_8);
            this.contentLength = body.length;

            switch(statusCode){
                case 200:
                    reasonPhrase = "OK";
                    break;
                case 404:
                    reasonPhrase = "Not Found";
                    break;
                case 500:
                    reasonPhrase = "Internal Server Error";
            }
        }
        /**
         * @return gives whole byte array response
         */
        public byte[] getResponse(){
            this.date = ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME);

            String strHeader = "HTTP/" + VERSION + " " + statusCode + " " + reasonPhrase + "\r\n" +
                             "Content-Type: " + contentType.getContentType() + "\r\n" + 
                             "Content-Length: " + contentLength + "\r\n" + 
                             connection.getLine() + "\r\n" + 
                             "Date: " + date + "\r\n" + 
                             "Server: " + SERVER + "\r\n" + 
                             "\r\n";
            byte[] byteHeader = strHeader.getBytes(StandardCharsets.UTF_8);
            byte[] response = new byte[byteHeader.length + contentLength];
            System.arraycopy(byteHeader,0,response,0,byteHeader.length);
            System.arraycopy(body,0,response,byteHeader.length,contentLength);
            return response;
        }
    }
}

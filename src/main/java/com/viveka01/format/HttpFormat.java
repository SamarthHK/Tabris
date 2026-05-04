package com.viveka01.format;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.text.AbstractDocument.Content;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;

public class HttpFormat {
    /**
     * Holds method, path, and version params for incoming request
     */
    public static class Request {
        //Request line
        Method method;
        String path;
        String version;
        //Header
        String host;
        ContentType content;
        int contentLength = 0;
        //Header Checks
        Boolean headerFinished = false;
        int headerEnding;
        //body
        byte[] body;
        /**
         * @param Takes whole http packet
         */
        public Request(byte[] packet) {
            if (headerFinished){
                return;
            }
            String request = new String(packet, StandardCharsets.UTF_8);
            String[] part = request.split("\r\n");
            assignValues(part);
            if(request.contains("\r\n\r\n")){
                headerFinished = true;
                headerEnding = request.indexOf("\r\n\r\n");
            }
        }

        private void assignValues(String[] part){
            assignRequestValues(part[0]);
            for(int i = 1; i != part.length; i++){
                assignHeaderValues(part[i]);
            }
        }

        private void assignHeaderValues(String part){
            String[] header = part.split(": ");
            switch(header[0].toLowerCase()){
                case "host":
                    host = header[1];
                    break;
                case "content":
                    content = ContentType.valueOf(header[1]);
                    break;
                case "content-length":
                    contentLength = Integer.parseInt(header[1]);
            }

        }

        private void assignRequestValues(String line) {
            String[] words = line.split(" ");
            method = Method.valueOf(words[0]);
            path = words[1];
            version = words[2].split("/")[1];
        }

        public Method getMethod() {
            return method;
        }

        public String getPath() {
            return path;
        }

        public String getVersion() {
            return version;
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

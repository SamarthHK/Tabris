package com.viveka01.format;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.text.AbstractDocument.Content;
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
        ContentType content;
        int contentLength = 0;
        //Header Checks
        Boolean headerFinished = false;
        int headerEnding;
        //body checks
        Boolean bodyFinished = false;
        
        public Request(InputStream request){
            try {
                getHeaderInfo(request);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void getHeaderInfo(InputStream in) throws IOException{
            final int BUFFERSIZE = 1024;
            byte[] temp = new byte[BUFFERSIZE];
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int headerEndPos = 0;

            while(true){
                int bytesRead = in.read(temp,0, BUFFERSIZE);
                if (bytesRead == -1){
                    break;
                }
                buffer.write(temp, 0, bytesRead);
                headerEndPos = checkLineBreak(buffer);
                if (headerEndPos != -1){
                    headerEnding = headerEndPos;
                    break;
                }
            }
            this.packet = buffer.toByteArray();
            this.header = Arrays.copyOfRange(packet, 0, headerEndPos);
            this.body = Arrays.copyOfRange(packet, headerEndPos+4,packet.length);
            storeHeaderValues(header);
        }

        private static int checkLineBreak(ByteArrayOutputStream in){
            byte[] inByteArray = in.toByteArray();
            byte[] lineBreak = { 0x0D,0x0A,0x0D,0x0A};
            byte[] temp = new byte[4];
            for(int i = 0; i != inByteArray.length-3;i++){
                temp = Arrays.copyOfRange(inByteArray,i,i+4);
                if (Arrays.equals(lineBreak,temp)){
                    return i;
                }
            }
            return -1;
        }
        
        private void assignRequestValues(String line) {
            String[] words = line.split(" ");
            method = Method.valueOf(words[0]);
            path = words[1];
            version = words[2].split("/")[1];
        }

        private void assignHeaderValues(String line){
            String[] parts = line.split(":");
            parts[0] = parts[0].trim();
            parts[1] = parts[1].trim();
            switch(parts[0].toLowerCase()){
                case "host":
                    host = parts[1];
                    break;
                case "content":
                    content = ContentType.valueOf(parts[1]);
                    break;
                case "content-length":
                    contentLength = Integer.parseInt(parts[1]);
                    break;
            }
        }

        private void storeHeaderValues(byte[] headerRaw){
            String header = new String(headerRaw,StandardCharsets.UTF_8);
            String[] parts = header.split("\r\n");
            assignRequestValues(parts[0]);
            for(int i = 1; i != parts.length;i++){
                assignHeaderValues(parts[i]);
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

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
        ContentType content;
        int contentLength = 0;
        //Header Checks
        Boolean headerFinished = false;
        int headerEnding;
        //body checks
        Boolean bodyFinished = false;
        //Constants
        final int BUFFER_SIZE = 1024; 
        public Request(InputStream in) throws IOException{
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] read = new byte[BUFFER_SIZE];
            int lineBreakPos;

            while (true){
                int bytesRead = in.read(read);
                if (bytesRead == -1) break;

                buffer.write(read,0,bytesRead);
                RingBuffer temp = new RingBuffer(BUFFER_SIZE*2);
                temp.addData(read);

                lineBreakPos = checkLineBreak(temp.toArray());
                if (lineBreakPos != -1){
                    break;
                }
            }
            System.out.println(buffer.toString(StandardCharsets.UTF_8));
        }
        private int checkLineBreak(byte[] read) {
            byte[] lineBreak = {0x0D, 0x0A, 0x0D, 0x0A};
            int lineBreakLength = lineBreak.length;

            for (int i = 0; i <= read.length - lineBreakLength; i++) {
                byte[] comparison = Arrays.copyOfRange(read, i, i + lineBreakLength);

                if (Arrays.equals(comparison, lineBreak)) {
                    return i;
                }
            }

            return -1;
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

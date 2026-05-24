package com.viveka01.format;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Response {
    public static final Response SERVER_ERROR = new Response(500, "Server Error");
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
    String origin = "*";
    String accessControlAllowMethod = "OPTIONS";
    String accessControlAllowHeaders = "Content-Type";
    String contentDisposition;
    String fileName;
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
     * Used for CORS response, use other methods ATM for responses
     * @param request http request object
     */
    public Response(Request request){
        this.statusCode = 204;
        this.contentType = ContentType.EMPTY;
        this.contentLength = 0;
        this.connection = ConnectionCodes.ALIVE;
        this.body = new byte[0];
        this.origin = "*";
        this.accessControlAllowHeaders = request.getRequestHeader();
        this.accessControlAllowMethod = request.getRequestMethod();
        this.reasonPhrase = "No content";
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

    public void setContentDisposition(ContentDisposition contentDisposition, String fileName){
        this.contentDisposition = contentDisposition.toString().toLowerCase();
        this.fileName = "\"" + fileName + "\"";
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
                            "Access-Control-Allow-Origin: " + origin + "\r\n" +
                            "Access-Control-Allow-Methods: " + accessControlAllowMethod + "\r\n" +
                            "Access-Control-Allow-Headers: " + accessControlAllowHeaders + "\r\n" +
                            "Content-Disposition: " + contentDisposition + "; filename=" + fileName + "\r\n" + 
                            "\r\n";
        System.out.println(strHeader);
        if (contentType == ContentType.PLAIN){
            try {
                System.out.write(body);
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] byteHeader = strHeader.getBytes(StandardCharsets.UTF_8);
        byte[] response = new byte[byteHeader.length + contentLength];
        System.arraycopy(byteHeader,0,response,0,byteHeader.length);
        System.arraycopy(body,0,response,byteHeader.length,contentLength);
        return response;
    }
}

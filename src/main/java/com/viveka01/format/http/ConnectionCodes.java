package com.viveka01.format.http;
/**
 * Enum holding connection types I support
 */
public enum ConnectionCodes{
    CLOSE("close"),
    ALIVE("keep-alive");

    private final String type;

    private ConnectionCodes(String type){
        this.type = type;
    }
    /**
     * @return returns string containing "Connection: type"
     */
    public String getLine(){
        return "Connection: " + type;
    }
}

package com.viveka01.format;

public enum ConnectionCodes{
    CLOSE("close"),
    ALIVE("keep-alive");

    private final String type;
    
    public static ConnectionCodes stringToCode(String code){
        switch(code){
            case "close":
                return ConnectionCodes.CLOSE;
            default:
                return ConnectionCodes.ALIVE;
        }
    }

    private ConnectionCodes(String type){
        this.type = type;
    }

    public String getLine(){
        return "Connection: " + type;
    }
}

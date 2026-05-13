package com.viveka01.logic;

import com.viveka01.format.HttpFormat;

public class CorsAccept{
    public static HttpFormat.Response handleCors(HttpFormat.Request request){
        HttpFormat.Response response = new HttpFormat.Response(request);
        return response;
    } 
}
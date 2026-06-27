package com.viveka01.logic;

import com.viveka01.format.http.Request;
import com.viveka01.format.http.Response;

public class CorsAccept{
    public static Response handleCors(Request request){
        Response response = new Response(request);
        return response;
    } 
}
package com.viveka01.logic;

import com.viveka01.format.http.Request;
import com.viveka01.format.http.Response;

public class ServerError {
    public static Response serverError(Request request){
        return Response.SERVER_ERROR;
    }
}

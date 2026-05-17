package com.viveka01.logic;

import com.viveka01.format.Request;
import com.viveka01.format.Response;

public class ServerError {
    public static Response serverError(Request request){
        return Response.SERVER_ERROR;
    }
}

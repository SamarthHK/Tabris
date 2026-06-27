package com.viveka01.router;

import com.viveka01.format.http.Request;
import com.viveka01.format.http.Response;

@FunctionalInterface
public interface RouteHandler{
    Response handle(Request request) throws Exception;
}

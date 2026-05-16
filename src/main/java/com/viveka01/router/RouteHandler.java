package com.viveka01.router;

import com.viveka01.format.Request;
import com.viveka01.format.Response;

@FunctionalInterface
public interface RouteHandler{
    Response handle(Request request) throws Exception;
}

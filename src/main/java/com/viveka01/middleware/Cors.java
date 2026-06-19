package com.viveka01.middleware;

import javax.lang.model.type.NullType;

public class Cors {
    /**
     *
     * @param origin content of origin header of request
     * @param host content of host header of request
     * @return returns true if they both match/ satisfy the whitelist, false if they cant use site
     */
    static public boolean corsInspect(String origin, String host){
        if (origin == null){
            return true;
        }
        if (origin.equals("http://localhost:8080")){
            return true;
        }
        return false;
    }
}

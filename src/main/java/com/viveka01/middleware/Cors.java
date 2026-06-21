package com.viveka01.middleware;

import com.viveka01.config.PropReader;

import javax.lang.model.type.NullType;

public class Cors {
    static final private String ACCEPTED_ORIGIN = PropReader.getInstance().getProperty("cors.accepted-origin");
    /**
     *
     * @param origin content of origin header of request
     * @param host content of host header of request
     * @return returns true if they both match/ satisfy the whitelist, false if they cant use site
     */
    static public boolean corsInspect(String origin, String host){
        if (origin == null || origin.equals(ACCEPTED_ORIGIN)){
            return true;
        }
        return false;
    }
}

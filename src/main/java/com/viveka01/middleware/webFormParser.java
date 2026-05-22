package com.viveka01.middleware;

import java.util.ArrayList;

public class webFormParser {
    String boundary;
    byte[] body;
    ArrayList<Element> elements = new ArrayList<>();
    
    public webFormParser(String boundary,byte[] body){
        this.boundary = boundary;
        this.body = body;
    }

    public void parseBody(){

    }
}

package com.viveka01.format.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonObjectMapper {
    static ObjectMapper objectMapper = createDefaultInstance();


    private static ObjectMapper createDefaultInstance(){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public static ObjectMapper getDefaultInstance(){return objectMapper;}





}

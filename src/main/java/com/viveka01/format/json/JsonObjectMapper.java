package com.viveka01.format.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viveka01.format.Initialize;

import java.io.File;

public class JsonObjectMapper implements Initialize {
    static private ObjectMapper objectMapper;
    static private JsonObjectMapper jsonObjectMapper;

    /**
     * Creating and saving a default instance to object-mapper
     */
    public static void init(){
        if (jsonObjectMapper == null){
            jsonObjectMapper = new JsonObjectMapper();
        }
    }

    private JsonObjectMapper(){
        objectMapper = createDefaultInstance();
    }

    private static ObjectMapper createDefaultInstance(){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public static ObjectMapper getDefaultInstance(){return objectMapper;}





}

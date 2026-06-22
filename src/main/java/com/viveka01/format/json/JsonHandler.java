package com.viveka01.format.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonHandler<T> {
    private Class<T> reference;
    private T instance;
    private final String jsonString;
    private JsonNode jsonNode;
    private final ObjectMapper objectMapper = JsonObjectMapper.getDefaultInstance();

    //Constructor overload
    public JsonHandler(Class<T> clazz, String jsonString) throws JsonProcessingException {
        this.reference = clazz;
        this.jsonString = jsonString;
        this.instance = objectMapper.readValue(jsonString,reference);
    }

    public JsonHandler(Class<T> clazz, File jsonFile) throws IOException{
        this.reference = clazz;
        try( InputStream read = new FileInputStream(jsonFile)){
            this.jsonString = new String(read.readAllBytes(),StandardCharsets.UTF_8);
        }
        this.instance = objectMapper.readValue(jsonString,reference);
    }

    public JsonHandler(Class<T> clazz, FileInputStream read) throws IOException{
        this.reference = clazz;
        this.jsonString = new String(read.readAllBytes(),StandardCharsets.UTF_8);
        read.close();
        this.instance = objectMapper.readValue(jsonString,reference);
    }

    public JsonHandler(String jsonString) throws JsonProcessingException {
        this.jsonString = jsonString;
        jsonNode =  objectMapper.readTree(jsonString);
    }

    public JsonHandler(File jsonFile) throws IOException{
        try(InputStream read = new FileInputStream(jsonFile)){
            this.jsonString = new String(read.readAllBytes(),StandardCharsets.UTF_8);
        }
        jsonNode =  objectMapper.readTree(jsonString);
    }
    //Getters
    public T getInstance(){return instance;}
    public String getJsonString() throws JsonProcessingException {return objectMapper.writeValueAsString(instance);}

    public <V> V getValue(String key, Class<V> type) {
        JsonNode node = jsonNode.findValue(key);
        return objectMapper.convertValue(node, type);
    }

    //Setters
    public void setClass(Class<T> clazz) throws JsonProcessingException {
        this.reference = clazz;
        this.instance = objectMapper.readValue(jsonString,reference);
    }
    public void setInstance(T instance) {this.instance = instance;}

}

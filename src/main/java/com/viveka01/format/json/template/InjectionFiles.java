package com.viveka01.format.json.template;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class InjectionFiles implements Template{
    @JsonProperty("Files")
    private ArrayList<String> files = new ArrayList<>();
    @JsonProperty("Front-End location")
    private String location;

    public InjectionFiles(){}

    public ArrayList<String> getList() {return files;}

    public void setList(ArrayList<String> files) {
        this.files = files;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

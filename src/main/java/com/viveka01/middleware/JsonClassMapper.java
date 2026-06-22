package com.viveka01.middleware;

import com.viveka01.config.PropReader;
import com.viveka01.format.Initialize;

import java.util.HashMap;

import com.viveka01.format.json.template.Template;
import org.reflections.Reflections;

public class JsonClassMapper implements Initialize {
    private final static HashMap<String,Class<?>> nameToClass = new HashMap<>();
    private static JsonClassMapper jsonClassMapper;

    /**
     * Loads all the classes inside the set package directory in prop.properties json.template-location
     */
    public static void init(){
        if (jsonClassMapper == null){
            jsonClassMapper = new JsonClassMapper();
        }
    }

    /**
     * Loads all the classes inside the set package directory in prop.properties json.template-location
     */
    private JsonClassMapper(){
        String classLocation = PropReader.getInstance().getProperty("json.template-location");
        Reflections reflect = new Reflections(classLocation);
        for (Class<?> clazz : reflect.getSubTypesOf(Template.class)){
            nameToClass.put(clazz.getName(),clazz);
        }
    }

    public static Class<?> getClass(String name){return nameToClass.get(name);}

}

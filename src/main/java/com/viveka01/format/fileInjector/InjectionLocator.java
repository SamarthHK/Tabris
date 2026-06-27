package com.viveka01.format.fileInjector;

import com.viveka01.format.FilePathHandler;
import com.viveka01.format.Initialize;
import com.viveka01.format.json.JsonHandler;
import com.viveka01.format.json.JsonObjectMapper;
import com.viveka01.format.json.template.InjectionFiles;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class InjectionLocator implements Initialize {
    String configLocation = FilePathHandler.getAbsolutePath("config|InjectionFiles.json");
    private static InjectionLocator injectionLocator;
    private static JsonHandler jsonHandler;
    private InjectionFiles injectionFiles;
    static ArrayList<String> files = new ArrayList<>();
    static HashMap<String,ArrayList<InjectionLocation>> fileLocations = new HashMap<>();

    /**
     *
     * @throws IOException If unable to read the config file
     */
    static public void init() throws IOException {
        if (injectionLocator == null){
            injectionLocator = new InjectionLocator();
        }
    }

    /**
     *
     * @throws IOException Unable to read config file
     * Just initializes a few values and feilds in the class
     */
    private InjectionLocator() throws IOException {
        jsonHandler = new JsonHandler(InjectionFiles.class,new File(configLocation));
        injectionFiles = (InjectionFiles) jsonHandler.getInstance();
        files =  injectionFiles.getList();
        loadInjectionLocations();
    }

    /**
     *
     * @throws IOException
     * Once read config file, will look through the array and get each file in the array
     * Sends each files contents into getInjectionLocations
     */
    private void loadInjectionLocations() throws IOException {
        for (String file : files) {
            String location = String.valueOf(Paths.get(injectionFiles.getLocation(),file));
            File staticFile = new File(location);

            byte[] fileBytes;
            try (FileInputStream inputStream = new FileInputStream(staticFile)) {
                fileBytes = inputStream.readAllBytes();
            }

            String fileContents = new String(fileBytes, StandardCharsets.UTF_8);

            ArrayList<InjectionLocation> injectionLocations =
                    getInjectionLocation(fileContents);

            fileLocations.put(file, injectionLocations);
        }
    }

    /**
     *
     * @param passage the text you want to scan the start, endings, and templates for
     * @return a arraylist containing all the start, endings, and templates in InjectionLocation record
     */
    private ArrayList<InjectionLocation> getInjectionLocation(String passage){
        int index = 0;
        int start;
        int end;
        ArrayList<InjectionLocation> output = new ArrayList<>();
        while(true){
            start = passage.indexOf("${",index);
            end = passage.indexOf("}",start+2) + 1;
            if (start == -1 || end == 0) break;
            InjectionType injectionType =  InjectionType.detectEnum(passage.substring(start,end));
            if (injectionType != null) {
                InjectionLocation location = new InjectionLocation(start, end, injectionType);
                output.add(location);
            }
            index = end;
        }
        return output;
    }

    /**
     *
     * @param file Takes location of file, except the root, so if it lives in /front-end/test.html input only test.html
     * @return returns the injections related to the file
     */
    public static ArrayList<InjectionLocation> getFileInjectionLocation(String file){
        return fileLocations.get(file);
    }

}

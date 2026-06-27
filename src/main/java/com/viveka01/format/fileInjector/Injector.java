package com.viveka01.format.fileInjector;

import java.util.ArrayList;
import java.util.HashMap;

public class Injector {
    private String passage;
    private final HashMap<InjectionType,ArrayList<InjectionLocation>> filenameToLocations = new HashMap<>();

    /**
     *
     * @param passage The content containing the tags
     * @param location ArrayList containing InjectionLocation record of all tags and starting/ending of them
     */
    public Injector(String passage, ArrayList<InjectionLocation> location){
        this.passage = passage;
        initializeLocation();
        for (InjectionLocation injectionLocation : location){
            this.filenameToLocations.get(injectionLocation.type()).add(injectionLocation);
        }
    }

    private void initializeLocation(){
        for (InjectionType injectionType : InjectionType.values()){
            this.filenameToLocations.put(injectionType, new ArrayList<>());
        }
    }

    /**
     *
     * @param replacement The text you want to replace the tag with
     * @param injectionType the specific tag
     * @param index which occurence of the tag, 0 is the first, and it goes upto how many there is of the type of tag you specified
     */
    public void replaceTemplate(String replacement, InjectionType injectionType, int index){
        InjectionLocation injectionLocation = this.filenameToLocations.get(injectionType).get(index);
        String passageStart = passage.substring(0,injectionLocation.start());
        String passageEnd = passage.substring(injectionLocation.end());
        this.passage =  passageStart + replacement + passageEnd;
        int offset =  replacement.length() - injectionType.getCode().length();
        updateIndexes(offset, injectionLocation.start());
    }
    public String getPassage(){return passage;}

    private void updateIndexes(int offset, int startThreshhold) {
        for (ArrayList<InjectionLocation> locations : this.filenameToLocations.values()) {
            for (int i = 0; i < locations.size(); i++) {
                InjectionLocation old = locations.get(i);
                if (old.start() < startThreshhold) continue;
                locations.set(i, new InjectionLocation(
                        old.start() + offset,
                        old.end() + offset,
                        old.type()
                ));
            }
        }
    }

}

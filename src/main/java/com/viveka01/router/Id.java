package com.viveka01.router;

import com.viveka01.format.ContentType;

public enum Id {
    //DO NOT REPEAT CODE EVERY, CODE MUST BE UNIQUE
    FILE("{f}"),
    INT("{i}"),
    STRING("{*}"),
    INVALID("{!}");

    public final String code;

    private Id(String code){
        this.code = code;
    }
    /**
     * @param segment takes in string of url segment
     * @return returns FILE, INT, or STRING enum
     */
    static public Id getType(String segment){
        if (Id.isSupportedFile(segment)){
            return Id.FILE;
        }
        try{
            Integer.valueOf(segment);
            return Id.INT;
        }catch (Exception e){
            return Id.STRING;
        }
    }

    /**
     * @param takes string of file name or anything
     * compares ending of file to all existing (supported) file endings
     * @return returns true of false
     */
    static public boolean isSupportedFile(String fileName){
        if (!fileName.contains(".")){
            return false;
        }
        String ending = fileName.substring(fileName.lastIndexOf(".")+1);
        for(ContentType type: ContentType.values()){
            if (type.getMediaType() == "EMPTY"){
                continue;
            }
            if (ending.equals(type.toString().toLowerCase())){
                return true;
            }
        }
        return false;
    }
    /**
     * @param code takes string
     * expects {f} {i} or {*}, if its none of those INVALID is returned
     * @return returns FILE, INT, or STRING if matched else will return INVALID
     */
    static public Id getIdFromCode(String code){
        for(Id id:Id.values()){
            if(code.equals(id.code)){
                return id;
            }
        }
        return Id.INVALID;
    }
}

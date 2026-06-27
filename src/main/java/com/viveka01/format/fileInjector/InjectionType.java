package com.viveka01.format.fileInjector;

public enum InjectionType {
    IMAGE("${image}"),
    LINK("${link}"),
    TITLE("${title}");
    final String code;

    private InjectionType(String code){this.code = code;}

    public String getCode() {return code;}

    /**
     *
     * @param passage string of text that can contain a template
     * @return returns the enum for the template detected. returns null if nothing detected
     */
    public static InjectionType detectEnum(String passage){
        for (InjectionType injectionType: InjectionType.values()){
            if (passage.contains(injectionType.getCode())) return injectionType;
        }
        return null;
    }
}

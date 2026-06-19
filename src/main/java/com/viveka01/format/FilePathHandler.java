package com.viveka01.format;

public class FilePathHandler {
    static Boolean isWindows;
    static{
        String osType = System.getProperty("os.name");
        isWindows = osType.startsWith("Windows");
    }
    /**
     * @param path Takes a string containing the path, seperated by '|'
     * @return returns a path that follows the convention of operating system
     * The method replaces '.' with '\\' if windows else '/'
     */
    static public String getAbsolutePath(String path){
        String delimiter = (isWindows) ? "\\" : "/";
        return path.replaceAll("\\|",delimiter);
    }
}

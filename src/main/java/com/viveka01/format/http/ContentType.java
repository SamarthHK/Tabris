package com.viveka01.format.http;
/**
 *Holding all the content types
 */
public enum ContentType{
    //Misc
    EMPTY("EMPTY","EMPTY"),
    UNSUPORTED("EMPTY","EMPTY"),
    //text
    PLAIN("text","plain"),
    HTML("text","html"),
    CSS("text","css"),
    JAVASCRIPT("text","javascript"),

    //Application
    JSON("application","json"),
    XML("application","xml"), 
    PDF("application","pdf"),   
    
    //Image
    PNG("image","png"),
    JPEG("image","jpeg"),
    JPG("image","jpg"),
    GIF("image","gif"),
    WEBP("image","webp"),
    ICO("image","ico"),
    SVG("image","svg"),

    //Video
    MPEG("video","mpeg"),
    MP4("video","mp4"),
    WEBM("video","webm"),
    
    //Multipart
    FORM("multipart","form-data");
    
    public final String mediaType;
    public final String mediaFormat;

    private ContentType(String mediaType,String mediaFormat){
        this.mediaType = mediaType;
        this.mediaFormat = mediaFormat;
    }
    /**
     *@return string containing mediaType/MediaFormat 
     */
    public String getContentType(){
        return mediaType + "/" + mediaFormat;
    }
    /**
     * @param in String representing the content type, in form of mediaType/mediaFormat
     * @return returns the ContentType enum, or UNSUPORTED if it couldnt be found
     */
    static public ContentType stringToContentType(String in){
        String[] vals = in.strip().toUpperCase().split("/");
        try{
            return ContentType.valueOf(vals[1].split("-")[0]);
        }catch (IllegalArgumentException e){
            System.out.println("Before unsupported called: "+ in);
            return ContentType.UNSUPORTED;
        }
        
    }
    /**
     * @return returns type of media
     */
    public String getMediaType(){
        return mediaType;
    }
    /**
     * @return returns format of media
     */
    public String getMediaFormat(){
        return mediaFormat;
    }
}

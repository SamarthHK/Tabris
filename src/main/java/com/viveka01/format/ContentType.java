package com.viveka01.format;
/**
 *Holding all the content types
 */
public enum ContentType{
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
    GIF("image","gif"),
    WEBP("image","webp"),

    //Video
    MPEG("video","mpeg"),
    MP4("video","mp4"),
    WEBM("video","webm");

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

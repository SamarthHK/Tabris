package com.viveka01.middleware;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.viveka01.format.RingBuffer;

public class WebFormParser {
    String boundary;
    byte[] body;
    ArrayList<Element> elements = new ArrayList<>();
    ArrayList<Integer> indexPos = new ArrayList<>();

    public WebFormParser(String boundary,byte[] body){
        this.boundary = boundary;
        this.body = body;
    }
    /**
     * @param array body of http req
     * @param subArray boundary in byte array
     */
    public void detectSubArrayIndex(byte[] array, byte[] subArray){
        RingBuffer buffer = new RingBuffer(subArray.length);
        for(int i = 0; i != array.length; i++){
            buffer.addByte(array[i]);
        }
    }
}

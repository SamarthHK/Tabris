package com.viveka01.middleware;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import com.viveka01.format.RingBuffer;

public class WebFormParser {
    String boundary;
    byte[] body;
    ArrayList<Element> elements = new ArrayList<>();
    ArrayList<int[]> indexPos = new ArrayList<>();

    public WebFormParser(String boundary,byte[] body){
        this.boundary = boundary;
        this.body = body;
        indexPos = detectSubArrayIndex(body, boundary.getBytes(StandardCharsets.UTF_8));
        elements = getElementFromIndex(body, indexPos);
    }
    /**
     * @param body the body or full packet, that corresponds to indexPos arrayList
     * @param indexPos the index positions where body should be split into elements, in form of {{start,end},{start,end}} where start is start of boundry and end is end of boundry
     */
    public ArrayList<Element> getElementFromIndex(byte[] body,ArrayList<int[]> indexPos){ 
        ArrayList<Element> elements = new ArrayList<>();
        for(int i = 0; i != indexPos.size()-1; i++){
            int start = indexPos.get(i)[1]+3;//Adding 3 so it doesnt include last digit of boundary, and skips CRLF
            int end = indexPos.get(i+1)[0]-2;// -2 for double CRLF (Idk how its not 4, i was doing unit tests and when i subtracted 2 instead of 4 its right...)
            Element element = new Element(Arrays.copyOfRange(body, start, end));
            elements.add(element);                        
        }
        return elements;
    }
    /**
     * @param array body of http req
     * @param subArray boundary in byte array
     */
    public ArrayList<int[]> detectSubArrayIndex(byte[] array, byte[] subArray){
        try {
            System.out.write(subArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        RingBuffer buffer = new RingBuffer(subArray.length);
        ArrayList<int[]> indexPos = new ArrayList<>();
        for(int i = 0; i != array.length; i++){
            buffer.addByte(array[i]);

            if (Arrays.equals(buffer.toArray(),subArray)){
                indexPos.add(new int[]{i-subArray.length+1,i});
                // int[] temp = indexPos.get(indexPos.size()-1);
                // System.out.printf("Boundary starts at: %d and ends at %d\n",temp[0],temp[1]);
            }
        }
        return indexPos;
    }

    public ArrayList<Element> getElements(){
        return elements;
    }
}

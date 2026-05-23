package com.viveka01.middleware;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    }
    /**
     * @param array body of http req
     * @param subArray boundary in byte array
     */
    public void detectSubArrayIndex(byte[] array, byte[] subArray){
        RingBuffer buffer = new RingBuffer(subArray.length);
        for(int i = 0; i != array.length; i++){
            buffer.addByte(array[i]);

            if (Arrays.equals(buffer.toArray(),subArray)){
                indexPos.add(new int[]{i-subArray.length+1,i});
                try {
                    System.out.write(buffer.toArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int[] temp = indexPos.get(indexPos.size()-1);
                System.out.printf("Boundary starts at: %d and ends at %d\n",temp[0],temp[1]);
            }
        }
        System.out.println(indexPos.size());
    }
}

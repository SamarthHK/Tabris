package com.viveka01.format.http;

/**
 * RingBuffer: Keeps track of data added and old data in order, but stays
 * constent length
 */
public class RingBuffer {
    private final byte[] buffer;
    private final int capacity;
    private int globalPosition;
    private int writePosition = 0;
    private int size = 0;

    /**
     * @param bufferSize how big the buffer should be
     */
    public RingBuffer(int bufferSize) {
        this.buffer = new byte[bufferSize];
        this.capacity = bufferSize;
    }

    /**
     * @param data byte array of what should be added
     */
    public void addData(byte[] data){
        for (byte b: data){
            addByte(b);
        }
    }

    public void addData(byte[] data, int length) {
        for (int i = 0; i < length; i++) {
            addByte(data[i]);
        }
    }

    public void addByte(byte data){
        buffer[writePosition] = data;
        writePosition = (writePosition + 1) % capacity;
        if (size < capacity) {
            size++;
        }
        globalPosition++;
    }

    /**
     * @return returns the current buffer, in proper order, and a copy so you cant
     *         change the original
     */
    public byte[] toArray() {
        byte[] result = new byte[size];
        int start = (writePosition - size + capacity) % capacity;
        for (int i = 0; i < size; i++) {
            result[i] = buffer[(start + i) % capacity];
        }
        return result;
    }

    public int getGlobal(){
        return globalPosition - size;
    }
}

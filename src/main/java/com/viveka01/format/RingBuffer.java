package com.viveka01.format;

public class RingBuffer {
    private final byte[] buffer;
    private final int capacity;
    private int writePosition = 0;
    private int size = 0;

    public RingBuffer(int bufferSize) {
        this.buffer = new byte[bufferSize];
        this.capacity = bufferSize;
    }

    public void addData(byte[] data) {
        for (byte b : data) {
            buffer[writePosition] = b;
            writePosition = (writePosition + 1) % capacity;
            if (size < capacity) {
                size++;
            }
        }
    }

    public byte[] toArray() {
        byte[] result = new byte[size];
        int start = (writePosition - size + capacity) % capacity;
        for (int i = 0; i < size; i++) {
            result[i] = buffer[(start + i) % capacity];
        }
        return result;
    }
}

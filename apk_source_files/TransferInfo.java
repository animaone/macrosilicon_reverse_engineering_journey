package com.p004ms.ms2160.myapplication;

/* renamed from: com.ms.ms2160.myapplication.TransferInfo */
public class TransferInfo {
    byte[] buffer;
    int index;
    int indexInterface;
    int length;
    int request;
    int requestType;
    int timeout;
    int value;

    public TransferInfo(int indexInterface2, int requestType2, int request2, int value2, int index2, byte[] buffer2, int length2, int timeout2) {
        this.indexInterface = indexInterface2;
        this.requestType = requestType2;
        this.request = request2;
        this.value = value2;
        this.index = index2;
        this.buffer = buffer2;
        this.length = length2;
        this.timeout = timeout2;
    }

    public int getIndexInterface() {
        return this.indexInterface;
    }

    public void setIndexInterface(int indexInterface2) {
        this.indexInterface = indexInterface2;
    }

    public int getRequestType() {
        return this.requestType;
    }

    public void setRequestType(int requestType2) {
        this.requestType = requestType2;
    }

    public int getRequest() {
        return this.request;
    }

    public void setRequest(int request2) {
        this.request = request2;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value2) {
        this.value = value2;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index2) {
        this.index = index2;
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public void setBuffer(byte[] buffer2) {
        this.buffer = buffer2;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length2) {
        this.length = length2;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout2) {
        this.timeout = timeout2;
    }
}

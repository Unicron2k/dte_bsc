package com.example.volleytest1livedata;

public class TextData {
    private String textData="";

    public TextData(String textData) {
        this.textData = textData;
    }

    public String getTextData() {
        return textData;
    }

    public void setTextData(String textData) {
        this.textData = textData;
    }

    @Override
    public String toString() {
        return textData;
    }
}

package com.example.volley.Photo;

import com.example.volley.Photo.Photo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PhotoData {
    private List<Photo> allPhotos = new ArrayList<>();

    public PhotoData(List<Photo> allPhotos) {
        this.allPhotos = allPhotos;
    }

    public PhotoData(String allAlbumsAsJson) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Photo>>(){}.getType();
        this.allPhotos = gson.fromJson(allAlbumsAsJson, type);
    }

    public PhotoData(JSONObject jsonObject) {

        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M.d.yy hh:mm a");
        gson = gsonBuilder.create();

        Type type = new TypeToken<ArrayList<Photo>>(){}.getType();
        this.allPhotos = gson.fromJson(jsonObject.toString(), type);
    }

    public List<Photo> getAllPhotos() {
        return allPhotos;
    }

    public void setAllPhotos(List<Photo> allPhotos) {
        this.allPhotos = allPhotos;
    }

    public void addPhoto(Photo photo) {
        allPhotos.add(photo);
    }

    @Override
    public String toString() {

        if (allPhotos !=null) {
            StringBuffer stringBuffer = new StringBuffer();
            for ( Photo photo : allPhotos) {
                stringBuffer.append(photo.toString());
                stringBuffer.append("\n");
            }
            return stringBuffer.toString();
        } else {
            return "...";
        }
    }
}
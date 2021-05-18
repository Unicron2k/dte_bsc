package com.example.volley.Album;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AlbumData {
    private List<Album> allAlbums = new ArrayList<>();

    public AlbumData(List<Album> allAlbums) {
        this.allAlbums = allAlbums;
    }

    public AlbumData(String allAlbumsAsJson) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Album>>(){}.getType();
        this.allAlbums = gson.fromJson(allAlbumsAsJson, type);
    }

    public AlbumData(JSONObject jsonObject) {

        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M.d.yy hh:mm a");
        gson = gsonBuilder.create();

        Type type = new TypeToken<ArrayList<Album>>(){}.getType();
        this.allAlbums = gson.fromJson(jsonObject.toString(), type);
    }

    public List<Album> getAllAlbums() {
        return allAlbums;
    }

    public void setAllAlbums(List<Album> allAlbums) {
        this.allAlbums = allAlbums;
    }

    public void addAlbum(Album album) {
        allAlbums.add(album);
    }

    @Override
    public String toString() {

        if (allAlbums!=null) {
            StringBuffer stringBuffer = new StringBuffer();
            for (Album album : allAlbums) {
                stringBuffer.append(album.toString());
                stringBuffer.append("\n");
            }
            return stringBuffer.toString();
        } else {
            return "...";
        }
    }
}

package com.shadyshrimp.lab9_2.program;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProgramData {
    private List<Program> allPrograms;

    public ProgramData(List<Program> allPrograms) {
        this.allPrograms = allPrograms;
    }

    public ProgramData(String allProgramsAsJson) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Program>>(){}.getType();
        this.allPrograms = gson.fromJson(allProgramsAsJson, type);
    }

    public ProgramData(JSONObject jsonObject) {

        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M.d.yy hh:mm a");
        gson = gsonBuilder.create();

        Type type = new TypeToken<ArrayList<Program>>(){}.getType();
        this.allPrograms = gson.fromJson(jsonObject.toString(), type);
    }

    public List<Program> getAllPrograms() {
        return allPrograms;
    }

    public void setAllPrograms(List<Program> allPrograms) {
        this.allPrograms = allPrograms;
    }

    public void addProgram(Program program) {
        allPrograms.add(program);
    }

    @Override
    public String toString() {

        if (allPrograms!=null) {
            StringBuilder stringBuffer = new StringBuilder();
            for (Program program : allPrograms) {
                stringBuffer.append(program.toString());
                stringBuffer.append("\n");
            }
            return stringBuffer.toString();
        } else {
            return "...";
        }
    }
}

package com.example.thelastdeath.entity;

import java.util.ArrayList;
import java.util.List;

public class IconFileNames {
    private String icons8_license;
    private String license_description;
    private List<String> icons8_filenames;

    public IconFileNames() {
    }

    public IconFileNames(String icons8_license, String license_description, List<String> icons8_filenames) {
        this.icons8_license = icons8_license;
        this.license_description = license_description;
        this.icons8_filenames = icons8_filenames;
    }

    /* Getters */
    public String getIcons8_license() {
        return icons8_license;
    }
    public String getLicense_description() {
        return license_description;
    }
    public List<String> getIcons8_filenames() {
        return icons8_filenames;
    }

    public static String getBaseUrl(){
        return "https://tusk.systems/trainingapp/icons/";
    }

    public List<String> getFileNamesPretty() {
        List<String> prettyFileNames = new ArrayList<>();
        for (String fileName : icons8_filenames) {
            fileName = fileName.replaceAll("icons8-", "");
            fileName = fileName.replaceAll("_", " ");
            prettyFileNames.add(fileName);
        }
        return prettyFileNames;
    }
}

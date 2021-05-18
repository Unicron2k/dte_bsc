package com.shadyshrimp.karakter1.Exercise;

public class Exercise {
    private long id;
    private String name;
    private String description;
    private String icon;
    private String infobox_color;

    public Exercise(long id, String name, String description, String icon, String infobox_color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.infobox_color = infobox_color;
    }

    public Exercise() {
        this(-1, "null", "null", "null", "null");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInfobox_color() {
        return infobox_color;
    }

    public void setInfobox_color(String infobox_color) {
        this.infobox_color = infobox_color;
    }
}

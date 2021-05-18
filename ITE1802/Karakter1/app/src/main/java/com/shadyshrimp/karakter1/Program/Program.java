package com.shadyshrimp.karakter1.Program;

public class Program {

    private long id;
    private String name;
    private String description;

    public Program(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Program(){
        this(-1,"null", "null");
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
}

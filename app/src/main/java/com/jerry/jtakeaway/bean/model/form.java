package com.jerry.jtakeaway.bean.model;

public class form {
    private int id;
    private String Name;
    private String result;

    public form() {
    }

    public form(int id, String name, String result) {
        this.id = id;
        Name = name;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

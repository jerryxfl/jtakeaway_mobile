package com.jerry.jtakeaway.bean.model;

public class Barrage {
    private String img;
    private String name;
    private String content;

    public Barrage() {
    }

    public Barrage(String img, String name, String content) {
        this.img = img;
        this.name = name;
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

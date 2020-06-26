package com.jerry.jtakeaway.bean.model;

public class NoticeStyle {
    private String name;
    private int img;

    public NoticeStyle() {
    }

    public NoticeStyle(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}

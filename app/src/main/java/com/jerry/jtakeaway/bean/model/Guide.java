package com.jerry.jtakeaway.bean.model;

public class Guide {
    private int img;
    private String text;

    public Guide() {
    }

    public Guide(int img, String text) {
        this.img = img;
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

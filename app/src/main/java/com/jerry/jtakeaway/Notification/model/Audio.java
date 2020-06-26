package com.jerry.jtakeaway.Notification.model;

import android.net.Uri;

public class Audio {
    private String name;
    private int img;
    private Uri uri;
    private int source;

    public Audio() {
    }

    public Audio(String name, int img, Uri uri,int source) {
        this.name = name;
        this.img = img;
        this.uri = uri;
        this.source = source;
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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}

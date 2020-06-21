package com.jerry.jtakeaway.bean.model;

public class TIButton {
    private int img;
    private String text;
    private Event event;

    public interface Event{
        void onClick();
    }

    public TIButton() {
    }

    public TIButton(int img, String textView, Event event) {
        this.img = img;
        text = textView;
        this.event = event;
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

    public void setText(String textView) {
        text = textView;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}

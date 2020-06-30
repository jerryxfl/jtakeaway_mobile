package com.jerry.jtakeaway.eventBusEvents;

public class BadgeDragEvent {
    private int tag;

    public BadgeDragEvent() {
    }

    public BadgeDragEvent(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}

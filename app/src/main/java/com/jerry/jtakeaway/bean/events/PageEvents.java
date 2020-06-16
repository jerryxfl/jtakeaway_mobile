package com.jerry.jtakeaway.bean.events;

public class PageEvents {
    private boolean isCanScroll;

    public PageEvents() {
    }

    public PageEvents(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }
}

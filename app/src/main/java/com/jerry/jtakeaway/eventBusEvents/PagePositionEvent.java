package com.jerry.jtakeaway.eventBusEvents;

public class PagePositionEvent {
    private int position_ViewPage;
    private int position_TabView;

    public PagePositionEvent() {
    }

    public PagePositionEvent(int position_ViewPage, int position_TabView) {
        this.position_ViewPage = position_ViewPage;
        this.position_TabView = position_TabView;
    }

    public int getPosition_ViewPage() {
        return position_ViewPage;
    }

    public void setPosition_ViewPage(int position_ViewPage) {
        this.position_ViewPage = position_ViewPage;
    }

    public int getPosition_TabView() {
        return position_TabView;
    }

    public void setPosition_TabView(int position_TabView) {
        this.position_TabView = position_TabView;
    }
}

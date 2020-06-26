package com.jerry.jtakeaway.eventBusEvents;

public class WebSocketEvent {
    private WebSocketEventType eventType;

    public WebSocketEvent() {
    }

    public WebSocketEvent(WebSocketEventType eventType) {
        this.eventType = eventType;
    }

    public WebSocketEventType getEventType() {
        return eventType;
    }

    public void setEventType(WebSocketEventType eventType) {
        this.eventType = eventType;
    }
}

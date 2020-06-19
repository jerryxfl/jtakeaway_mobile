package com.jerry.jtakeaway.bean.events;

import com.jerry.jtakeaway.bean.Address;

public class AddressEvent {
    private Address address;
    private int eventType;

    public AddressEvent() {
    }

    public AddressEvent(Address address, int eventType) {
        this.address = address;
        this.eventType = eventType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}

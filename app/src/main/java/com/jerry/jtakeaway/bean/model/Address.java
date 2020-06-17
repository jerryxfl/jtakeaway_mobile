package com.jerry.jtakeaway.bean.model;

import java.io.Serializable;

public class Address implements Serializable {
    private int id;
    private String address;
    private String detaileAddress;
    private String contact;
    private String phone;
    private String label;

    public Address() {
    }

    public Address(int id,String address, String detaileAddress, String contact, String phone, String label) {
        this.id = id;
        this.address = address;
        this.detaileAddress = detaileAddress;
        this.contact = contact;
        this.phone = phone;
        this.label = label;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetaileAddress() {
        return detaileAddress;
    }

    public void setDetaileAddress(String detaileAddress) {
        this.detaileAddress = detaileAddress;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

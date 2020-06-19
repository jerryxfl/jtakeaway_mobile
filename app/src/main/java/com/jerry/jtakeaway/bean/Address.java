package com.jerry.jtakeaway.bean;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {
    private int id;
    private String address;
    private String detaileaddress;
    private String contact;
    private String phone;
    private String label;
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetaileaddress() {
        return detaileaddress;
    }

    public void setDetaileaddress(String detaileaddress) {
        this.detaileaddress = detaileaddress;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return id == address1.id &&
                Objects.equals(address, address1.address) &&
                Objects.equals(detaileaddress, address1.detaileaddress) &&
                Objects.equals(contact, address1.contact) &&
                Objects.equals(phone, address1.phone) &&
                Objects.equals(label, address1.label);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", detaileaddress='" + detaileaddress + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", label='" + label + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, detaileaddress, contact, phone, label);
    }
}

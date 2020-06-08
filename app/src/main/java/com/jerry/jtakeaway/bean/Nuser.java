package com.jerry.jtakeaway.bean;

import java.util.Objects;

public class Nuser {
    private int id;
    private String phone;
    private String address;
    private Integer wallet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getWallet() {
        return wallet;
    }

    public void setWallet(Integer wallet) {
        this.wallet = wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nuser nuser = (Nuser) o;
        return id == nuser.id &&
                Objects.equals(phone, nuser.phone) &&
                Objects.equals(address, nuser.address) &&
                Objects.equals(wallet, nuser.wallet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, address, wallet);
    }
}

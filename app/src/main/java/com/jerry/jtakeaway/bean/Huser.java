package com.jerry.jtakeaway.bean;

import java.util.Objects;

public class Huser {
    private int id;
    private String name;
    private String idcard;
    private String transport;
    private Integer walletid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public Integer getWalletid() {
        return walletid;
    }

    public void setWalletid(Integer walletid) {
        this.walletid = walletid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Huser huser = (Huser) o;
        return id == huser.id &&
                Objects.equals(name, huser.name) &&
                Objects.equals(idcard, huser.idcard) &&
                Objects.equals(transport, huser.transport) &&
                Objects.equals(walletid, huser.walletid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, idcard, transport, walletid);
    }
}

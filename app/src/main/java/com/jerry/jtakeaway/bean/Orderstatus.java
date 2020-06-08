package com.jerry.jtakeaway.bean;


import java.util.Objects;

public class Orderstatus {
    private int id;
    private int statusnum;
    private String statusdesc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatusnum() {
        return statusnum;
    }

    public void setStatusnum(int statusnum) {
        this.statusnum = statusnum;
    }

    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orderstatus that = (Orderstatus) o;
        return id == that.id &&
                statusnum == that.statusnum &&
                Objects.equals(statusdesc, that.statusdesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statusnum, statusdesc);
    }
}

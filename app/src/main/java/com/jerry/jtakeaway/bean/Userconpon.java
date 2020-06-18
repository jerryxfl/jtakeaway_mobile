package com.jerry.jtakeaway.bean;


import java.sql.Timestamp;
import java.util.Objects;

public class Userconpon {
    private int id;
    private int conponid;
    private int nuserid;
    private Timestamp createtime;
    private int status;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConponid() {
        return conponid;
    }

    public void setConponid(int conponid) {
        this.conponid = conponid;
    }

    public int getNuserid() {
        return nuserid;
    }

    public void setNuserid(int nuserid) {
        this.nuserid = nuserid;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Userconpon that = (Userconpon) o;
        return id == that.id &&
                conponid == that.conponid &&
                nuserid == that.nuserid &&
                Objects.equals(createtime, that.createtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, conponid, nuserid, createtime);
    }
}

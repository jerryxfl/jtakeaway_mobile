package com.jerry.jtakeaway.bean;

import java.sql.Timestamp;
import java.util.Objects;

public class Orde {
    private int id;
    private int nuserid;
    private Timestamp createdTime;
    private int suserid;
    private String menus;
    private Integer huserid;
    private int statusid;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNuserid() {
        return nuserid;
    }

    public void setNuserid(int nuserid) {
        this.nuserid = nuserid;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public int getSuserid() {
        return suserid;
    }

    public void setSuserid(int suserid) {
        this.suserid = suserid;
    }

    public String getMenus() {
        return menus;
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }

    public Integer getHuserid() {
        return huserid;
    }

    public void setHuserid(Integer huserid) {
        this.huserid = huserid;
    }

    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orde orde = (Orde) o;
        return id == orde.id &&
                nuserid == orde.nuserid &&
                suserid == orde.suserid &&
                statusid == orde.statusid &&
                Objects.equals(createdTime, orde.createdTime) &&
                Objects.equals(menus, orde.menus) &&
                Objects.equals(huserid, orde.huserid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nuserid, createdTime, suserid, menus, huserid, statusid);
    }
}

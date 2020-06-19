package com.jerry.jtakeaway.bean;

import java.sql.Timestamp;
import java.util.Objects;

public class Coupon {
    private int id;
    private String conpondesc;
    private Double conponprice;
    private Integer conpontarget;
    private Timestamp conponfailuretime;
    private int num;
    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConpondesc() {
        return conpondesc;
    }

    public void setConpondesc(String conpondesc) {
        this.conpondesc = conpondesc;
    }

    public Double getConponprice() {
        return conponprice;
    }

    public void setConponprice(Double conponprice) {
        this.conponprice = conponprice;
    }

    public Integer getConpontarget() {
        return conpontarget;
    }

    public void setConpontarget(Integer conpontarget) {
        this.conpontarget = conpontarget;
    }

    public Timestamp getConponfailuretime() {
        return conponfailuretime;
    }

    public void setConponfailuretime(Timestamp conponfailuretime) {
        this.conponfailuretime = conponfailuretime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return id == coupon.id &&
                Objects.equals(conpondesc, coupon.conpondesc) &&
                Objects.equals(conponprice, coupon.conponprice) &&
                Objects.equals(conpontarget, coupon.conpontarget) &&
                Objects.equals(conponfailuretime, coupon.conponfailuretime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, conpondesc, conponprice, conpontarget, conponfailuretime);
    }
}

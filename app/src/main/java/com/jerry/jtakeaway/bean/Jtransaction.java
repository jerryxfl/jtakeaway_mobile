package com.jerry.jtakeaway.bean;


import java.sql.Timestamp;
import java.util.Objects;

public class Jtransaction {
    private int id;
    private Timestamp paytime;
    private Double paymoney;
    private int userid;
    private String more;
    private Integer couponid;
    private int targetuserid;
    private String uuid;

    public int getTargetuserid() {
        return targetuserid;
    }

    public void setTargetuserid(int targetuserid) {
        this.targetuserid = targetuserid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getPaytime() {
        return paytime;
    }

    public void setPaytime(Timestamp paytime) {
        this.paytime = paytime;
    }

    public Double getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(Double paymoney) {
        this.paymoney = paymoney;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public Integer getCouponid() {
        return couponid;
    }

    public void setCouponid(Integer couponid) {
        this.couponid = couponid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jtransaction that = (Jtransaction) o;
        return id == that.id &&
                userid == that.userid &&
                Objects.equals(paytime, that.paytime) &&
                Objects.equals(paymoney, that.paymoney) &&
                Objects.equals(more, that.more) &&
                Objects.equals(couponid, that.couponid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paytime, paymoney, userid, more, couponid);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

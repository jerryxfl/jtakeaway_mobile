package com.jerry.jtakeaway.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class Menus implements Serializable {
    private int id;
    private int suerid;
    private String foodname;
    private String foodimg;
    private String fooddesc;
    private double foodprice;
    private double foodlowprice;
    private int foodstatus;
    private Timestamp lowpricefailed;

    public Menus() {
    }

    public Menus(int id, int suerid, String foodname, String foodimg, String fooddesc, double foodprice, double foodlowprice, int foodstatus,Timestamp lowpricefailed) {
        this.id = id;
        this.suerid = suerid;
        this.foodname = foodname;
        this.foodimg = foodimg;
        this.fooddesc = fooddesc;
        this.foodprice = foodprice;
        this.foodlowprice = foodlowprice;
        this.foodstatus = foodstatus;
        this.lowpricefailed = lowpricefailed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSuerid() {
        return suerid;
    }

    public void setSuerid(int suerid) {
        this.suerid = suerid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodimg() {
        return foodimg;
    }

    public void setFoodimg(String foodimg) {
        this.foodimg = foodimg;
    }

    public String getFooddesc() {
        return fooddesc;
    }

    public void setFooddesc(String fooddesc) {
        this.fooddesc = fooddesc;
    }

    public double getFoodprice() {
        return foodprice;
    }

    public void setFoodprice(double foodprice) {
        this.foodprice = foodprice;
    }

    public double getFoodlowprice() {
        return foodlowprice;
    }

    public void setFoodlowprice(double foodlowprice) {
        this.foodlowprice = foodlowprice;
    }

    public int getFoodstatus() {
        return foodstatus;
    }

    public void setFoodstatus(int foodstatus) {
        this.foodstatus = foodstatus;
    }

    public Timestamp getLowpricefailed() {
        return lowpricefailed;
    }

    public void setLowpricefailed(Timestamp lowpricefailed) {
        this.lowpricefailed = lowpricefailed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menus menus = (Menus) o;
        return id == menus.id &&
                suerid == menus.suerid &&
                foodstatus == menus.foodstatus &&
                Objects.equals(foodname, menus.foodname) &&
                Objects.equals(foodimg, menus.foodimg) &&
                Objects.equals(fooddesc, menus.fooddesc) &&
                Objects.equals(foodprice, menus.foodprice) &&
                Objects.equals(foodlowprice, menus.foodlowprice);
    }

    @Override
    public String toString() {
        return "Menus{" +
                "id=" + id +
                ", suerid=" + suerid +
                ", foodname='" + foodname + '\'' +
                ", foodimg='" + foodimg + '\'' +
                ", fooddesc='" + fooddesc + '\'' +
                ", foodprice=" + foodprice +
                ", foodlowprice=" + foodlowprice +
                ", foodstatus=" + foodstatus +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, suerid, foodname, foodimg, fooddesc, foodprice, foodlowprice, foodstatus);
    }
}

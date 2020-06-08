package com.jerry.jtakeaway.bean;

import java.math.BigDecimal;
import java.util.Objects;

public class Menus {
    private int id;
    private int suerid;
    private String foodname;
    private String foodimg;
    private String fooddesc;
    private BigDecimal foodprice;
    private BigDecimal foodlowprice;
    private int foodstatus;

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

    public BigDecimal getFoodprice() {
        return foodprice;
    }

    public void setFoodprice(BigDecimal foodprice) {
        this.foodprice = foodprice;
    }

    public BigDecimal getFoodlowprice() {
        return foodlowprice;
    }

    public void setFoodlowprice(BigDecimal foodlowprice) {
        this.foodlowprice = foodlowprice;
    }

    public int getFoodstatus() {
        return foodstatus;
    }

    public void setFoodstatus(int foodstatus) {
        this.foodstatus = foodstatus;
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
    public int hashCode() {
        return Objects.hash(id, suerid, foodname, foodimg, fooddesc, foodprice, foodlowprice, foodstatus);
    }
}

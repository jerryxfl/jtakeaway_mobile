package com.jerry.jtakeaway.bean;


import java.util.Objects;

public class Suser {
    private int id;
    private String shopname;
    private String shoplicense;
    private String idcard;
    private String name;
    private String shopaddress;
    private String slideid;
    private Integer applyid;
    private Integer walletid;

    public Integer getWalletid() {
        return walletid;
    }

    public void setWalletid(Integer walletid) {
        this.walletid = walletid;
    }

    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShoplicense() {
        return shoplicense;
    }

    public void setShoplicense(String shoplicense) {
        this.shoplicense = shoplicense;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }

    public String getSlideid() {
        return slideid;
    }

    public void setSlideid(String slideid) {
        this.slideid = slideid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Suser suser = (Suser) o;
        return id == suser.id &&
                Objects.equals(shopname, suser.shopname) &&
                Objects.equals(shoplicense, suser.shoplicense) &&
                Objects.equals(idcard, suser.idcard) &&
                Objects.equals(name, suser.name) &&
                Objects.equals(shopaddress, suser.shopaddress) &&
                Objects.equals(slideid, suser.slideid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopname, shoplicense, idcard, name, shopaddress, slideid);
    }
}

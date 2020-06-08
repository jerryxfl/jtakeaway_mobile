package com.jerry.jtakeaway.bean;


import java.util.Objects;

public class Xuser {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Xuser xuser = (Xuser) o;
        return id == xuser.id ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

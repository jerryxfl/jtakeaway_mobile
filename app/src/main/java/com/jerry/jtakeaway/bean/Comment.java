package com.jerry.jtakeaway.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class Comment implements Serializable {
    private int id;
    private String content;
    private Timestamp createtime;
    private Suser suser;
    private User user;


    public Suser getSuser() {
        return suser;
    }

    public void setSuser(Suser suser) {
        this.suser = suser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        Comment comment = (Comment) o;
        return id == comment.id &&
                Objects.equals(content, comment.content) &&
                Objects.equals(createtime, comment.createtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createtime);
    }
}

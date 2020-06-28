package com.jerry.jtakeaway.bean;

import java.sql.Timestamp;
import java.util.Objects;

public class Msg {
    private int id;
    private Integer acceptuserid;
    private Integer senduserid;
    private String content;
    private Timestamp sendTime;
    private int readalready;
    private int pushalready;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAcceptuserid() {
        return acceptuserid;
    }

    public void setAcceptuserid(Integer acceptuserid) {
        this.acceptuserid = acceptuserid;
    }

    public Integer getSenduserid() {
        return senduserid;
    }

    public void setSenduserid(Integer senduserid) {
        this.senduserid = senduserid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Msg msg = (Msg) o;
        return id == msg.id &&
                acceptuserid == msg.acceptuserid &&
                senduserid == msg.senduserid &&
                Objects.equals(content, msg.content) &&
                Objects.equals(sendTime, msg.sendTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, acceptuserid, senduserid, content, sendTime);
    }

    public int getReadalready() {
        return readalready;
    }

    public void setReadalready(int readalready) {
        this.readalready = readalready;
    }

    public int getPushalready() {
        return pushalready;
    }

    public void setPushalready(int pushalready) {
        this.pushalready = pushalready;
    }
}

package com.jerry.jtakeaway.bean.events;

public class InvestMoney {
    private boolean active;

    public InvestMoney() {
    }

    public InvestMoney(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

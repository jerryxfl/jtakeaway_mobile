package com.jerry.jtakeaway.bean;

import java.util.Objects;

public class Wallet {
    private int id;
    private Double balance;
    private String paymentpassword;
    private String transactionid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getPaymentpassword() {
        return paymentpassword;
    }

    public void setPaymentpassword(String paymentpassword) {
        this.paymentpassword = paymentpassword;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return id == wallet.id &&
                Objects.equals(balance, wallet.balance) &&
                Objects.equals(paymentpassword, wallet.paymentpassword) &&
                Objects.equals(transactionid, wallet.transactionid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, paymentpassword, transactionid);
    }
}

package com.pranitkulkarni.expensemanager.bank_accounts;

import android.support.v4.content.ContextCompat;

/**
 * Created by pranitkulkarni on 2/11/17.
 */

public class AccountModel {

    private float balance = (float) 0.0;
    private String name = "";
    private int type = 0,id=0;

    public void setId(int id) {
        this.id = id;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public float getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

}

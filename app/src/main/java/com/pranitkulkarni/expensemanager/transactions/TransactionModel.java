package com.pranitkulkarni.expensemanager.transactions;

import java.util.Date;

/**
 * Created by pranitkulkarni on 3/8/17.
 */

public class TransactionModel {

    private int id =0, sender_id = 0, receiver_id = 0, currency_id = 0;
    private String description = "";
    private float amount = 0;
    private Date date_of_transaction,created_at;
    private Boolean repeat;

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public void setCurrency_id(int currency_id) {
        this.currency_id = currency_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public void setDate_of_transaction(Date date_of_transaction) {
        this.date_of_transaction = date_of_transaction;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    ///// ---------- GET METHODS ----------------


    public int getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public int getCurrency_id() {
        return currency_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate_of_transaction() {
        return date_of_transaction;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Boolean getRepeat() {
        return repeat;
    }
}

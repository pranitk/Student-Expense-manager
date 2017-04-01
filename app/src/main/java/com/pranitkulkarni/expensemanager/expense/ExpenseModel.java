package com.pranitkulkarni.expensemanager.expense;

import android.util.Log;

import com.pranitkulkarni.expensemanager.bank_accounts.AccountModel;

import java.util.Date;

/**
 * Created by pranitkulkarni on 2/11/17.
 */

public class ExpenseModel {

    private float amount = 0;
    private String desc = "";
    private int category_id = 0,account_id = 0,currency_id=0,id=0,day=0,month=0,year=0;
    private Date updated_at;
    private AccountModel accountModel;
    private ExpenseCategoryModel expenseCategoryModel;

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setUpdated_at(Date date) {
        Log.d("Saving date as",""+date);
        this.updated_at = date;
    }

    public void setCurrency_id(int currency_id) {
        this.currency_id = currency_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setAccountModel(AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    public void setExpenseCategoryModel(ExpenseCategoryModel expenseCategoryModel) {
        this.expenseCategoryModel = expenseCategoryModel;
    }

    // --------- GET METHODS ------------------


    public int getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public int getAccount_id() {
        return account_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public String getDesc() {
        return desc;
    }

    public int getCurrency_id() {
        return currency_id;
    }


    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }


    public AccountModel getAccountModel() {
        return accountModel;
    }

    public ExpenseCategoryModel getExpenseCategoryModel() {
        return expenseCategoryModel;
    }
}

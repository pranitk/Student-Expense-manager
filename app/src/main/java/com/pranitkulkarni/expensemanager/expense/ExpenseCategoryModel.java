package com.pranitkulkarni.expensemanager.expense;

/**
 * Created by pranitkulkarni on 2/16/17.
 */

public class ExpenseCategoryModel {

    private int id = 0;
    private String name = "", icon = "";

    public void setId(int id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    /////////////////   GET FUNCTIONS ////////////////


    public int getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}

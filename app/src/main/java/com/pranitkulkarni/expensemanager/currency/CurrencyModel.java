package com.pranitkulkarni.expensemanager.currency;

/**
 * Created by pranitkulkarni on 3/28/17.
 */

public class CurrencyModel {

    String code = "",symbol="",name="";

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

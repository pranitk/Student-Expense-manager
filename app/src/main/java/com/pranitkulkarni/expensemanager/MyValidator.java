package com.pranitkulkarni.expensemanager;

import android.net.ParseException;
import android.widget.EditText;

/**
 * Created by pranitkulkarni on 2/12/17.
 */

public class MyValidator {

    public Boolean isStringValid(EditText editText){

        if (editText.getText() == null)
            return false;

        if (editText.getText().toString().isEmpty())
            return false;
        else if (editText.getText().toString().equals("null"))
            return false;

        return true;
    }

    public Boolean isAmountValid(String amount){

        try {
            float value = Float.parseFloat(amount);

            return true;
        }
        catch (ParseException p){
            p.printStackTrace();
        }

        return false;
    }
}

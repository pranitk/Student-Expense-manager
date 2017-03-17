package com.pranitkulkarni.expensemanager.bank_accounts;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.pranitkulkarni.expensemanager.MyValidator;
import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.database.DatabaseHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddAccount extends AppCompatActivity {

    EditText accountNameEt,balanceEt;
    RadioGroup radioGroup;
    CoordinatorLayout coordinatorLayout;
    public int selected_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_2);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().build());

        accountNameEt = (EditText)findViewById(R.id.account_name);
        balanceEt = (EditText)findViewById(R.id.balance);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (areFieldsValid()){

                    AccountModel account = new AccountModel();
                    account.setBalance(Float.parseFloat(balanceEt.getText().toString()));
                    account.setName(accountNameEt.getText().toString());

                    if (radioGroup.getCheckedRadioButtonId() == R.id.radio_card)
                        account.setType(2);
                    else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_cash)
                        account.setType(1);

                    new DatabaseHelper(AddAccount.this).addAccount(account);    // Add entry to database

                    setResult(RESULT_OK);
                    finish();
                }

            }
        });


        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    private Boolean areFieldsValid(){

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Snackbar.make(coordinatorLayout,"Select account type",Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (!new MyValidator().isStringValid(accountNameEt)){
            Snackbar.make(coordinatorLayout,"Enter valid account name",Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (!new MyValidator().isStringValid(balanceEt)){
            Snackbar.make(coordinatorLayout,"Enter valid balance",Snackbar.LENGTH_LONG).show();
            return false;
        }


        return true;
    }


    @Override
    protected void attachBaseContext(Context tp) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(tp));
    }
}

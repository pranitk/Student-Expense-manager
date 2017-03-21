package com.pranitkulkarni.expensemanager.transactions;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.MyValidator;
import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.bank_accounts.SelectAccount;
import com.pranitkulkarni.expensemanager.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddTransfer extends AppCompatActivity {

    EditText amountEt,descriptionEt;
    TextView senderAccountTv,receiverAccountTv,dateTv;
    int sender_account_id=0,receiver_account_id=0,transaction_day=0,transaction_month=0,transaction_year=0;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transfer);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.Open_regular)).build());


        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        amountEt = (EditText)findViewById(R.id.amount);
        descriptionEt = (EditText)findViewById(R.id.description);
        senderAccountTv = (TextView)findViewById(R.id.sender_account_text);
        receiverAccountTv = (TextView)findViewById(R.id.receiver_account_text);
        dateTv = (TextView)findViewById(R.id.select_date_text);


        transaction_year = Calendar.getInstance().get(Calendar.YEAR);
        transaction_month = Calendar.getInstance().get(Calendar.MONTH);
        transaction_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        findViewById(R.id.select_sender_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(AddTransfer.this, SelectAccount.class),1);

            }
        });


        findViewById(R.id.select_receiver_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(AddTransfer.this, SelectAccount.class),2);

            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        findViewById(R.id.select_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTransfer.this,dateSetListener,transaction_year,transaction_month,transaction_day);
                datePickerDialog.show();

            }
        });


        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (areFieldsValid()){  // Check if input is valid....

                    try {


                        final DatabaseHelper databaseHelper = new DatabaseHelper(AddTransfer.this);

                        TransactionModel transaction = new TransactionModel();
                        transaction.setAmount(Float.parseFloat(amountEt.getText().toString()));
                        transaction.setCurrency_id(1);  // TODO
                        transaction.setDescription(descriptionEt.getText().toString());
                        transaction.setReceiver_id(receiver_account_id); // to
                        transaction.setSender_id(sender_account_id);    // from

                        transaction.setRepeat(false);

                        transaction.setType(1);
                        transaction.setDay(transaction_day);
                        transaction.setMonth(transaction_month + 1);
                        transaction.setYear(transaction_year);

                        //String date = transaction_day+"-"+(transaction_month+1)+"-"+transaction_year;
                        //transaction.setDate_of_transaction(new SimpleDateFormat("dd-MM-yyyy").parse(date));
                        transaction.setCreated_at(Calendar.getInstance().getTime());


                        if(databaseHelper.addTransaction(transaction))
                            finish();   // close page if transfer is saved
                        else
                            Snackbar.make(coordinatorLayout,"Something went wrong!",Snackbar.LENGTH_LONG).show();

                        databaseHelper.close();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                }


            }
        });


    }



    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            transaction_day = dayOfMonth;
            transaction_month = month;
            transaction_year = year;


            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
            SimpleDateFormat systemFormat = new SimpleDateFormat("MM");


            if (year == Calendar.getInstance().get(Calendar.YEAR) && month == Calendar.getInstance().get(Calendar.MONTH) && dayOfMonth == Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                dateTv.setText("TODAY");
            else {

                String month_name = "";

                try{

                    month++;
                    month_name = monthFormat.format(systemFormat.parseObject(String.valueOf(month)));

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                dateTv.setText(dayOfMonth + " " + month_name + " " + year);


            }

        }
    };


    private Boolean areFieldsValid() {

        final MyValidator validator = new MyValidator();

        if (validator.isStringValid(amountEt)) {

            if (!validator.isAmountValid(amountEt.getText().toString())) {

                Snackbar.make(coordinatorLayout, "Invalid amount", Snackbar.LENGTH_LONG).show();
                return false;
            }

        } else {

            Snackbar.make(coordinatorLayout, "Enter amount", Snackbar.LENGTH_LONG).show();
            return false;
        }


        if (sender_account_id <= 0){

            Snackbar.make(coordinatorLayout,"Select account 1",Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivityForResult(new Intent(AddTransfer.this,SelectAccount.class),1);

                }
            });

            return false;
        }


        if (receiver_account_id <= 0){

            Snackbar.make(coordinatorLayout,"Select account 2",Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivityForResult(new Intent(AddTransfer.this,SelectAccount.class),2);

                }
            });

            return false;
        }


        if (transaction_day <=0 || transaction_month <= 0 || transaction_year <= 0) {

            Snackbar.make(coordinatorLayout,"Invalid date of transaction",Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (!validator.isStringValid(descriptionEt)){

            Snackbar.make(coordinatorLayout,"Enter description",Snackbar.LENGTH_LONG).show();

            return false;
        }

        return true;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) { // from

            if (resultCode == Activity.RESULT_OK) {

                sender_account_id = data.getIntExtra("account_id",0);
                senderAccountTv.setText(data.getStringExtra("account_name"));

            }
        }


        if (requestCode == 2) { // to

            if (resultCode == Activity.RESULT_OK) {

                receiver_account_id = data.getIntExtra("account_id",0);
                receiverAccountTv.setText(data.getStringExtra("account_name"));

            }
        }


    }

    @Override
    protected void attachBaseContext(Context tp) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(tp));
    }

}

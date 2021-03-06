package com.pranitkulkarni.expensemanager.expense;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.MyValidator;
import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.bank_accounts.AccountModel;
import com.pranitkulkarni.expensemanager.bank_accounts.SelectAccount;
import com.pranitkulkarni.expensemanager.currency.SelectCurrency;
import com.pranitkulkarni.expensemanager.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddExpense extends AppCompatActivity {


    int category_id = 0,account_id = 0,expense_year=0,expense_month=0,expense_day=0;
    int expense_id =0; // Only to be used for EDIT EXPENSE
    Boolean isEdit;
    String account_name = "",category_name = "",category_icon = "";
    Float old_amount;
    TextView accountTv,dateTv,categoryTv;
    EditText amountEt,descEt;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_expense);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.Open_regular)).build());


        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        descEt = (EditText)findViewById(R.id.expense_desc);
        accountTv = (TextView)findViewById(R.id.select_account_text);
        categoryTv = (TextView)findViewById(R.id.select_category_text);
        dateTv = (TextView)findViewById(R.id.select_date_text);
        amountEt = (EditText)findViewById(R.id.expense_amount);

        //expense_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


        findViewById(R.id.select_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddExpense.this, SelectAccount.class),1);
            }
        });


        findViewById(R.id.select_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddExpense.this, SelectCategory.class),2);

            }
        });


        /*findViewById(R.id.currency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(AddExpense.this,SelectCurrency.class),3);

            }
        });*/

        isEdit = getIntent().getBooleanExtra("isEdit",false);

        if (isEdit)
            getDataFromIntent();
        else {

            final Calendar calendar = Calendar.getInstance();
            expense_year = calendar.get(Calendar.YEAR);
            expense_month = calendar.get(Calendar.MONTH);
            expense_day = calendar.get(Calendar.DAY_OF_MONTH);
        }



        findViewById(R.id.select_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpense.this,dateSetListener,expense_year,expense_month,expense_day);
                datePickerDialog.show();

            }
        });


        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddExpense.this);
                builder.setTitle("Save expense ?");
                builder.setNegativeButton("Cancel",null);
                builder.setPositiveButton("Yes",null);

                AlertDialog alertDialog = builder.show();

                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (areFieldsValid()){

                            try
                            {

                                final DatabaseHelper databaseHelper = new DatabaseHelper(AddExpense.this);

                                ExpenseModel expense = new ExpenseModel();
                                expense.setAccount_id(account_id);
                                expense.setAmount(Float.parseFloat(amountEt.getText().toString()));
                                expense.setCategory_id(category_id);
                                //expense.setCurrency_id(1);
                                expense.setDesc(descEt.getText().toString());
                                expense.setUpdated_at(Calendar.getInstance().getTime());
                                /*String date = expense_day+"/"+(expense_month + 1)+"/"+expense_year;
                                expense.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));*/

                                Log.d("Day of expense",""+expense_day);
                                Log.d("Year of expense",""+expense_year);

                                expense.setDay(expense_day);
                                expense.setMonth(expense_month);
                                expense.setYear(expense_year);

                                if (isEdit){

                                    // TODO: What if account was changed ? Adjust the balance accordingly...

                                    expense.setId(expense_id);  // ID required to update row
                                    Boolean updatedSuccessfully = false;

                                    //if amount is changed then update it to database
                                    if (old_amount != expense.getAmount()) {
                                        if (databaseHelper.updateAccountBalance(account_id, expense.getAmount() - old_amount)) // Add the difference
                                            updatedSuccessfully = databaseHelper.updateExpense(expense);
                                    }
                                    else // if amount is unchanged then only update other details
                                        updatedSuccessfully = databaseHelper.updateExpense(expense);


                                    if (updatedSuccessfully)
                                        finish();
                                    else
                                        Snackbar.make(coordinatorLayout,"Something went wrong!",Snackbar.LENGTH_LONG).show();

                                }
                                else {


                                    if(databaseHelper.addExpense(expense))
                                        finish();   // close page if expense is saved
                                    else
                                        Snackbar.make(coordinatorLayout,"Something went wrong!",Snackbar.LENGTH_LONG).show();

                                }


                                databaseHelper.close();

                                Log.d("Expense Added - "," Date: "+expense.getUpdated_at());

                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }

                    }
                });



            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    // Used for edit expense...

    private void getDataFromIntent(){


        /*final DatabaseHelper databaseHelper = new DatabaseHelper(AddExpense.this);

        final ExpenseModel expense = (ExpenseModel) getIntent().getSerializableExtra("expense_model");

        category_id = expense.getCategory_id();
        account_id = expense.getAccount_id();
        expense_year = expense.getYear();
        expense_month = expense.getMonth();
        expense_day = expense.getDay();
        amountEt.setText(String.valueOf(expense.getAmount()));
        descEt.setText(expense.getDesc());

        accountTv.setText(databaseHelper.getAccountName(account_id));
        categoryTv.setText(databaseHelper.getCategoryName(category_id));*/

        Intent data = getIntent();
        expense_id = data.getIntExtra("id",0);
        setCategory(data);
        setAccount(data);
        descEt.setText(data.getStringExtra("description"));
        old_amount = data.getFloatExtra("amount_value",0);
        amountEt.setText(data.getStringExtra("amount"));
        expense_year = data.getIntExtra("year",0);
        expense_month = data.getIntExtra("month",0);
        expense_day = data.getIntExtra("day",0);


        // Set date ...

        final Calendar calendar = Calendar.getInstance();

        if (expense_year == calendar.get(Calendar.YEAR) && expense_month == calendar.get(Calendar.MONTH) && expense_day == calendar.get(Calendar.DAY_OF_MONTH))
            dateTv.setText("TODAY");
        else {


            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
            SimpleDateFormat systemFormat = new SimpleDateFormat("MM");

            try {

                String monthText = monthFormat.format(systemFormat.parseObject(String.valueOf(expense_month + 1)));
                dateTv.setText(expense_day+" "+monthText+" "+expense_year);

            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult"," called ....");

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK)
                setAccount(data);
        }

        if (requestCode == 2){

            if (resultCode == Activity.RESULT_OK)
                setCategory(data);

        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            expense_year = year;
            expense_month = month;
            expense_day = dayOfMonth;

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


    private Boolean areFieldsValid(){

        final MyValidator validator = new MyValidator();

        if (validator.isStringValid(amountEt)){

            if (!validator.isAmountValid(amountEt.getText().toString())) {

                Snackbar.make(coordinatorLayout,"Enter expense amount",Snackbar.LENGTH_LONG).show();
                return false;
            }

        }
        else {

            Snackbar.make(coordinatorLayout,"Enter expense amount",Snackbar.LENGTH_LONG).show();
            return false;
        }


        if (!validator.isStringValid(descEt)){
            Snackbar.make(coordinatorLayout,"Enter expense description",Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (account_id <= 0) {

            Snackbar.make(coordinatorLayout,"Select one account",Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                startActivityForResult(new Intent(AddExpense.this, SelectAccount.class),1);

                }
            }).show();

            return false;
        }


        if (category_id <= 0) {

            Snackbar.make(coordinatorLayout,"Select expense category",Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    startActivityForResult(new Intent(AddExpense.this, SelectCategory.class),2);

                }
            }).show();

            return false;
        }

        if (expense_day <=0 || expense_month <= 0 || expense_year <= 0) {

            Snackbar.make(coordinatorLayout,"Invalid date of expense",Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;

    }


    private void setCategory(Intent data){

        category_id = data.getIntExtra("category_id",0);
        category_name = data.getStringExtra("category_name");
        category_icon = data.getStringExtra("category_icon");


        categoryTv.setText(category_name);

        // Hide the imageview and show the fontawesome text icon
        TextView iconText = (TextView)findViewById(R.id.selected_category_icon);
        findViewById(R.id.category_icon).setVisibility(View.GONE);

        iconText.setText(category_icon);
        iconText.setVisibility(View.VISIBLE);
    }

    private void setAccount(Intent data){
        account_id = data.getIntExtra("account_id",0);
        account_name = data.getStringExtra("account_name");
        accountTv.setText(account_name);
    }

    /*private void showAllCategories(){


        AlertDialog.Builder builder = new AlertDialog.Builder(AddExpense.this);
        builder.setTitle("Select category");

        View view = LayoutInflater.from(AddExpense.this).inflate(R.layout.recycler_view,null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(AddExpense.this));
        recyclerView.setAdapter(new AdapterSelectExpenseCategory(categories_list));

        builder.setView(view);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                categoryTv.setText(category_name);

                TextView iconText = (TextView)findViewById(R.id.selected_category_icon);
                findViewById(R.id.category_icon).setVisibility(View.GONE);

                iconText.setText(category_icon);
                iconText.setVisibility(View.VISIBLE);

            }
        });

        builder.create().show();
    }

    private void showAllAccounts(){


        AlertDialog.Builder builder = new AlertDialog.Builder(AddExpense.this);
        builder.setTitle("Select account");

        View view = LayoutInflater.from(AddExpense.this).inflate(R.layout.recycler_view,null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(AddExpense.this));
        recyclerView.setAdapter(new AdapterSelectAccount(AddExpense.this,accounts_list));

        builder.setView(view);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                accountTv.setText(account_name);

            }
        });


        builder.create().show();

    }*/

    @Override
    protected void attachBaseContext(Context tp) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(tp));
    }



}

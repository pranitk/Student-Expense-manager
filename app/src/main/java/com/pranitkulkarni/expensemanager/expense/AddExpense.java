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
import com.pranitkulkarni.expensemanager.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddExpense extends AppCompatActivity {


    int category_id = 0,account_id = 0,expense_year=0,expense_month=0,expense_day=0;
    String account_name = "",category_name = "",category_icon = "";
    DatabaseHelper databaseHelper;
    TextView accountTv,dateTv,categoryTv;
    EditText amountEt,descEt;
    CoordinatorLayout coordinatorLayout;
    List<AccountModel> accounts_list;
    List<ExpenseCategoryModel> categories_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_expense);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.Open_regular)).build());

        databaseHelper = new DatabaseHelper(AddExpense.this);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        descEt = (EditText)findViewById(R.id.expense_desc);
        accountTv = (TextView)findViewById(R.id.select_account_text);
        categoryTv = (TextView)findViewById(R.id.select_category_text);
        dateTv = (TextView)findViewById(R.id.select_date_text);
        amountEt = (EditText)findViewById(R.id.expense_amount);

        //expense_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        accounts_list = databaseHelper.getAllAccounts();    // Get all accounts from DB

        findViewById(R.id.select_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showAllAccounts();

                startActivityForResult(new Intent(AddExpense.this, SelectAccount.class),1);

            }
        });


        categories_list = databaseHelper.getAllCategories();

        findViewById(R.id.select_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showAllCategories();
                startActivityForResult(new Intent(AddExpense.this, SelectCategory.class),2);

            }
        });

        expense_year = Calendar.getInstance().get(Calendar.YEAR);
        expense_month = Calendar.getInstance().get(Calendar.MONTH);
        expense_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

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

                if (areFieldsValid()){

                    try
                    {

                        ExpenseModel expense = new ExpenseModel();
                        expense.setAccount_id(account_id);
                        expense.setAmount(Float.parseFloat(amountEt.getText().toString()));
                        expense.setCategory_id(category_id);
                        expense.setCurrency_id(1);
                        expense.setDesc(descEt.getText().toString());
                        /*String date = expense_day+"/"+(expense_month + 1)+"/"+expense_year;
                        expense.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));*/

                        Log.d("Day of expense",""+expense_day);
                        Log.d("Year of expense",""+expense_year);

                        expense.setDay(expense_day);
                        expense.setMonth(expense_month+1);
                        expense.setYear(expense_year);

                        if(databaseHelper.addExpense(expense))
                            finish();   // close page if expense is saved
                        else
                            Snackbar.make(coordinatorLayout,"Something went wrong!",Snackbar.LENGTH_LONG).show();

                        databaseHelper.close();

                        Log.d("Expense Added - "," Date: "+expense.getDate());

                    }catch (Exception e){
                        e.printStackTrace();
                    }


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult"," called ....");

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK)
            {

                account_id = data.getIntExtra("account_id",0);
                account_name = data.getStringExtra("account_name");
                accountTv.setText(account_name);

            }
        }

        if (requestCode == 2){

            if (resultCode == Activity.RESULT_OK){


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

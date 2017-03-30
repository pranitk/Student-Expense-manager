package com.pranitkulkarni.expensemanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.pranitkulkarni.expensemanager.bank_accounts.AccountModel;
import com.pranitkulkarni.expensemanager.bank_accounts.AdapterAccounts;
import com.pranitkulkarni.expensemanager.bank_accounts.AddAccount;
import com.pranitkulkarni.expensemanager.database.DatabaseHelper;
import com.pranitkulkarni.expensemanager.expense.AdapterExpenses;
import com.pranitkulkarni.expensemanager.expense.AddExpense;
import com.pranitkulkarni.expensemanager.expense.ExpenseCategoryModel;
import com.pranitkulkarni.expensemanager.expense.ExpenseModel;
import com.pranitkulkarni.expensemanager.transactions.AddIncome;
import com.pranitkulkarni.expensemanager.transactions.AddTransfer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.Open_regular)).build());

        Fab fab = (Fab)findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);

        new MaterialSheetFab(fab,sheetView,overlay, Color.parseColor("#FFFFFF"),ContextCompat.getColor(this,R.color.colorAccent));

        // Intitalize material drawer
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
        new DrawerBuilder().withActivity(this).withToolbar(toolbar).addDrawerItems(item1).build();


        databaseHelper = new DatabaseHelper(this);



        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.d("Available currencies - ",Currency.getAvailableCurrencies().toString());
        }*/



        /*findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,AddAccount.class));

            }
        });*/


        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);


        if (!sharedPref.getBoolean(getString(R.string.pref_create_categories),false))
            new AddCategories().execute();
        else
            Log.d("Categories"," already created...");


        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("default_currency_code",Currency.getInstance(Locale.getDefault()).getCurrencyCode());
        editor.putString("default_currency_symbol",Currency.getInstance(Locale.getDefault()).getSymbol(Locale.getDefault()));
        editor.apply();

        Log.d("Local Currency code - ",Currency.getInstance(Locale.getDefault()).getCurrencyCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.d("Local Currency Name - ",Currency.getInstance(Locale.getDefault()).getDisplayName());
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new FragmentHome()).commit();


        findViewById(R.id.add_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,AddAccount.class));



            }
        });

        findViewById(R.id.add_expense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,AddExpense.class));
            }
        });

        findViewById(R.id.add_income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,AddIncome.class));
            }
        });

        findViewById(R.id.add_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,AddTransfer.class));
            }
        });


    }



    @Override
    public void onResume() {
        super.onResume();


        final List<AccountModel> accounts = databaseHelper.getAllAccounts();

        for (int i=0; i < accounts.size(); i++){

            AccountModel account = accounts.get(i);
            Log.d("Acc "+i," Name - "+account.getName());
            Log.d("Acc "+i," Balance - "+account.getBalance());
            Log.d("Acc "+i," Type - "+account.getType());

        }


        /*

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterAccounts = new AdapterAccounts(this,accounts);
        recyclerView.setAdapter(adapterAccounts);


        new GetAllExpenses().execute();


        */

       // adapterAccounts.notifyDataSetChanged();
       // recyclerView.setAdapter(adapterAccounts);

    }

    private class GetAllExpenses extends AsyncTask<Void,Void,Boolean>{

        private List<ExpenseModel> list = new ArrayList<>();

        @Override
        protected Boolean doInBackground(Void... params) {

            try{


                list = databaseHelper.getAllExpenses();

                Log.d("Expenses list"," size = "+list.size());


            }catch (Exception e){
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean){

                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewExpenses);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(new AdapterExpenses(MainActivity.this,list));

            }

        }
    }

    public class AddCategories extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean saved = false;

            try{

                Log.d("Adding"," categories.....");

                String[] names,icons;
                List<ExpenseCategoryModel> list = new ArrayList<>();

                // get arrays from the app resources
                names = getResources().getStringArray(R.array.categories);
                icons = getResources().getStringArray(R.array.categories_icons);


                for (int i=0; i < names.length; i++)
                {
                    ExpenseCategoryModel model = new ExpenseCategoryModel();
                    model.setName(names[i]);
                    model.setIcon(icons[i]);
                    list.add(model);
                }

                if (list.size() > 0)
                    saved = new DatabaseHelper(MainActivity.this).addExpenseCategories(list);   // Add the list to database table


            }
            catch (Exception e){
                e.printStackTrace();
                saved = false;
            }

            return saved;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean){


                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putBoolean(getString(R.string.pref_create_categories),true);
                editor.apply();

            }

        }
    }

    @Override
    protected void attachBaseContext(Context tp) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(tp));
    }
}

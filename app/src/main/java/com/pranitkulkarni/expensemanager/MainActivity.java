package com.pranitkulkarni.expensemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;

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

        //CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().build());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.Open_regular)).build());

        Fab fab = (Fab)findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);

        new MaterialSheetFab(fab,sheetView,overlay, Color.parseColor("#FFFFFF"),ContextCompat.getColor(this,R.color.colorAccent));

        // Intitalize material drawer
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
        new DrawerBuilder().withActivity(this).withToolbar(toolbar).addDrawerItems(item1).build();


        databaseHelper = new DatabaseHelper(this);

        /*findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,AddAccount.class));

            }
        });*/

        final List<AccountModel> accounts = databaseHelper.getAllAccounts();

        for (int i=0; i < accounts.size(); i++){

            AccountModel account = accounts.get(i);
            Log.d("Acc "+i," Name - "+account.getName());
            Log.d("Acc "+i," Balance - "+account.getBalance());
            Log.d("Acc "+i," Type - "+account.getType());

        }

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);


        if (!sharedPref.getBoolean(getString(R.string.pref_create_categories),false))
            new AddCategories().execute();
        else
            Log.d("Categories"," already created...");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterAccounts(this,accounts));


        new GetAllExpenses().execute();

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

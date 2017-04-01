package com.pranitkulkarni.expensemanager.bank_accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.database.DatabaseHelper;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAllAccounts extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_accounts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().build());

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getAccounts();


        Log.d("Currency symbol","- "+getPreferences(Context.MODE_PRIVATE).getString("default_currency_symbol",""));

        findViewById(R.id.add_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(ShowAllAccounts.this,AddAccount.class),1);

            }
        });



    }


    private void getAccounts(){

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        final List<AccountModel> accounts = databaseHelper.getAllAccounts();

        recyclerView.setAdapter(new AdapterAccounts(this,accounts));

        databaseHelper.close();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult", " called ....");

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK)
                getAccounts();

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context tp) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(tp));
    }

}

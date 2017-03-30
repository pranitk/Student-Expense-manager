package com.pranitkulkarni.expensemanager.currency;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pranitkulkarni.expensemanager.R;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectCurrency extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_currency);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.Open_regular)).build());


        List<CurrencyModel> list = new ArrayList<>();

        Set<Currency> currencies = Currency.getAvailableCurrencies();

        for (Iterator<Currency> iterator = currencies.iterator(); iterator.hasNext();){

            final Currency currency = iterator.next();
            CurrencyModel myCurrencyModel = new CurrencyModel();
            myCurrencyModel.setCode(currency.getCurrencyCode());
            myCurrencyModel.setName(currency.getDisplayName());
            myCurrencyModel.setSymbol(currency.getSymbol());

            list.add(myCurrencyModel);

        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectCurrency.this));
        recyclerView.setAdapter(new AdapterCurrency(SelectCurrency.this,list));


        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }


    @Override
    protected void attachBaseContext(Context tp) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(tp));
    }
}

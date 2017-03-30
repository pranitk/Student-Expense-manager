package com.pranitkulkarni.expensemanager;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.bank_accounts.ShowAllAccounts;
import com.pranitkulkarni.expensemanager.database.DatabaseHelper;
import com.pranitkulkarni.expensemanager.expense.AdapterExpenses;
import com.pranitkulkarni.expensemanager.expense.ExpenseModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {


    TextView monthName,monnthlyIncome,monthlyExpense,totalBalance;
    RecyclerView recyclerViewExpenses;
    DatabaseHelper databaseHelper;
    String default_currency_symbol = "";
    //Float monthly_income = 0.0;

    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        default_currency_symbol = sharedPreferences.getString("default_currency_symbol","");

        monthName = (TextView)view.findViewById(R.id.month_name);
        monthlyExpense = (TextView)view.findViewById(R.id.expense_amount);
        totalBalance = (TextView)view.findViewById(R.id.total_balance);
        monnthlyIncome = (TextView)view.findViewById(R.id.income_amount);
        recyclerViewExpenses = (RecyclerView)view.findViewById(R.id.recyclerViewExpenses);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseHelper = new DatabaseHelper(getActivity());

        new GetAllExpenses().execute();

        String monthInWords = Calendar.getInstance().getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.US);
        monthName.setText(monthInWords);

        //String currencySymbol = ;

        monthlyExpense.setText(default_currency_symbol + databaseHelper.getCurrentMonthExpense());
        totalBalance.setText(default_currency_symbol + databaseHelper.getTotalBalance());
        monnthlyIncome.setText(default_currency_symbol + databaseHelper.getCurrentMonthIncome());

        view.findViewById(R.id.show_all_accounts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ShowAllAccounts.class));

            }
        });


        return view;
    }


    private class GetAllExpenses extends AsyncTask<Void,Void,Boolean> {

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

                recyclerViewExpenses.setAdapter(new AdapterExpenses(getActivity(),list));

            }

        }
    }

}

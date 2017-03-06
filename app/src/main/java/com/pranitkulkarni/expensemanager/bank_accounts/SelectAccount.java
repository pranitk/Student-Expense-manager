package com.pranitkulkarni.expensemanager.bank_accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.database.DatabaseHelper;
import com.pranitkulkarni.expensemanager.expense.AddExpense;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectAccount extends AppCompatActivity {


    int account_id = 0;
    String account_name = "";
    //RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().build());

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        DatabaseHelper databaseHelper = new DatabaseHelper(SelectAccount.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(SelectAccount.this));
        recyclerView.setAdapter(new AdapterSelectAccount(SelectAccount.this,databaseHelper.getAllAccounts()));
    }

    class AdapterSelectAccount extends RecyclerView.Adapter<AdapterSelectAccount.myViewHolder>{

        private List<AccountModel> list;
        private Context context;

        public AdapterSelectAccount(Context context, List<AccountModel> list){
            this.list = list;
            this.context = context;
        }

        @Override
        public AdapterSelectAccount.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AdapterSelectAccount.myViewHolder(LayoutInflater.from(context).inflate(R.layout.select_single_item,parent,false));
        }

        @Override
        public void onBindViewHolder(AdapterSelectAccount.myViewHolder holder, int position) {

            final AccountModel account = list.get(position);
            holder.nameTv.setText(account.getName());

            String icon = "";

            if (account.getType() == 1) // CASH
                icon = context.getString(R.string.icon_cash);
            else if (account.getType() == 2) //  CARD
                icon = context.getString(R.string.icon_card);

            holder.iconTV.setText(icon);



        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class myViewHolder extends RecyclerView.ViewHolder{

            TextView nameTv,iconTV;

            public myViewHolder(View itemView) {
                super(itemView);
                nameTv = (TextView)itemView.findViewById(R.id.text);
                iconTV = (TextView)itemView.findViewById(R.id.icon);

                itemView.findViewById(R.id.click_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AccountModel account = list.get(getAdapterPosition());

                        account_id = account.getId();
                        account_name = account.getName();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("account_id",account_id);
                        returnIntent.putExtra("account_name",account_name);
                        //returnIntent.putExtra("account_icon",account.ge)
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();

                    }
                });

            }
        }
    }


    @Override
    protected void attachBaseContext(Context tp) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(tp));
    }
}

package com.pranitkulkarni.expensemanager.bank_accounts;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.R;

import java.util.List;

/**
 * Created by pranitkulkarni on 2/14/17.
 */

public class AdapterAccounts extends RecyclerView.Adapter<AdapterAccounts.myViewHolder>{

    private List<AccountModel> list;
    private Context context;

    public AdapterAccounts(Context context, List<AccountModel> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.accounts_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        final AccountModel account = list.get(position);
        holder.nameTv.setText(account.getName());
        holder.balanceTv.setText(String.valueOf(account.getBalance()));

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

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView nameTv,balanceTv,iconTV;

        public myViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView)itemView.findViewById(R.id.account_name);
            balanceTv = (TextView)itemView.findViewById(R.id.balance);
            iconTV = (TextView)itemView.findViewById(R.id.account_type_icon);
        }
    }
}

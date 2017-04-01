package com.pranitkulkarni.expensemanager.bank_accounts;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.account_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        final AccountModel account = list.get(position);
        holder.nameTv.setText(account.getName());


        final String balance = context.getSharedPreferences(context.getString(R.string.pref_expenses),Context.MODE_PRIVATE).getString("default_currency_symbol","")
                            + " "+ String.valueOf(account.getBalance());

        holder.balanceTv.setText(balance);

        String icon;
        int color1,color2;

        if (account.getType() == 1) { // CASH
            icon = context.getString(R.string.icon_cash);
            color1 = R.color.green_500;
            color2 = R.color.green_300;

        }else if (account.getType() == 2) { //  CARD
            icon = context.getString(R.string.icon_card);

            color1 = R.color.purple_500;
            color2 = R.color.purple_300;

        }else {
            icon = context.getString(R.string.icon_bank);

            color1 = R.color.orange_700;
            color2 = R.color.orange_400;
        }

        holder.card.setCardBackgroundColor(ContextCompat.getColor(context,color1));
        holder.band.setBackgroundColor(ContextCompat.getColor(context,color2));
        holder.iconTV.setText(icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView nameTv,balanceTv,iconTV;
        View band;
        CardView card;

        public myViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView)itemView.findViewById(R.id.account_name);
            balanceTv = (TextView)itemView.findViewById(R.id.balance);
            iconTV = (TextView)itemView.findViewById(R.id.account_type_icon);
            card = (CardView) itemView.findViewById(R.id.click_layout);
            band = itemView.findViewById(R.id.band);
        }
    }
}

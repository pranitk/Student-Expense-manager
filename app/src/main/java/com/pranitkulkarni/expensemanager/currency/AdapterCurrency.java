package com.pranitkulkarni.expensemanager.currency;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.bank_accounts.AdapterAccounts;

import java.util.List;

/**
 * Created by pranitkulkarni on 3/28/17.
 */

public class AdapterCurrency extends RecyclerView.Adapter<AdapterCurrency.myViewHolder> {

    private Context context;
    private List<CurrencyModel> list;


    public AdapterCurrency(Context context,List<CurrencyModel> list){
        this.context = context;
        this.list = list;
    }


    @Override
    public AdapterCurrency.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterCurrency.myViewHolder(LayoutInflater.from(context).inflate(R.layout.currency_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(AdapterCurrency.myViewHolder holder, int position) {

        final CurrencyModel model = list.get(position);

        holder.symbolTv.setText(model.getSymbol());
        holder.codeTv.setText(model.getCode());
        holder.nameTv.setText(model.getName());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView nameTv,symbolTv,codeTv;

        public myViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView)itemView.findViewById(R.id.name);
            symbolTv = (TextView)itemView.findViewById(R.id.symbol);
            codeTv = (TextView)itemView.findViewById(R.id.code);
        }
    }
}

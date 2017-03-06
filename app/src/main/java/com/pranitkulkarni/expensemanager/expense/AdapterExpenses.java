package com.pranitkulkarni.expensemanager.expense;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.R;

import java.util.List;

/**
 * Created by pranitkulkarni on 3/5/17.
 */

public class AdapterExpenses  extends RecyclerView.Adapter<AdapterExpenses.myViewHolder>{

    private List<ExpenseModel> list;
    private Context context;

    public AdapterExpenses(Context context,List<ExpenseModel> list){
        this.list = list;
        this.context = context;
    }


    @Override
    public AdapterExpenses.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterExpenses.myViewHolder(LayoutInflater.from(context).inflate(R.layout.expenses_list_item,parent,false));
    }


    @Override
    public void onBindViewHolder(AdapterExpenses.myViewHolder holder, int position) {

        final ExpenseModel expense = list.get(position);
        holder.description.setText(expense.getDesc());
        holder.amount.setText(String.valueOf(expense.getAmount()));
        //holder.categoryName.setText(expense.ge);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView categoryIcon,description,amount,currency,categoryName;

        public myViewHolder(View itemView) {

            super(itemView);
            amount = (TextView)itemView.findViewById(R.id.amount);
            currency = (TextView)itemView.findViewById(R.id.currency);
            categoryIcon = (TextView)itemView.findViewById(R.id.category_icon);
            categoryName = (TextView)itemView.findViewById(R.id.category_name);
            description = (TextView)itemView.findViewById(R.id.expense_desc);

        }
    }
}

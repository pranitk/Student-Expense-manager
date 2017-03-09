package com.pranitkulkarni.expensemanager.expense;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.bank_accounts.AccountModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by pranitkulkarni on 3/5/17.
 */

public class AdapterExpenses  extends RecyclerView.Adapter<AdapterExpenses.myViewHolder>{

    private List<ExpenseModel> list;
    private Context context;

    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
    private SimpleDateFormat systemFormat = new SimpleDateFormat("MM");

    public AdapterExpenses(Context context,List<ExpenseModel> list){
        this.list = list;
        this.context = context;
    }


    @Override
    public AdapterExpenses.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterExpenses.myViewHolder(LayoutInflater.from(context).inflate(R.layout.expenses_list_item_2,parent,false));
    }


    @Override
    public void onBindViewHolder(AdapterExpenses.myViewHolder holder, int position) {

        final ExpenseModel expense = list.get(position);
        holder.description.setText(expense.getDesc());
        holder.amount.setText(String.valueOf(expense.getAmount()));


        //holder.day.setText(String.valueOf(expense.getDay()));
        //holder.year.setText(String.valueOf(expense.getYear()));

        try {

            String monthInWords = monthFormat.format(systemFormat.parseObject(String.valueOf(expense.getMonth())));
            String day = String.valueOf(expense.getDay());
            holder.date.setText(day+" "+monthInWords);
            //holder.month.setText(monthInWords);

        }catch (ParseException p){
            p.printStackTrace();
        }

        final AccountModel accountDetails = expense.getAccountModel();

        String icon = context.getString(R.string.icon_card);

        if (accountDetails.getType() == 1)
            icon = context.getString(R.string.icon_cash);

        holder.accountIcon.setText(icon);
        holder.accountName.setText(accountDetails.getName());

        final ExpenseCategoryModel categoryDetails = expense.getExpenseCategoryModel();

        holder.categoryName.setText(categoryDetails.getName());
        holder.categoryIcon.setText(categoryDetails.getIcon());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView categoryIcon,description,amount,currency,categoryName,date;//day,month,year;
        TextView accountName,accountIcon;

        public myViewHolder(View itemView) {

            super(itemView);
            amount = (TextView)itemView.findViewById(R.id.amount);
            currency = (TextView)itemView.findViewById(R.id.currency);
            categoryIcon = (TextView)itemView.findViewById(R.id.category_icon);
            categoryName = (TextView)itemView.findViewById(R.id.category_name);
            description = (TextView)itemView.findViewById(R.id.expense_desc);
            date = (TextView)itemView.findViewById(R.id.date);

            accountIcon = (TextView)itemView.findViewById(R.id.account_type_icon);
            accountName = (TextView)itemView.findViewById(R.id.account_name);
            /*
            day = (TextView)itemView.findViewById(R.id.day);
            month = (TextView)itemView.findViewById(R.id.month);
            year = (TextView)itemView.findViewById(R.id.year);*/

        }
    }
}

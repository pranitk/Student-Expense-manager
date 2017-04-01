package com.pranitkulkarni.expensemanager.expense;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.bank_accounts.AccountModel;

import java.io.Serializable;
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
    private String currencySymbol = "";


    public AdapterExpenses(Context context,List<ExpenseModel> list){
        this.list = list;
        this.context = context;
        this.currencySymbol = context.getSharedPreferences(context.getString(R.string.pref_expenses),Context.MODE_PRIVATE)
                .getString("default_currency_symbol","");
    }


    @Override
    public AdapterExpenses.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterExpenses.myViewHolder(LayoutInflater.from(context).inflate(R.layout.expenses_list_item_2,parent,false));
    }


    @Override
    public void onBindViewHolder(AdapterExpenses.myViewHolder holder, int position) {

        final ExpenseModel expense = list.get(position);
        holder.description.setText(expense.getDesc());
        holder.currency.setText(currencySymbol);
        holder.amount.setText(String.valueOf(expense.getAmount()));


        //holder.day.setText(String.valueOf(expense.getDay()));
        //holder.year.setText(String.valueOf(expense.getYear()));

        try {

            String monthInWords = monthFormat.format(systemFormat.parseObject(String.valueOf(expense.getMonth()+1)));
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

        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(currencySymbol+" "+expense.getAmount());
                View body = LayoutInflater.from(context).inflate(R.layout.edit_and_delete,null);
                builder.setView(body);

                body.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context,AddExpense.class);
                        intent.putExtra("isEdit",true);
                        intent.putExtra("id",expense.getId());

                        intent.putExtra("amount",String.valueOf(expense.getAmount()));
                        intent.putExtra("amount_value",expense.getAmount());
                        intent.putExtra("category_id",expense.getCategory_id());
                        intent.putExtra("category_name",categoryDetails.getName());
                        intent.putExtra("category_icon",categoryDetails.getIcon());

                        intent.putExtra("account_id",expense.getAccount_id());
                        intent.putExtra("account_name",accountDetails.getName());

                        intent.putExtra("description",expense.getDesc());
                        intent.putExtra("year",expense.getYear());
                        intent.putExtra("month",expense.getMonth());
                        intent.putExtra("day",expense.getDay());
                        ///intent.putExtra("expense_model",expense);
                        context.startActivity(intent);

                    }
                });

                body.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                builder.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView categoryIcon,description,amount,currency,categoryName,date;//day,month,year;
        TextView accountName,accountIcon;
        View clickLayout;

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

            clickLayout = itemView.findViewById(R.id.click_layout);
            /*
            day = (TextView)itemView.findViewById(R.id.day);
            month = (TextView)itemView.findViewById(R.id.month);
            year = (TextView)itemView.findViewById(R.id.year);*/

        }
    }
}

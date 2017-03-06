package com.pranitkulkarni.expensemanager.expense;

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
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.bank_accounts.SelectAccount;
import com.pranitkulkarni.expensemanager.database.DatabaseHelper;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectCategory extends AppCompatActivity {

    //int category_id = 0;
   // String category_name = "",icon="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().build());

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        DatabaseHelper databaseHelper = new DatabaseHelper(SelectCategory.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(SelectCategory.this));
        recyclerView.setAdapter(new AdapterSelectExpenseCategory(databaseHelper.getAllCategories()));

    }

    class AdapterSelectExpenseCategory extends RecyclerView.Adapter<AdapterSelectExpenseCategory.myViewHolder>{

        private List<ExpenseCategoryModel> list;
        private Context context;

        public AdapterSelectExpenseCategory(List<ExpenseCategoryModel> list){
            this.list = list;
        }

        @Override
        public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AdapterSelectExpenseCategory.myViewHolder(LayoutInflater.from(SelectCategory.this).inflate(R.layout.select_single_item,parent,false));
        }

        @Override
        public void onBindViewHolder(myViewHolder holder, int position) {

            final ExpenseCategoryModel model = list.get(position);

            holder.text.setText(model.getName());
            holder.icon.setText(model.getIcon());


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class myViewHolder extends RecyclerView.ViewHolder{

            TextView text,icon;

            public myViewHolder(View itemView) {
                super(itemView);

                text = (TextView)itemView.findViewById(R.id.text);
                icon = (TextView)itemView.findViewById(R.id.icon);

                itemView.findViewById(R.id.click_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final ExpenseCategoryModel model = list.get(getAdapterPosition());

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("category_id",model.getId());
                        returnIntent.putExtra("category_icon",model.getIcon());
                        returnIntent.putExtra("category_name",model.getName());
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

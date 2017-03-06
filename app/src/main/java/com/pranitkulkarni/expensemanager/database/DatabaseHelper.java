package com.pranitkulkarni.expensemanager.database;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pranitkulkarni.expensemanager.R;
import com.pranitkulkarni.expensemanager.expense.ExpenseCategoryModel;
import com.pranitkulkarni.expensemanager.expense.ExpenseModel;
import com.pranitkulkarni.expensemanager.bank_accounts.AccountModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranitkulkarni on 2/11/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private Context context;

    // Database Name
    private static final String DATABASE_NAME = "p_expense_manager.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + DatabaseInfo.Accounts.TABLE_NAME + "(" + DatabaseInfo.Accounts.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseInfo.Accounts.BALANCE + " FLOAT NOT NULL, " + DatabaseInfo.Accounts.NAME + " TEXT, "+ DatabaseInfo.Accounts.TYPE+" INTEGER)";

        db.execSQL(CREATE_ACCOUNTS_TABLE);

        String CREATE_EXPENSE_CATEGORY_TABLE = "CREATE TABLE " + DatabaseInfo.ExpenseCategory.TABLE_NAME + "(" + DatabaseInfo.ExpenseCategory.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseInfo.ExpenseCategory.NAME + " TEXT,"+ DatabaseInfo.ExpenseCategory.ICON+" TEXT )";

        db.execSQL(CREATE_EXPENSE_CATEGORY_TABLE);

        String CREATE_EXPENSE_TABLE = "CREATE TABLE " + DatabaseInfo.Expenses.TABLE_NAME + "(" + DatabaseInfo.Expenses.ID + " INTEGER PRIMARY KEY,"
                + DatabaseInfo.Expenses.AMOUNT + " FLOAT NOT NULL, "+ DatabaseInfo.Expenses.CATEGORY + " INTEGER, " + DatabaseInfo.Expenses.DESCRIPTION
                + " TEXT,"+ DatabaseInfo.Expenses.ACCOUNT_ID +" INTEGER,"+ DatabaseInfo.Expenses.DATE +" DATE, "+
                DatabaseInfo.Expenses.CURRENCY_ID+" INTEGER"+
                ", FOREIGN KEY (" + DatabaseInfo.Expenses.CATEGORY+") "+ "REFERENCES "+ DatabaseInfo.Expenses.TABLE_NAME + "(" + DatabaseInfo.ExpenseCategory.ID + ")"+
                ", FOREIGN KEY (" + DatabaseInfo.Expenses.ACCOUNT_ID+") "+ "REFERENCES "+ DatabaseInfo.Accounts.TABLE_NAME + "(" + DatabaseInfo.Accounts.ID + "))";

        db.execSQL(CREATE_EXPENSE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addAccount(AccountModel model){

        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseInfo.Accounts.BALANCE,model.getBalance());
        contentValues.put(DatabaseInfo.Accounts.NAME,model.getName());
        contentValues.put(DatabaseInfo.Accounts.TYPE,model.getType());

        database.insert(DatabaseInfo.Accounts.TABLE_NAME,null,contentValues);
        database.close();

    }

    public Boolean addExpense(ExpenseModel model){

        Boolean saved = false;

        try {


            SQLiteDatabase database = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseInfo.Expenses.AMOUNT,model.getAmount());
            contentValues.put(DatabaseInfo.Expenses.DESCRIPTION,model.getDesc());
            contentValues.put(DatabaseInfo.Expenses.ACCOUNT_ID,model.getAccount_id());
            contentValues.put(DatabaseInfo.Expenses.CATEGORY,model.getCategory_id());
            contentValues.put(DatabaseInfo.Expenses.DATE,String.valueOf(model.getDate()));
            contentValues.put(DatabaseInfo.Expenses.CURRENCY_ID,model.getCurrency_id());


            if(updateAccountBalance(model.getAccount_id(),model.getAmount())) {  // Update account balance
                database.insert(DatabaseInfo.Expenses.TABLE_NAME, null, contentValues);
                saved = true;
            }
            else
                saved = false;

            database.close();

        }catch (Exception e){
            e.printStackTrace();
            saved = false;
        }


        return saved;
    }

    private Boolean updateAccountBalance(int account_id, float amount){

        try{

            final float existingBalance = getAccount(account_id).getBalance();

            final float updatedBalance = existingBalance - amount;

            Log.d("Existing balance - ",""+existingBalance);
            Log.d("Updated balance - ",""+updatedBalance);

            SQLiteDatabase database = getWritableDatabase();

            String query = "UPDATE "+ DatabaseInfo.Accounts.TABLE_NAME +" SET "+ DatabaseInfo.Accounts.BALANCE +" = "+updatedBalance+ " WHERE " +
                    DatabaseInfo.Accounts.ID + " = " + account_id;

            database.execSQL(query);


        }catch (Exception e){
            e.printStackTrace();

            return false;
        }

        return true;


    }

    public Boolean addExpenseCategories(List<ExpenseCategoryModel> list){

        try {

            SQLiteDatabase database = getWritableDatabase();

            for (int i=0; i < list.size(); i++){

                final ExpenseCategoryModel category = list.get(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseInfo.ExpenseCategory.NAME,category.getName());
                contentValues.put(DatabaseInfo.ExpenseCategory.ICON,category.getIcon());

                database.insert(DatabaseInfo.ExpenseCategory.TABLE_NAME,null,contentValues);

            }

            Log.d("Categories list"," created..");

            database.close();


        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }


    public AccountModel getAccount(int account_id){

        AccountModel model = new AccountModel();

        String query = "SELECT * FROM "+ DatabaseInfo.Accounts.TABLE_NAME+" WHERE "+ DatabaseInfo.Accounts.ID+" = "+account_id;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        if (cursor != null){

            cursor.moveToFirst();

            model.setBalance(cursor.getFloat(cursor.getColumnIndex(DatabaseInfo.Accounts.BALANCE)));
            model.setName(cursor.getString(cursor.getColumnIndex(DatabaseInfo.Accounts.NAME)));
            model.setType(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Accounts.TYPE)));
            model.setId(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Accounts.ID)));


            cursor.close();
        }


        return model;
    }

    public List<AccountModel> getAllAccounts(){

        ArrayList<AccountModel> accounts = new ArrayList<>();

        String query = "SELECT * FROM "+ DatabaseInfo.Accounts.TABLE_NAME;  // Select * from Accounts

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

            AccountModel model = new AccountModel();
            model.setBalance(cursor.getFloat(cursor.getColumnIndex(DatabaseInfo.Accounts.BALANCE)));
            model.setName(cursor.getString(cursor.getColumnIndex(DatabaseInfo.Accounts.NAME)));
            model.setType(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Accounts.TYPE)));
            model.setId(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Accounts.ID)));
            accounts.add(model);

        }

        cursor.close();
        database.close();

        return accounts;

    }

    public List<ExpenseModel> getAllExpenses(){

        List<ExpenseModel> expenses = new ArrayList<>();

        String query = "SELECT * FROM "+ DatabaseInfo.Expenses.TABLE_NAME;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);


        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

            ExpenseModel model = new ExpenseModel();
            model.setAmount(cursor.getFloat(cursor.getColumnIndex(DatabaseInfo.Expenses.AMOUNT)));
            model.setAccount_id(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.ACCOUNT_ID)));
            model.setCategory_id(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.CATEGORY)));
            model.setCurrency_id(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.CURRENCY_ID)));
            model.setDesc(cursor.getString(cursor.getColumnIndex(DatabaseInfo.Expenses.DESCRIPTION)));
            model.setId(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.ID)));
            String date = cursor.getString(cursor.getColumnIndex(DatabaseInfo.Expenses.DATE));

            try {
                model.setDate(new SimpleDateFormat(context.getString(R.string.date_format)).parse(date));
            }
            catch (ParseException p){
                p.printStackTrace();
            }
            expenses.add(model);
        }


        cursor.close();
        database.close();

        return expenses;

    }

    public List<ExpenseCategoryModel> getAllCategories(){

        ArrayList<ExpenseCategoryModel> categories = new ArrayList<>();

        String query = "SELECT * FROM "+ DatabaseInfo.ExpenseCategory.TABLE_NAME;  // Select * from Expense category

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

            ExpenseCategoryModel model = new ExpenseCategoryModel();
            model.setName(cursor.getString(cursor.getColumnIndex(DatabaseInfo.ExpenseCategory.NAME)));
            model.setId(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.ExpenseCategory.ID)));
            model.setIcon(cursor.getString(cursor.getColumnIndex(DatabaseInfo.ExpenseCategory.ICON)));
            categories.add(model);

        }

        cursor.close();
        database.close();

        return categories;

    }

}

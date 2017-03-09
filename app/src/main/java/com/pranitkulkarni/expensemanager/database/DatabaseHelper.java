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
import com.pranitkulkarni.expensemanager.transactions.TransactionModel;

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

    private static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + DatabaseInfo.Accounts.TABLE_NAME + "("
            + DatabaseInfo.Accounts.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseInfo.Accounts.BALANCE + " FLOAT NOT NULL, "
            + DatabaseInfo.Accounts.NAME + " TEXT, "
            + DatabaseInfo.Accounts.TYPE+" INTEGER )";

    private static final String CREATE_EXPENSE_CATEGORY_TABLE = "CREATE TABLE " + DatabaseInfo.ExpenseCategory.TABLE_NAME + "("
            + DatabaseInfo.ExpenseCategory.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseInfo.ExpenseCategory.NAME + " TEXT,"
            + DatabaseInfo.ExpenseCategory.ICON+" TEXT )";

    private static final String CREATE_EXPENSE_TABLE = "CREATE TABLE " + DatabaseInfo.Expenses.TABLE_NAME + "("
            + DatabaseInfo.Expenses.ID + " INTEGER PRIMARY KEY,"
            + DatabaseInfo.Expenses.AMOUNT + " FLOAT NOT NULL, "
            + DatabaseInfo.Expenses.CATEGORY + " INTEGER, "
            + DatabaseInfo.Expenses.DESCRIPTION + " TEXT,"
            + DatabaseInfo.Expenses.ACCOUNT_ID +" INTEGER,"
            + DatabaseInfo.Expenses.DATE +" DATE, "
            + DatabaseInfo.Expenses.DAY + " INTEGER, "
            + DatabaseInfo.Expenses.MONTH + " INTEGER, "
            + DatabaseInfo.Expenses.YEAR + " INTEGER, "
            + DatabaseInfo.Expenses.CURRENCY_ID+" INTEGER" +
            ", FOREIGN KEY (" + DatabaseInfo.Expenses.CATEGORY+") "+ "REFERENCES "+ DatabaseInfo.Expenses.TABLE_NAME + "(" + DatabaseInfo.ExpenseCategory.ID + ")"+
            ", FOREIGN KEY (" + DatabaseInfo.Expenses.ACCOUNT_ID+") "+ "REFERENCES "+ DatabaseInfo.Accounts.TABLE_NAME + "(" + DatabaseInfo.Accounts.ID + "))";



    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + DatabaseInfo.Transactions.TABLE_NAME +"("
            + DatabaseInfo.Transactions.ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseInfo.Transactions.AMOUNT +" FLOAT NOT NULL, "
            + DatabaseInfo.Transactions.CURRENCY_ID + " INTEGER, "
            + DatabaseInfo.Transactions.SENDER_ID + " INTEGER, "
            + DatabaseInfo.Transactions.RECEIVER_ID + " INTEGER, "
            + DatabaseInfo.Transactions.DATE + " DATE, "
            + DatabaseInfo.Transactions.CREATED_AT + " DATE, "
            + DatabaseInfo.Transactions.REPEAT + " BOOLEAN, "
            + DatabaseInfo.Transactions.DESCRIPTION + " TEXT )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ACCOUNTS_TABLE);

        db.execSQL(CREATE_EXPENSE_CATEGORY_TABLE);

        db.execSQL(CREATE_EXPENSE_TABLE);

        db.execSQL(CREATE_TRANSACTIONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion){


            db.execSQL(CREATE_ACCOUNTS_TABLE);

            db.execSQL(CREATE_EXPENSE_CATEGORY_TABLE);

            db.execSQL(CREATE_EXPENSE_TABLE);

            db.execSQL(CREATE_TRANSACTIONS_TABLE);

        }

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

        Boolean saved;

        try {


            SQLiteDatabase database = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseInfo.Expenses.AMOUNT,model.getAmount());
            contentValues.put(DatabaseInfo.Expenses.DESCRIPTION,model.getDesc());
            contentValues.put(DatabaseInfo.Expenses.ACCOUNT_ID,model.getAccount_id());
            contentValues.put(DatabaseInfo.Expenses.CATEGORY,model.getCategory_id());
            //contentValues.put(DatabaseInfo.Expenses.DATE,String.valueOf(model.getDate()));
            contentValues.put(DatabaseInfo.Expenses.DAY,model.getDay());
            contentValues.put(DatabaseInfo.Expenses.MONTH,model.getMonth());
            contentValues.put(DatabaseInfo.Expenses.YEAR,model.getYear());
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


    public Boolean addTransaction(TransactionModel model){

        Boolean saved = false;

        try {

            SQLiteDatabase database = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseInfo.Transactions.AMOUNT,model.getAmount());
            contentValues.put(DatabaseInfo.Transactions.DESCRIPTION,model.getDescription());
            contentValues.put(DatabaseInfo.Transactions.SENDER_ID,model.getSender_id());
            contentValues.put(DatabaseInfo.Transactions.RECEIVER_ID, model.getReceiver_id());
            contentValues.put(DatabaseInfo.Transactions.CURRENCY_ID,model.getCurrency_id());
            contentValues.put(DatabaseInfo.Transactions.DATE,model.getDate_of_transaction().toString());
            contentValues.put(DatabaseInfo.Transactions.CREATED_AT,model.getCreated_at().toString());
            contentValues.put(DatabaseInfo.Transactions.REPEAT,model.getRepeat());



            if (model.getSender_id() != 0)  // SENDER_ID is 0 when its income
                saved = updateAccountBalance(model.getSender_id(),model.getAmount());

            if (model.getReceiver_id() != 0)
                saved = updateAccountBalance(model.getReceiver_id(),(-1)*model.getAmount());


            if (saved)  // Update account balance was successful .. so save the transaction now
                database.insert(DatabaseInfo.Transactions.TABLE_NAME,null,contentValues);
            else
                return false;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;


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

    public ExpenseCategoryModel getExpenseCategory(int category_id){

        ExpenseCategoryModel model = new ExpenseCategoryModel();

        String query = "SELECT * FROM "+ DatabaseInfo.ExpenseCategory.TABLE_NAME + " WHERE " + DatabaseInfo.ExpenseCategory.ID + " = "+category_id;


        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        if (cursor != null){

            cursor.moveToFirst();

            model.setId(category_id);
            model.setIcon(cursor.getString(cursor.getColumnIndex(DatabaseInfo.ExpenseCategory.ICON)));
            model.setName(cursor.getString(cursor.getColumnIndex(DatabaseInfo.ExpenseCategory.NAME)));

            cursor.close();
        }

        return model;

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

            int account_id = cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.ACCOUNT_ID));
            model.setAccount_id(account_id);

            int category_id = cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.CATEGORY));
            model.setCategory_id(category_id);

            model.setCurrency_id(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.CURRENCY_ID)));
            model.setDesc(cursor.getString(cursor.getColumnIndex(DatabaseInfo.Expenses.DESCRIPTION)));
            model.setId(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.ID)));
            model.setDay(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.DAY)));
            model.setMonth(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.MONTH)));
            model.setYear(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Expenses.YEAR)));

            model.setAccountModel(getAccount(account_id));
            model.setExpenseCategoryModel(getExpenseCategory(category_id));

            /*String date = cursor.getString(cursor.getColumnIndex(DatabaseInfo.Expenses.DATE));

            try {
                model.setDate(new SimpleDateFormat(context.getString(R.string.date_format)).parse(date));
            }
            catch (ParseException p){
                p.printStackTrace();
            }*/

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

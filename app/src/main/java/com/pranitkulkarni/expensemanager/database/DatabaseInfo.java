package com.pranitkulkarni.expensemanager.database;

import android.provider.BaseColumns;

/**
 * Created by pranitkulkarni on 2/6/17.
 */

public final class DatabaseInfo {

    private DatabaseInfo(){}

    public static class Accounts implements  BaseColumns{

        public static final String TABLE_NAME = "accounts";
        public static final String ID = "account_id";
        public static final String BALANCE = "balance";
        public static final String NAME = "account_name";
        public static final String TYPE = "type";

    }

    public static class Expenses implements BaseColumns{

        public static final String TABLE_NAME = "expenses";

        public static final String ID = "id";
        public static final String AMOUNT = "amount";
        public static final String DESCRIPTION = "description"; // What's the expense
        public static final String CATEGORY = "category_id";    // Food, Electronics etc
        public static final String ACCOUNT_ID = "account_id";   // Paid by which account
        public static final String DATE = "date_of_expense";
        public static final String CURRENCY_ID = "currency_id";     // currency of expense
        public static final String DAY = "expense_day";
        public static final String MONTH = "expense_month";
        public static final String YEAR = "expense_year";

        public static final String REPEAT = "repeat";
        // Add 'created at' ....

    }

    public static class ExpenseCategory implements BaseColumns{

        public static final String TABLE_NAME = "expense_category";

        public static final String ID = "category_id";
        public static final String NAME = "name";
        public static final String ICON = "icon";

    }

    public static class Transactions implements BaseColumns{

        public static final String TABLE_NAME = "transactions";

        public static final String ID = "transaction_id";
        public static final String SENDER_ID = "sender_id";
        public static final String RECEIVER_ID = "receiver_id";
        public static final String DESCRIPTION = "description";
        public static final String AMOUNT = "amount";
        public static final String CURRENCY_ID = "currency_id";
        //public static final String DATE = "date_of_transaction";
        public static final String CREATED_AT = "created_at";
        public static final String REPEAT = "repeat";
        public static final String DAY = "expense_day";
        public static final String MONTH = "expense_month";
        public static final String YEAR = "expense_year";

        public static final String TYPE = "type";

    }

    public static class Profile implements BaseColumns{

        public static final String TABLE_NAME = "user_profile";

        public static final String USER_NAME = "user_name";
        public static final String IS_STUDENT = "is_student";
        public static final String STUDENT_GOAL = "student_goal";

    }
}

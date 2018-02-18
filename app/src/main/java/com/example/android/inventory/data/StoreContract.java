package com.example.android.inventory.data;

import android.provider.BaseColumns;

public class StoreContract {

    private StoreContract() {
    }


    public static final class ItemsEntry implements BaseColumns {


        public final static String TABLE_NAME = "Items";

        public final static String _ID = BaseColumns._ID;

        public static final String COLUMN_NAME = "product_name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";

        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_EMAIL = "supplier_email";
        public static final String COLUMN_SUPPLIER_NUMBER = "supplier_phone";


        public static final String CREATE_TABLE_STOCK = "CREATE TABLE " +
                ItemsEntry.TABLE_NAME + "(" +
                ItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemsEntry.COLUMN_NAME + " TEXT NOT NULL," +
                ItemsEntry.COLUMN_PRICE + " TEXT NOT NULL," +
                ItemsEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
                ItemsEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
                ItemsEntry.COLUMN_SUPPLIER_NUMBER + " TEXT NOT NULL," +
                ItemsEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL," + ");";


    }
}

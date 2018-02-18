package com.example.android.inventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventory.data.StoreContract.ItemsEntry;

/**
 * Created by manar on 09/02/2018.
 */

public class StoreDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Store.db";

    private static final int DATABASE_VERSION = 1;

    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemsEntry.TABLE_NAME + " ("
                + ItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemsEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ItemsEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + ItemsEntry.COLUMN_QUANTITY + " INTEGER DEFAULT 0, "
                + ItemsEntry.COLUMN_SUPPLIER_NAME + " TEXT, "
                + ItemsEntry.COLUMN_SUPPLIER_EMAIL + " TEXT, "
                + ItemsEntry.COLUMN_SUPPLIER_NUMBER + " TEXT);";


        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

    public void insertItem(StoreItems item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemsEntry.COLUMN_NAME, item.getProductName());
        values.put(ItemsEntry.COLUMN_PRICE, item.getPrice());
        values.put(ItemsEntry.COLUMN_QUANTITY, item.getQuantity());
        values.put(ItemsEntry.COLUMN_SUPPLIER_NAME, item.getSupplierName());
        values.put(ItemsEntry.COLUMN_SUPPLIER_NUMBER, item.getSupplierPhone());
        values.put(ItemsEntry.COLUMN_SUPPLIER_EMAIL, item.getSupplierEmail());
        long id = db.insert(ItemsEntry.TABLE_NAME, null, values);
    }

    public Cursor readItem(long itemId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                ItemsEntry._ID,
                ItemsEntry.COLUMN_NAME,
                ItemsEntry.COLUMN_PRICE,
                ItemsEntry.COLUMN_QUANTITY,
                ItemsEntry.COLUMN_SUPPLIER_NAME,
                ItemsEntry.COLUMN_SUPPLIER_NUMBER,
                ItemsEntry.COLUMN_SUPPLIER_EMAIL,
        };
        String selection = ItemsEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(itemId)};

        Cursor cursor = db.query(
                ItemsEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    public Cursor readAll() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                ItemsEntry._ID,
                ItemsEntry.COLUMN_NAME,
                ItemsEntry.COLUMN_PRICE,
                ItemsEntry.COLUMN_QUANTITY,
                ItemsEntry.COLUMN_SUPPLIER_NAME,
                ItemsEntry.COLUMN_SUPPLIER_NAME,
                ItemsEntry.COLUMN_SUPPLIER_EMAIL,
        };
        Cursor cursor = db.query(
                ItemsEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public void updateItem(long currentItemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemsEntry.COLUMN_QUANTITY, quantity);
        String selection = ItemsEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(currentItemId)};
        db.update(ItemsEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }

    public void sellOneItem(long itemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        int newQuantity = 0;
        if (quantity > 0) {
            newQuantity = quantity - 1;
        }
        ContentValues values = new ContentValues();
        values.put(ItemsEntry.COLUMN_QUANTITY, newQuantity);
        String selection = ItemsEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(itemId)};
        db.update(ItemsEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }

}

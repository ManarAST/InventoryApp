/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventory.data.StoreContract.ItemsEntry;


public class ItemCursorAdapter extends CursorAdapter {

    private final MainActivity activity;


    public ItemCursorAdapter(MainActivity context, Cursor c) {
        super(context, c, 0 /* flags */);
        this.activity = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);

        Button sale = view.findViewById(R.id.button_sale);


        int nameColumnIndex = cursor.getColumnIndex(ItemsEntry.COLUMN_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ItemsEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ItemsEntry.COLUMN_QUANTITY);


        String itemName = cursor.getString(nameColumnIndex);
        String itemPrice = cursor.getString(priceColumnIndex);
        final int itemQuantity = cursor.getInt(quantityColumnIndex);


        nameTextView.setText(itemName);
        priceTextView.setText(itemPrice);
        quantityTextView.setText(String.valueOf(itemQuantity));

        final long id = cursor.getLong(cursor.getColumnIndex(ItemsEntry._ID));


        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickOnSale(id,
                        itemQuantity);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickOnViewItem(id);
            }
        });


    }

}

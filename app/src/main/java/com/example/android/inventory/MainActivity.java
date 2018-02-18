package com.example.android.inventory;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.android.inventory.data.StoreContract.ItemsEntry;
import com.example.android.inventory.data.StoreDbHelper;

public class MainActivity extends AppCompatActivity {


    StoreDbHelper dbHelper;
    ItemCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new StoreDbHelper(this);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProductEditor.class);
                startActivity(intent);
            }
        });


        final ListView itemsListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        itemsListView.setEmptyView(emptyView);

        Cursor cursor = dbHelper.readAll();

        mCursorAdapter = new ItemCursorAdapter(this, cursor);
        itemsListView.setAdapter(mCursorAdapter);


    }

    public void clickOnViewItem(long id) {
        Intent intent = new Intent(this, ProductEditor.class);
        intent.putExtra("itemId", id);
        startActivity(intent);
    }


    private int deleteAllItems() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete(ItemsEntry.TABLE_NAME, null, null);

    }


    public void clickOnSale(long id, int quantity) {
        dbHelper.sellOneItem(id, quantity);
        mCursorAdapter.swapCursor(dbHelper.readAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void deleteAllConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.delete_all_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllItems();

            }

        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.action_delete_all_entries:

                deleteAllConfirmationDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}






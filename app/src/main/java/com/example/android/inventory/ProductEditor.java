package com.example.android.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.inventory.data.StoreContract.ItemsEntry;
import com.example.android.inventory.data.StoreDbHelper;
import com.example.android.inventory.data.StoreItems;

/**
 * Created by manar on 13/02/2018.
 */

public class ProductEditor extends AppCompatActivity {


    private StoreDbHelper dbHelper;
    long currentItemId;

    private boolean mItemHasChanged = false;


    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductQuantity;
    private EditText mSupplierName;
    private EditText mSupplierEmail;
    private EditText mSupplierPhone;

    Button plusQuantity;
    Button minusQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mProductName = findViewById(R.id.edit_product_name);
        mProductPrice = findViewById(R.id.edit_product_price);
        mProductQuantity = findViewById(R.id.edit_product_quantity);

        mSupplierName = findViewById(R.id.edit_supplier_name);
        mSupplierEmail = findViewById(R.id.edit_supplier_email);
        mSupplierPhone = findViewById(R.id.edit_supplier_phone);

        plusQuantity = findViewById(R.id.quantity_plus_btn);
        minusQuantity = findViewById(R.id.quantity_minus_btn);


        dbHelper = new StoreDbHelper(this);

        currentItemId = getIntent().getLongExtra("itemId", 0);

        if (currentItemId == 0) {
            setTitle(getString(R.string.editor_activity_title_new_Item));
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_item));
            addValuesToEditItem(currentItemId);
        }

        plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add1ToQuantity();
                mItemHasChanged = true;
            }
        });

        minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minus1ToQuantity();
                mItemHasChanged = true;
            }
        });


    }

    private void add1ToQuantity() {

        String previousValueString = mProductQuantity.getText().toString();
        int previousValue;

        if (previousValueString.isEmpty()) {
            previousValue = 0;

        } else {
            previousValue = Integer.parseInt(previousValueString);
        }

        mProductQuantity.setText(String.valueOf(previousValue + 1));
    }

    private void minus1ToQuantity() {
        String previousValueString = mProductQuantity.getText().toString();
        int previousValue;

        if (previousValueString.isEmpty()) {
            return;

        } else if (previousValueString.equals("0")) {
            return;

        } else {
            previousValue = Integer.parseInt(previousValueString);
            mProductQuantity.setText(String.valueOf(previousValue - 1));
        }
    }


    private boolean saveItem() {


        boolean isValid = true;
        if (!checkIfValueSet(mProductName, "name")) {
            isValid = false;
        }
        if (!checkIfValueSet(mProductPrice, "price")) {
            isValid = false;
        }
        if (!checkIfValueSet(mProductQuantity, "quantity")) {
            isValid = false;
        }
        if (!checkIfValueSet(mSupplierName, "supplier name")) {
            isValid = false;
        }
        if (!checkIfValueSet(mSupplierEmail, "supplier phone")) {
            isValid = false;
        }
        if (!checkIfValueSet(mSupplierPhone, "supplier email")) {
            isValid = false;
        }

        if (!isValid) {
            return false;
        }
        if (currentItemId == 0) {
            StoreItems item = new StoreItems(
                    mProductName.getText().toString().trim(),
                    mProductPrice.getText().toString().trim(),
                    Integer.parseInt(mProductQuantity.getText().toString().trim()),
                    mSupplierName.getText().toString().trim(),
                    mSupplierPhone.getText().toString().trim(),
                    mSupplierEmail.getText().toString().trim());
            dbHelper.insertItem(item);
        } else {
            int quantity = Integer.parseInt(mProductQuantity.getText().toString().trim());
            dbHelper.updateItem(currentItemId, quantity);
        }
        return true;

    }

    private boolean checkIfValueSet(EditText text, String description) {
        if (TextUtils.isEmpty(text.getText())) {
            text.setError("Missing product " + description);
            return false;
        } else {
            text.setError(null);
            return true;
        }
    }

    private void addValuesToEditItem(long itemId) {
        Cursor cursor = dbHelper.readItem(itemId);
        cursor.moveToFirst();

        mProductName.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.COLUMN_NAME)));
        mProductPrice.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.COLUMN_PRICE)));
        mProductQuantity.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.COLUMN_QUANTITY)));

        mSupplierName.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.COLUMN_SUPPLIER_NAME)));
        mSupplierPhone.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.COLUMN_SUPPLIER_NUMBER)));
        mSupplierEmail.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.COLUMN_SUPPLIER_EMAIL)));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                if (!saveItem()) {
                    return true;
                }
                finish();
                return true;

            case R.id.action_delete:
                deleteItemConfirmationDialog();
                return true;

            case R.id.action_order:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mSupplierPhone.getText().toString().trim()));
                startActivity(intent);
                return true;

            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(ProductEditor.this);
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);

        builder.setPositiveButton(R.string.discard, discardButtonClickListener);

        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deleteItemConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem(currentItemId);
                finish();

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

    private int deleteItem(long itemId) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = ItemsEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};
        int rowsDeleted = database.delete(
                ItemsEntry.TABLE_NAME, selection, selectionArgs);
        return rowsDeleted;

    }
}

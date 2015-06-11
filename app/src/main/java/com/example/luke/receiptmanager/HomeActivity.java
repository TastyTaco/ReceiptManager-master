package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends Activity {

    Map<String, ArrayList<Receipt>> receiptsCollection;
    ReceiptManager receiptManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupAddReceiptActivity();

        setupAddCategoriesActivity();


        //Create an instance of receipt manager.
        receiptManager = ReceiptManager.getInstance(getApplicationContext(), this);

        setupExpandingListView();
    }

    void setupAddReceiptActivity() {
        Button btnReceiptView = (Button)findViewById(R.id.button);
        btnReceiptView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddReceipt.class);
                //Pass the categories to the add Receipt activity.
                intent.putExtra("Categories", receiptManager.getCategories());

                startActivityForResult(intent, 0);
            }
        });
    }

    void setupAddCategoriesActivity() {
        Button btnAddCategory = (Button)findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddCategories.class);
                //Pass the categories to the add categories activity.
                intent.putExtra("Categories", receiptManager.getCategories());
                startActivityForResult(intent, 1);
            }
        });
    }

    void setupExpandingListView() {
        //Get the categories to group the receipts by.
        ArrayList<String> groupList = receiptManager.getCategories();

        createCollection(groupList);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        //Create the expandable list view and provide it with receipts.
        ExpandableListView expListView = (ExpandableListView)findViewById(R.id.elvMainList);
        final ExpandableListAdapter listViewAdapter = new ExpandableListAdapter (this, groupList, receiptsCollection);
        expListView.setAdapter(listViewAdapter);

        expListView.setIndicatorBoundsRelative(width - GetPixelFromDips(80), width - GetPixelFromDips(10));
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void createCollection(ArrayList<String> categories) {
        receiptsCollection = new LinkedHashMap<String, ArrayList<Receipt>>();

        //Group the receipts into the categories.
        for (String category : categories) {
            ArrayList<Receipt> categorisedReceipts = receiptManager.getReceipts(category);
            receiptsCollection.put(category, categorisedReceipts);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == 0) { //Add Receipt
            //Grab the data from the Add Receipt Activity.
            Bundle extras = data.getExtras();
            String title = extras.getString("Title");
            String amountSpent = extras.getString("AmountSpent");
            String category = extras.getString("Category");
            String photo = extras.getString("Photo");

            //Add the new receipt to the receipt manager.
            receiptManager.addReceipt(title,category, photo, amountSpent);
            //Reload the receipt expandable list view.
            setupExpandingListView();

        } else if (requestCode == 1) { //Add Category
            //Get the new category.
            String category = data.getStringExtra("Category");
            if (category != null && category != "") {
                //Add the new category to the receipt view.
                receiptManager.addCategory(category);
                //Refresh the expanding list view.
                setupExpandingListView();
            }
        }
    }

}

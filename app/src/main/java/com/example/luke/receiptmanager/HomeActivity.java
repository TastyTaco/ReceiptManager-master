package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class HomeActivity extends Activity {

    List<String> groupList;
    List<String> childList;
    Map<String, ArrayList<Receipt>> receiptsCollection;
    ReceiptManager receiptManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);



        Button btnReceiptView = (Button)findViewById(R.id.button);
        btnReceiptView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tIntent = new Intent(HomeActivity.this, AddReceipt.class);
                startActivity(tIntent);
            }
        });

        receiptManager = new ReceiptManager(getApplicationContext());
        /*receiptManager.loadReceipts();
        ArrayList<Receipt> receipts = receiptManager.GetReciepts();


        if (receipts != null && receipts.size() > 0) {
            TextView welcome = (TextView)findViewById(R.id.txtWelcome);
            welcome.setText("Title:" + receipts.get(0).Title + " AmountSpent:" + receipts.get(0).AmountSpent + " Category:" + receipts.get(0).Category);
        }*/

        //get category array from strings.xml
        Resources res = getResources();
        String[] categories = res.getStringArray(R.array.listCategories);
        groupList = new ArrayList<String>();
        groupList.add("Petrol");
        groupList.add("Groceries");
        groupList.add("Entertainment");
        groupList.add("Phone bill");
        groupList.add("Take aways");

        createCollection();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void createCollection() {
        receiptsCollection = new LinkedHashMap<String, ArrayList<Receipt>>();
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        receiptManager.loadReceipts();
        receipts = receiptManager.GetReciepts();


        ArrayList<Receipt> petrolReceipts = new ArrayList<>();
        ArrayList<Receipt> groceryReceipts = new ArrayList<>();
        ArrayList<Receipt> entertainmentReceipts = new ArrayList<>();
        ArrayList<Receipt> phoneBillReceipts = new ArrayList<>();
        ArrayList<Receipt> takeAwayReceipts = new ArrayList<>();

        for(Receipt receipt : receipts) {
            if(receipt.Category.equals("Petrol"))
            {
                petrolReceipts.add(receipt);
            }
            else if(receipt.Category.equals("Groceries"))
            {
                groceryReceipts.add(receipt);
            }
            else if(receipt.Category.equals("Entertainment"))
            {
                entertainmentReceipts.add(receipt);
            }
            else if(receipt.Category.equals("Phone bill"))
            {
                phoneBillReceipts.add(receipt);
            }
            else if(receipt.Category.equals("Take aways"))
            {
                takeAwayReceipts.add(receipt);
            }
        }

        receiptsCollection.put("Petrol", petrolReceipts);
        receiptsCollection.put("Groceries", groceryReceipts);
        receiptsCollection.put("Entertainment", entertainmentReceipts);
        receiptsCollection.put("Phone bill", phoneBillReceipts);
        receiptsCollection.put("Take aways", petrolReceipts);
    }

}

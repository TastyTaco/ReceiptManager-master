package com.example.luke.receiptmanager;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Logan Mabbett on 16/05/2015.
 */

public class ReceiptManager {
    private String localStorageRecieptsJSON = "ReceiptManagerJSON";
    private Context context;


    private ArrayList<Receipt> receipts = new ArrayList<Receipt>();
    private ArrayList<String> categories = new ArrayList<String>();

    public ReceiptManager( Context context ) {
        this.context = context;
        load();
    }

    public ArrayList<Receipt> getReciepts() {
        return receipts;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public ArrayList<Receipt> getReceipts(String category) {
        ArrayList<Receipt> categorisedReceipts = new ArrayList<Receipt>();

        for (Receipt receipt : receipts) {
            if (receipt.Category == category)
                categorisedReceipts.add(receipt);
        }

        return categorisedReceipts;
    }

    private void load() {
        try {
            FileInputStream fileInputStream = context.openFileInput(localStorageRecieptsJSON);

            byte tBytes[] = new byte[fileInputStream.available()];
            fileInputStream.read(tBytes, 0, tBytes.length);

            String receiptsStr = new String(tBytes);

            JSONObject root;
            JSONArray jsonCategories = null;
            JSONArray jsonReceipts = null;
            try {
                root = new JSONObject(receiptsStr);
                jsonCategories = root.getJSONArray("Categories");
                jsonReceipts = root.getJSONArray("Receipts");
            } catch (JSONException jsonEx) {

            }
            if (jsonReceipts != null) {
                //Receipt tReceipts[] = new Gson().fromJson(/*receiptsStr*/jsonReceipts.toString(), Receipt[].class);
                receipts = new Gson().fromJson(/*receiptsStr*/jsonReceipts.toString(), new TypeToken<ArrayList<Receipt>>(){}.getType() /*Receipt[].class*/);
                //receipts = new ArrayList<Receipt>();

                //Are you meant to be able to go directrly to an array list from an array???
                //for (Receipt r : tReceipts)
                    //receipts.add(r);
            }

            if (jsonCategories != null) {
                categories = new Gson().fromJson(jsonCategories.toString(), new TypeToken<ArrayList<String>>(){}.getType());
            }

        }catch (FileNotFoundException fnfEx) {

        }catch (IOException ioEx) {

        }

        if (receipts == null)
            receipts = new ArrayList<Receipt>();

        if (categories == null)
            categories = new ArrayList<String>();
    }

    private void save() {
        String categoryStr = new Gson().toJson(categories);
        String receiptsStr = new Gson().toJson(receipts);

        JSONObject root = new JSONObject();
        try {
            root.put("Categories", categoryStr);
            root.put("Receipts", receiptsStr);
        } catch(JSONException jsonEx) {

        }

        try {

            FileOutputStream fileOutputStream = context.openFileOutput(localStorageRecieptsJSON, context.MODE_PRIVATE);
            fileOutputStream.write(/*receiptsStr.getBytes()*/ root.toString().getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException fnfEx) {

        }catch (IOException ioEx) {

        }

    }

    public void addReceipt(String Title, String Category, Uri Photo, String AmountSpent) {
        Receipt receipt = new Receipt(Title, Category, Photo, AmountSpent);
        receipts.add(receipt);
        save();
    }

    public void addCategory(String category) {
        categories.add(category);
        Collections.sort(categories);
        save();
    }

}
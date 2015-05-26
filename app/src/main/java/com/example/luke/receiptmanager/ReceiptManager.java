package com.example.luke.receiptmanager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Logan Mabbett on 16/05/2015.
 */

public class ReceiptManager {
    private String localStorageRecieptsJSON = "ReceiptManagerJSON";
    private Context context;

    private int maxId = 0;

    private ArrayList<Receipt> receipts = new ArrayList<Receipt>();
    private ArrayList<String> categories = new ArrayList<String>();

    private FirebaseWrapper firebaseWrapper;

    public ReceiptManager( Context context ) {
        this.context = context;
        firebaseWrapper = FirebaseWrapper.getInstance(context);
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
            if (receipt.Category.equals(category)) {
                categorisedReceipts.add(receipt);
            }
        }

        return categorisedReceipts;
    }

    private void load() {
        InputStream inputStream = null;
        try {
            inputStream = context.openFileInput(localStorageRecieptsJSON);

        } catch (FileNotFoundException fnfEx) {

        } catch (IOException ioEx) {

        }
        String receiptsStr = "";
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            try {
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
            receiptsStr = stringBuilder.toString();
        }

        JSONObject root;
        String jsonCategories = null;
        String jsonReceipts = null;
        try {
            root = new JSONObject(receiptsStr);
            jsonCategories = root.getString("Categories");
            jsonReceipts = root.getString("Receipts");
        } catch (JSONException jsonEx) {
            jsonEx.printStackTrace();
        }

        if (jsonReceipts != null) {
            receipts = new Gson().fromJson(jsonReceipts.toString(), new TypeToken<ArrayList<Receipt>>() {
            }.getType());

            for (Receipt receipt : receipts) {
                receipt.Id = maxId;
                maxId++;
            }
        }

        if (jsonCategories != null) {
            categories = new Gson().fromJson(jsonCategories.toString(), new TypeToken<ArrayList<String>>() {
            }.getType());
        }


        if (receipts == null)
            receipts = new ArrayList<Receipt>();

        if (categories == null)
            categories = new ArrayList<String>();
    }

    private void save() {
        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new Receipt.ReceiptExclusionStrategy()).create();
        String categoryStr = gson.toJson(categories);
        String receiptsStr = gson.toJson(receipts);

        JSONObject root = new JSONObject();
        try {
            root.put("Categories", categoryStr);
            root.put("Receipts", receiptsStr);
        } catch(JSONException jsonEx) {

        }

        String jsonStr = root.toString();
        firebaseWrapper.saveToFirebase(jsonStr);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(localStorageRecieptsJSON, context.MODE_PRIVATE);
            fileOutputStream.write(jsonStr.getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException fnfEx) {

        }catch (IOException ioEx) {

        }

    }

    public void addReceipt(String title, String category, String photo, String amountSpent) {
        Receipt receipt = new Receipt(maxId, title, category, photo, amountSpent);
        receipts.add(receipt);
        maxId++;
        save();
    }

    public void addCategory(String category) {
        categories.add(category);
        Collections.sort(categories);
        save();
    }

    public void deleteReceipt(int id) {
        if (receipts == null) return;

        for (int ii = 0; ii < receipts.size(); ii++) {
            if (receipts.get(ii).Id == id) {
                receipts.remove(ii);
                break;
            }
        }

        save();
    }

}
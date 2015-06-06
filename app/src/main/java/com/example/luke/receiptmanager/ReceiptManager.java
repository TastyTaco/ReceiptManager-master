package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Logan Mabbett on 16/05/2015.
 */

public class ReceiptManager {
    private Context context;
    private HomeActivity homeActivity;

    private int maxId = 0;

    private ArrayList<Receipt> receipts = new ArrayList<Receipt>();
    private ArrayList<String> categories = new ArrayList<String>();

    private FirebaseWrapper firebaseWrapper;

    private static ReceiptManager receiptManager;

    private ReceiptManager( Context context, HomeActivity homeActivity) {
        this.context = context;
        this.homeActivity = homeActivity;
        firebaseWrapper = FirebaseWrapper.getInstance(context);
        load();
    }

    public static ReceiptManager getInstance(Context context, HomeActivity homeActivity){
        if (receiptManager == null)
            receiptManager = new ReceiptManager(context, homeActivity);

        return receiptManager;
    }

    //May return null;
    public static ReceiptManager getInstance() {
        return receiptManager;
    }

    public ArrayList<Receipt> getReciepts() {
        return receipts;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    //Used when loading from firebase.
    public void addReceipts(ArrayList<Receipt> receipts) {
        if (receipts == null) return;

        this.receipts = receipts;

        if (receipts != null) {
            for (Receipt receipt : receipts) {
                if (receipt.Id > maxId) {
                    maxId = receipt.Id;
                }
            }
        }


        homeActivity.setupExpandingListView();
    }

    //Used when loading from firebase.
    public void addCategories(ArrayList<String> categories) {
        if (categories == null) return;
        this.categories = categories;
        homeActivity.setupExpandingListView();
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
        firebaseWrapper.loadReceipts(this);
        firebaseWrapper.loadCategories(this);
    }

    public void addReceipt(String title, String category, String photo, String amountSpent) {
        //Add the category if it doesn't already exist, bit of a shortcut for being able to add a category from the add receipt page.
        if (!categories.contains(category)){
            categories.add(category);
            firebaseWrapper.saveCategories(categories);
        }

        Receipt receipt = new Receipt(maxId, title, category, photo, amountSpent);

        if (receipts == null)
            receipts = new ArrayList<Receipt>();

        receipts.add(receipt);
        maxId++;
        firebaseWrapper.saveReceipts(receipts);
    }

    public void addCategory(String category) {
        if (categories == null)
            categories = new ArrayList<String>();

        categories.add(category);
        Collections.sort(categories);
        firebaseWrapper.saveCategories(categories);
    }

    public void deleteReceipt(int id) {
        if (receipts == null) return;

        for (int ii = 0; ii < receipts.size(); ii++) {
            if (receipts.get(ii).Id == id) {
                receipts.remove(ii);
                break;
            }
        }

        firebaseWrapper.saveReceipts(receipts);
    }

}
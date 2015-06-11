package com.example.luke.receiptmanager;

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

    //hidden receipt manager.
    private static ReceiptManager receiptManager;

    private ReceiptManager( Context context, HomeActivity homeActivity) {
        this.context = context;
        this.homeActivity = homeActivity;
        firebaseWrapper = FirebaseWrapper.getInstance(context);
        load();
    }

    //Return singleton instance of receipt manager, or create a new one.
    public static ReceiptManager getInstance(Context context, HomeActivity homeActivity){
        if (receiptManager == null)
            receiptManager = new ReceiptManager(context, homeActivity);

        return receiptManager;
    }

    //May return null;
    public static ReceiptManager getInstance() {
        return receiptManager;
    }

    //Return the receipt manager's categories.
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

        //Refresh the expanding list view once the receipts have been loaded from firebase (asyncronously)
        homeActivity.setupExpandingListView();
    }

    //Used when loading from firebase.
    public void addCategories(ArrayList<String> categories) {
        if (categories == null) return;
        this.categories = categories;
        //Refresh the expanding list view once the categories have been loaded from firebase (asyncronously)
        homeActivity.setupExpandingListView();
    }

    public ArrayList<Receipt> getReceipts(String category) {
        ArrayList<Receipt> categorisedReceipts = new ArrayList<Receipt>();

        // return the receipts for a certain category type.
        for (Receipt receipt : receipts) {
            if (receipt.Category.equals(category)) {
                categorisedReceipts.add(receipt);
            }
        }

        return categorisedReceipts;
    }

    //Load all data from firebase.
    private void load() {
        firebaseWrapper.loadReceipts(this);
        firebaseWrapper.loadCategories(this);
    }

    public void addReceipt(String title, String category, String photo, String amountSpent) {
        //Add the category if it doesn't already exist, bit of a shortcut for being able to add a category from the add receipt page.
        if (!categories.contains(category)){
            categories.add(category);
            //Save the categories to firebase.
            firebaseWrapper.saveCategories(categories);
        }

        //Create a new receipt.
        Receipt receipt = new Receipt(maxId, title, category, photo, amountSpent);

        if (receipts == null)
            receipts = new ArrayList<Receipt>();

        receipts.add(receipt);
        //increment the ID, used for deleting.
        maxId++;
        //Save the receipt to firebase.
        firebaseWrapper.saveReceipts(receipts);
    }

    public void addCategory(String category) {
        if (categories == null)
            categories = new ArrayList<String>();

        categories.add(category);
        Collections.sort(categories);
        //Save the category to firebase.
        firebaseWrapper.saveCategories(categories);
    }

    public void deleteReceipt(int id) {
        if (receipts == null) return;

        //Remove the receipt from the collection.
        for (int ii = 0; ii < receipts.size(); ii++) {
            if (receipts.get(ii).Id == id) {
                receipts.remove(ii);
                break;
            }
        }

        //Save the updated receipts array to firebase.
        firebaseWrapper.saveReceipts(receipts);
    }

}
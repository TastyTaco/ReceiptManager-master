package com.example.luke.receiptmanager;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Logan Mabbett on 15/05/2015.
 */
public class FirebaseWrapper {
    //Our firebase location.
    final String firebaseUrl = "https://reciptmanger.firebaseio.com/";

    private static FirebaseWrapper firebaseWrapper;
    //Hidden firebase instance
    private Firebase firebase;

    //Users account.
    private String userId;

    //Constructor to setup singleton instance
    private FirebaseWrapper(Context context) {
        Firebase.setAndroidContext(context);
        firebase = new Firebase(firebaseUrl);
    }

    //Return the instance of the firebase wrapper, or initilise it if it doesn't exist.
    public static FirebaseWrapper getInstance(Context context) {
        if (firebaseWrapper == null)
            firebaseWrapper = new FirebaseWrapper(context);

        return firebaseWrapper;
    }

    public void saveCategories(List<String> categories) {
        Firebase categoriesFileLocation = getCategoriesFileLocation();
        //Overwrite the entire existing array to allow for deleting categories to not confuse firebase's array structure.
        categoriesFileLocation.setValue(categories);
    }

    public void saveReceipts(List<Receipt> receipts) {
        if (userId == null) return;
        Firebase receiptsFileLocation = getReceiptsFileLocation();
        //Overwrite the entire array of receipts to allow for deleting receipts not to confuse firebase's array structure.
        receiptsFileLocation.setValue(new ArrayList<>());

        //Save each receipt to firebase.
        for (Receipt receipt : receipts) {
            Firebase receiptFileLocation = receiptsFileLocation.push();
            Map<String, String> receiptToSave = receipt.save();
            receiptFileLocation.setValue(receiptToSave);
        }
    }

    public void loadReceipts(final ReceiptManager receiptManager) {
        Firebase receiptsFileLocation = getReceiptsFileLocation();

        //Load the Receipts from firebase when the user logs in.
        receiptsFileLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Receipt> receipts = new ArrayList<Receipt>();

                //Load the receipts out of the snapshot.
                for (int ii = 0; ii < snapshot.getChildrenCount(); ii++) {
                    DataSnapshot childSnapshot = snapshot.getChildren().iterator().next();
                    HashMap<String, String> hashReceipt = (HashMap < String, String >)childSnapshot.getValue();
                    Receipt receipt = new Receipt(ii, hashReceipt);
                    receipts.add(receipt);
                }
                //Add the receipts to the receipt manager.
                receiptManager.addReceipts(receipts);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                receiptManager.addReceipts(new ArrayList<Receipt>());
            }

        });
    }

    public void loadCategories(final ReceiptManager receiptManager) {
        Firebase categoriesFileLocation = getCategoriesFileLocation();

        categoriesFileLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                };
                //Load categories from the firebase snapshot.
                ArrayList<String> categories = snapshot.getValue(t);
                receiptManager.addCategories(categories);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                receiptManager.addCategories(new ArrayList<String>());
            }
        });
    }

    //Get the location in firebase for the receipts
    private Firebase getReceiptsFileLocation() {
        Firebase usersFileLocation = getUsersFileLocation();
        return usersFileLocation.child("receipts");
    }

    //Get the location in firebase for the categories
    private Firebase getCategoriesFileLocation() {
        Firebase usersFileLocation = getUsersFileLocation();
        return usersFileLocation.child("categories");
    }

    //Get the location of the users directory.
    private Firebase getUsersFileLocation() {
        return firebase.child("users").child(userId);
    }

    public void setUserId(String userId)
    {
       this.userId = userId;
    }

    //Wrapper for creating a firebase user.
    public void createUser(String emailAddress, String password, Firebase.ValueResultHandler<Map<String, Object>> resultHandler) {
        firebase.createUser(emailAddress, password, resultHandler);
    }

    //Wrapper for logging into firebase.
    public void authWithPassword(String emailAddress, String password, Firebase.AuthResultHandler authResultHandler) {
        firebase.authWithPassword(emailAddress, password, authResultHandler);
    }

    //https://www.firebase.com/docs/android/guide/saving-data.html

    //https://www.firebase.com/docs/android/guide/retrieving-data.html
}

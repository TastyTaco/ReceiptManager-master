package com.example.luke.receiptmanager;

import android.content.Context;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Logan Mabbett on 16/05/2015.
 */

public class ReceiptManager {
    String localStorageFileName = "ReceiptManagerJSON";
    Context context;


    ArrayList<Receipt> receipts = new ArrayList<Receipt>();

    public ReceiptManager( Context context ) {
        this.context = context;
        loadReceipts();
    }

    public ArrayList<Receipt> GetReciepts() {
        return receipts;
    }

    void loadReceipts() {
        try {
            FileInputStream fileInputStream = context.openFileInput(localStorageFileName);

            byte tBytes[] = new byte[fileInputStream.available()];
            fileInputStream.read(tBytes, 0, tBytes.length);

            String receiptsStr = new String(tBytes);

            Receipt tReceipts[] = new Gson().fromJson(receiptsStr, Receipt[].class);
            receipts = new ArrayList<Receipt>();

            //Are you meant to be able to go directrly to an array list from an array???
            for (Receipt r : tReceipts)
                receipts.add(r);

        }catch (FileNotFoundException fnfEx) {

        }catch (IOException ioEx) {

        }
    }

    public void SaveReceipts() {
        String receiptsStr = new Gson().toJson(receipts);
        try {

            FileOutputStream fileOutputStream = context.openFileOutput(localStorageFileName, context.MODE_PRIVATE);
            fileOutputStream.write(receiptsStr.getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException fnfEx) {

        }catch (IOException ioEx) {

        }

    }

    public void AddReceipt(String Title, String Category, File Photo, String AmountSpent) {
        Receipt receipt = new Receipt(Title, Category, Photo, AmountSpent);
        receipts.add(receipt);
    }

}
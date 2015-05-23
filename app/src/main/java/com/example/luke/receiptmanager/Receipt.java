package com.example.luke.receiptmanager;

import android.net.Uri;


/**
 * Created by Logan Mabbett on 16/05/2015.
 */
public class Receipt {
    String Title;
    String Category;
    String AmountSpent;
    Uri Photo;

    public Receipt(String Title, String Category, Uri Photo, String AmountSpent) {
        this.Title = Title;
        this.Category = Category;
        this.Photo = Photo;
        this.AmountSpent = AmountSpent;
    }

}

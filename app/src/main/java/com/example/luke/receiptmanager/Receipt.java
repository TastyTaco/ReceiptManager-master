package com.example.luke.receiptmanager;

import java.io.File;

/**
 * Created by Logan Mabbett on 16/05/2015.
 */
public class Receipt {
    String Title;
    String Category;
    String AmountSpent;
    File Photo;

    public Receipt(String Title, String Category, File Photo, String AmountSpent) {
        this.Title = Title;
        this.Category = Category;
        this.Photo = Photo;
        this.AmountSpent = AmountSpent;
    }

}

package com.example.luke.receiptmanager;

import java.util.HashMap;

/**
 * Created by Logan Mabbett on 16/05/2015.
 */
public class Receipt {
    int Id;
    String Title;
    String Category;
    String AmountSpent;
    String Photo; //Base64 encoded photo

    //Create a receipt.
    public Receipt(int Id, String Title, String Category, String Photo, String AmountSpent) {
        this.Id = Id;
        this.Title = Title;
        this.Category = Category;
        this.Photo = Photo;
        this.AmountSpent = AmountSpent;
    }

    //Create a receipt from the firebase hashmap.
    public Receipt(int Id, HashMap<String, String> values) {
        this.Id = Id;

        this.Title = values.get("title");
        this.Category = values.get("category");
        this.Photo = values.get("photo");
        this.AmountSpent = values.get("amountSpent");
    }

    //Save the receipt to the firebase hasmap.
    public HashMap<String, String> save() {
        HashMap<String, String> mapping = new HashMap<String, String>();
        mapping.put("title", Title);
        mapping.put("category", Category);
        mapping.put("amountSpent", AmountSpent);
        mapping.put("photo", Photo);

        return mapping;
    }
}

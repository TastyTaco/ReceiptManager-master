package com.example.luke.receiptmanager;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by Logan Mabbett on 16/05/2015.
 */
public class Receipt {
    int Id;
    String Title;
    String Category;
    String AmountSpent;
    String Photo; //Base64 encoded photo

    public Receipt(int Id, String Title, String Category, String Photo, String AmountSpent) {
        this.Title = Title;
        this.Category = Category;
        this.Photo = Photo;
        this.AmountSpent = AmountSpent;
    }

    static class ReceiptExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            //Skip serilisation of Receipt.Id
            return (f.getDeclaringClass() == Receipt.class && f.getName().equals("id"));
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

}

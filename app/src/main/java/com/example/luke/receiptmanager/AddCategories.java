package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class AddCategories extends Activity {

    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);

        Bundle extra = getIntent().getExtras();
        categories = extra.getStringArrayList("Categories");

        setupAddButton();
    }

    void setupAddButton() {
        Button btnAddCategory = (Button)findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtCategory = (EditText) findViewById(R.id.txtAddCategory);

                String newCategory = txtCategory.getText().toString();

                if (newCategory == null || newCategory == "") {
                    Toast.makeText(AddCategories.this, "Please enter a category to add", Toast.LENGTH_LONG).show();
                    return;
                }

                if (categories.contains(newCategory)) {
                    Toast.makeText(AddCategories.this, "Category already exists", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("Category", newCategory);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}

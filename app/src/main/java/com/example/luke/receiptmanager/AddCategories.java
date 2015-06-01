package com.example.luke.receiptmanager;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class AddCategories extends AppCompatActivity {

    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extra = getIntent().getExtras();
        categories = extra.getStringArrayList("Categories");

        if (categories == null)
            categories = new ArrayList<String>();

        setupAddButton();
    }

    void setupAddButton() {
        Button btnAddCategory = (Button) findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtCategory = (EditText) findViewById(R.id.txtAddCategory);

                String newCategory = txtCategory.getText().toString();
                TextView validationMessage = (TextView)findViewById(R.id.txtValidationMessage);

                if (newCategory.equals("null") || newCategory.isEmpty()) {
                    validationMessage.setText("Please enter a category");
                    return;
                }

                if (categories.contains(newCategory)) {
                    validationMessage.setText("Category already exists");
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("Category", newCategory);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_categories, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

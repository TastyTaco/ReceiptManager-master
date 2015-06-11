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

import java.util.ArrayList;


public class AddCategories extends AppCompatActivity {

    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);

        //Setup the back button.
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Grab categories from the calling intent.
        Bundle extra = getIntent().getExtras();
        categories = extra.getStringArrayList("Categories");

        //Just in case it wasn't passed in.
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

                //Grab the category out of the text box.
                String newCategory = txtCategory.getText().toString();
                TextView validationMessage = (TextView)findViewById(R.id.txtValidationMessage);

                //Give a message if no category is selected.
                if (newCategory.equals("null") || newCategory.isEmpty()) {
                    validationMessage.setText("Please enter a category");
                    return;
                }

                //Check to make sure the category doesn't already exist.
                if (categories.contains(newCategory)) {
                    validationMessage.setText("Category already exists");
                    return;
                }

                //Return the new activity.
                Intent intent = new Intent();
                intent.putExtra("Category", newCategory);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            //Return up the call tree when the back button is pressed.
            case R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

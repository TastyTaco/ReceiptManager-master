package com.example.luke.receiptmanager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class AddReceipt extends AppCompatActivity {

    String mPhotoFileName = "";
    File mPhotoFile = null;
    Uri mPhotoFileUri;

    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);

        //Setup back button
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Grab categories passed from the parent intent.
        Bundle extras = getIntent().getExtras();
        categories = extras.getStringArrayList("Categories");

        setupCategorySpinner();
        setup();
    }

    void setup() {
        //Recipt Sumbisson.
        Button btnAddReceipt = (Button)findViewById(R.id.btnAddReceipt);
        btnAddReceipt.setOnClickListener(new addReceiptButton());

        //Selecting Photo
        Button btnPhoto = (Button)findViewById(R.id.btnChooseImage);
        btnPhoto.setOnClickListener(new choosePhotoButton());

        //Adding a new category
        Button btnAddCategory = (Button)findViewById(R.id.btnAddCategoryFromAddReceipt);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch the Add Categories activity, passing in the existing categories.
                Intent intent = new Intent(AddReceipt.this, AddCategories.class);
                intent.putExtra("Categories", categories);

                startActivityForResult(intent, 4);
            }
        });

    }

    void setupCategorySpinner() {
        //Grab category spinner, setup the adapter with the list items.
        Spinner categorySpinner = (Spinner)findViewById(R.id.spnCategory);

        ArrayAdapter<String> categoriesArrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categories);

        categorySpinner.setAdapter(categoriesArrayAdapter);
    }

    class addReceiptButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            EditText txtTitle = (EditText)findViewById(R.id.txtTitle);
            EditText txtAmountSpent = (EditText)findViewById(R.id.txtAmountSpent);
            Spinner spnCategory = (Spinner)findViewById(R.id.spnCategory);

            //Get the data out of the controls.
            String title = txtTitle.getText().toString();
            String amountSpent = txtAmountSpent.getText().toString();
            String category = spnCategory.getSelectedItem().toString();

            //Check that all data is provided.
            if (mPhotoFile != null && !title.equals("null") && !title.isEmpty() && !amountSpent.equals("null")&& !amountSpent.isEmpty() && !category.equals("null") && !category.isEmpty()){
                //Return the new data to the calling category.
                Intent intent = new Intent();
                intent.putExtra("Title", title);
                intent.putExtra("AmountSpent", amountSpent);
                intent.putExtra("Category", category);

                String realFilePath = mPhotoFile.getPath();

                Bitmap userPhotoBitmap = BitmapFactory.decodeFile(realFilePath);

                //Create an image, compress it, and encode it in a 64byte encoded string.
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                userPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream);
                userPhotoBitmap.recycle();
                byte[] byteArray = byteOutputStream.toByteArray();
                String byteImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                intent.putExtra("Photo", byteImage);

                //return the data.
                setResult(RESULT_OK, intent);
                finish();
            }
            else {
                //Report to the user what is preventing them from submitting.
                if(mPhotoFile == null){
                    Toast.makeText(getApplicationContext(), "Please take a photo", Toast.LENGTH_SHORT).show();
                }
                else if(title.equals("null") || title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                }
                else if(category.equals("null") || category.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please choose a category", Toast.LENGTH_SHORT).show();
                }
                else if(amountSpent.equals("null") || amountSpent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    class choosePhotoButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //create a unique
            mPhotoFile = CreateUniqueFile();
            mPhotoFileName = mPhotoFile.getName();

            mPhotoFileUri = Uri.fromFile(mPhotoFile);

            //Launch the camera app for the user to select an image or take a photo.
            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoFileUri);

            startActivityForResult(imageCaptureIntent, 3);
        }
    }

    File CreateUniqueFile() {
        File imageRootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //Create the local storage directory if it dosen't exist.
        File imageStorageDirectory = new File(imageRootPath, "ReceiptManager");
        if (!imageStorageDirectory.exists()) {
            imageStorageDirectory.mkdirs();
        }

        //Create a unique timestamp
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date currTime = new Date();
        String timeStamp = timestampFormat.format(currTime);

        String photoFileName = "IMG_" + timeStamp + ".jpg";

       File photoFile = new File(imageStorageDirectory.getPath() + File.separator + photoFileName);

        return photoFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Camera intent request
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String realFilePath = mPhotoFile.getPath();

                Bitmap userPhotoBitmap = BitmapFactory.decodeFile(realFilePath);

                ImageView imgView = (ImageView)findViewById(R.id.imgReceipt);
                imgView.setImageBitmap(userPhotoBitmap);

            } else {
                Toast.makeText(this, "No photo saved.", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == 4) {
            if (resultCode == RESULT_OK) {
                //Get the newly added category
                Bundle extras = data.getExtras();
                String newCategory = extras.getString("Category");

                //Add it to the categories collection
                categories.add(newCategory);
                Collections.sort(categories);

                //resetup the spinner with the new category.
                setupCategorySpinner();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            //return up the app tree when the home button is pressed.
            case R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

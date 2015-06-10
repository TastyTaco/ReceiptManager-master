package com.example.luke.receiptmanager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
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

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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

                Intent intent = new Intent(AddReceipt.this, AddCategories.class);
                intent.putExtra("Categories", categories);

                startActivityForResult(intent, 4);
            }
        });

    }

    void setupCategorySpinner() {
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


            String title = txtTitle.getText().toString();
            String amountSpent = txtAmountSpent.getText().toString();
            String category = spnCategory.getSelectedItem().toString();

            if (mPhotoFile != null && !title.equals("null") && !title.isEmpty() && !amountSpent.equals("null")&& !amountSpent.isEmpty() && !category.equals("null") && !category.isEmpty()){
                Intent intent = new Intent();
                intent.putExtra("Title", title);
                intent.putExtra("AmountSpent", amountSpent);
                intent.putExtra("Category", category);

                String realFilePath = mPhotoFile.getPath();

                Bitmap userPhotoBitmap = BitmapFactory.decodeFile(realFilePath);

                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                userPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream);
                userPhotoBitmap.recycle();
                byte[] byteArray = byteOutputStream.toByteArray();
                String byteImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                intent.putExtra("Photo", byteImage);

                setResult(RESULT_OK, intent);
                finish();
            }
            else {
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
            mPhotoFile = CreateUniqueFile();
            mPhotoFileName = mPhotoFile.getName();

            mPhotoFileUri = Uri.fromFile(mPhotoFile);

            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoFileUri);

            startActivityForResult(imageCaptureIntent, 3);
        }
    }

    File CreateUniqueFile() {
        File imageRootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File imageStorageDirectory = new File(imageRootPath, "ReceiptManager");
        if (!imageStorageDirectory.exists()) {
            imageStorageDirectory.mkdirs();
        }

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
                Bundle extras = data.getExtras();
                String newCategory = extras.getString("Category");

                categories.add(newCategory);
                Collections.sort(categories);

                setupCategorySpinner();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("mPhotoFileName", mPhotoFileName);
        //savedInstanceState.putParcelable("mPhotoFile", mPhotoFile);
        savedInstanceState.putParcelable("mPhotoFileUri", mPhotoFileUri);
        savedInstanceState.putStringArrayList("Categories", categories);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mPhotoFileName = savedInstanceState.getString("mPhotoFileName");
        //savedInstanceState.putParcelable("mPhotoFile", mPhotoFile);
        mPhotoFileUri = savedInstanceState.getParcelable("mPhotoFileUri");
        categories = savedInstanceState.getStringArrayList("Categories");
        mPhotoFile = new File(mPhotoFileUri.getPath());
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_receipt, menu);
        return true;
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

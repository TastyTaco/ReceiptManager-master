package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
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
import java.util.Date;


public class AddReceipt extends Activity {

    String mPhotoFileName = "";
    File mPhotoFile = null;
    Uri mPhotoFileUri;
    //base64 encoded image file.
    String imageFile;

    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);

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
    }

    void setupCategorySpinner() {
        Spinner categorySpinner = (Spinner)findViewById(R.id.spnCategory);
        //Resources res = getResources();
        //String[] tCategories = res.getStringArray(R.array.listCategories);

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

            if (mPhotoFile != null && title != null && title != "" && amountSpent != null && amountSpent != "" && category != null){
                //ReceiptManager receiptManager = new ReceiptManager(getApplicationContext());
                //receiptManager.addReceipt(title, category, mPhotoFile, amountSpent);


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
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_receipt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}

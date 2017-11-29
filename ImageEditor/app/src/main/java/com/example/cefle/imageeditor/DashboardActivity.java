package com.example.cefle.imageeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;

public class DashboardActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_FROM_GALLERY = 100;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 200;

    private Uri imagePathUri;

    private LinearLayout selectFromGalleryButton;
    private LinearLayout captureImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        findViews();
        attachListeners();
    }

    private void findViews() {
        selectFromGalleryButton = (LinearLayout) findViewById(R.id.btn_select_from_gallery);
        captureImageButton = (LinearLayout) findViewById(R.id.btn_capture_image);
    }

    private void attachListeners() {

        selectFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickerIntent = new Intent(Intent.ACTION_PICK);
                pickerIntent.setType("image/*");
                startActivityForResult(pickerIntent, REQUEST_CODE_SELECT_FROM_GALLERY);
            }
        });

        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(getExternalCacheDir(), String.valueOf(System.currentTimeMillis()) + ".jpg");
                imagePathUri = Uri.fromFile(file);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagePathUri);
                startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {

                // Create the intent to send the image to the ImageEditActivity
                Intent imageEditIntent = new Intent(this, ImageEditActivity.class);

                // Extract the image path Uri from the result Intent
                Uri imagePath = data.getData();

                // Set the new Intent's data to the image path
                imageEditIntent.setData(imagePath);

                // Start the activity
                startActivity(imageEditIntent);
            }
        }

        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {
                // Create the intent to send the image to the ImageEditActivity
                Intent imageEditIntent = new Intent(this, ImageEditActivity.class);

                // Set the new Intent's data to the image path
                imageEditIntent.setData(imagePathUri);

                // Start the activity
                startActivity(imageEditIntent);
            }
        }

    }
}

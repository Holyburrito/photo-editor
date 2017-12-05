package com.example.cefle.imageeditor;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
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

/**
 * Created by Zach Reznicek on 11/10/17.
 */
public class DashboardActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 123;

    /**
     * The Intent requestCode for selecting an image from the gallery
     */
    private static final int REQUEST_CODE_SELECT_FROM_GALLERY = 100;

    /**
     * The Intent requestCode for using the camera to capture an image
     */
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 200;

    /**
     * Holds the Uri for the image captured by the camera
     */
    private Uri imagePathUri;

    /**
     * The LinearLayout associated with the gallery selection "button"
     */
    private LinearLayout selectFromGalleryButton;

    /**
     * The LinearLayout associated with the capture image "button"
     */
    private LinearLayout captureImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

        // Get references to the components
        findViews();

        // Attach listeners to the components
        attachListeners();
    }

    /**
     * Get references to all of the Views and Layout components in the Activity
     */
    private void findViews() {
        selectFromGalleryButton = (LinearLayout) findViewById(R.id.btn_select_from_gallery);
        captureImageButton = (LinearLayout) findViewById(R.id.btn_capture_image);
    }

    /**
     * This method adds OnClickListeners to the LinearLayouts associated with the "buttons"
     * I added click listeners on the Layouts themselves so that the user can click anywhere
     * within the border and the action will fire, instead of adding the same click listener
     * implementation on the ImageButton, and the TextView.
     */
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The user has access to whatever permission they asked for
            }
        }
    }
}

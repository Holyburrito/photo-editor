package com.example.cefle.imageeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DashboardActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_FROM_GALLERY = 100;

    private Button selectFromGalleryButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        // Find all of the layout views
        findViews();

        // Attach click listeners to the buttons
        attachListeners();
    }

    /**
     * Find and cast all of the layout Views for DashboardActivity
     */
    private void findViews() {
        selectFromGalleryButton = (Button) findViewById(R.id.btn_select_from_gallery);
        imageView = (ImageView) findViewById(R.id.iv_image);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                    //
                }
            }
        }
    }

}

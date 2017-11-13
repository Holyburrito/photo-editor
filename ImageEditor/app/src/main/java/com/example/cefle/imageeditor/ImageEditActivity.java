package com.example.cefle.imageeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageEditActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_edit_activity);

        // Find all of the layout views
        findViews();

        Intent intent = getIntent();
        try {
            final Uri imageUri = intent.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            //
        }
    }

    /**
     * Find and cast all of the layout Views for ImageEditActivity
     */
    private void findViews() {
        imageView = (ImageView) findViewById(R.id.iv_image);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {

                    }
                }

                return true;
            }
        });
    }

}

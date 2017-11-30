package com.example.cefle.imageeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageEditActivity extends AppCompatActivity {

    private ImageView imageView;

    private TextView ie_darken;
    private TextView ie_lighten;
    private TextView ie_badblur;

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

        ie_darken = (TextView) findViewById(R.id.ie_darken);
        ie_darken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(BitmapUtil.darken((BitmapDrawable) imageView.getDrawable()));
                ToastUtil.createAndShow(ImageEditActivity.this, "Image darkened!");
            }
        });

        ie_lighten = (TextView) findViewById(R.id.ie_brighten);
        ie_lighten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(BitmapUtil.lighten((BitmapDrawable) imageView.getDrawable()));
                ToastUtil.createAndShow(ImageEditActivity.this, "Image lightened!");
            }
        });

        ie_badblur = (TextView) findViewById(R.id.ie_badblur);
        ie_badblur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(BitmapUtil.badBlur((BitmapDrawable) imageView.getDrawable()));
                ToastUtil.createAndShow(ImageEditActivity.this, "Image blurred!");
            }
        });

    }

}

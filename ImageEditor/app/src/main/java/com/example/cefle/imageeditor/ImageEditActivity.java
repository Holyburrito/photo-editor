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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cefle.util.BitmapUtil;
import com.example.cefle.util.ToastUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Zach Reznicek on 11/10/17.
 */
public class ImageEditActivity extends AppCompatActivity {

    /**
     * Contains the image Bitmap that is being edited
     */
    private ImageView imageView;

    private ProgressBar progressBar;

    private TextView editButtonDarken;
    private TextView editButtonLighten;
    private TextView editButtonBadBlur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_edit_activity);

        // Get references to the components
        findViews();

        // Attach listeners to the components
        attachListeners();

        Intent intent = getIntent();
        try {
            final Uri imageUri = intent.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    /**
     * Find and cast all of the layout Views for ImageEditActivity
     */
    private void findViews() {
        imageView = (ImageView) findViewById(R.id.iv_image);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.bringToFront();
        progressBar.setProgress(50);
        editButtonDarken = (TextView) findViewById(R.id.ie_darken);
        editButtonLighten = (TextView) findViewById(R.id.ie_brighten);
        editButtonBadBlur = (TextView) findViewById(R.id.ie_badblur);
    }

    private void attachListeners() {
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
        editButtonDarken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setProgress(progressBar.getProgress() + 10);
                imageView.setImageBitmap(BitmapUtil.darken((BitmapDrawable) imageView.getDrawable()));
                ToastUtil.createAndShow(ImageEditActivity.this, "Image darkened!");
            }
        });
        editButtonLighten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(BitmapUtil.lighten((BitmapDrawable) imageView.getDrawable()));
                ToastUtil.createAndShow(ImageEditActivity.this, "Image lightened!");
            }
        });
        editButtonBadBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(BitmapUtil.badBlur((BitmapDrawable) imageView.getDrawable()));
                ToastUtil.createAndShow(ImageEditActivity.this, "Image blurred!");
            }
        });
    }

}

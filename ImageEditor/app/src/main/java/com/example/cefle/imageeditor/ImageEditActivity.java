package com.example.cefle.imageeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cefle.tasks.BitmapTasks;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Zach Reznicek on 11/10/17.
 */
public class ImageEditActivity extends AppCompatActivity {

    private AsyncTask currentTask;

    /**
     * Contains the image Bitmap that is being edited
     */
    private ImageView imageView;
    private ProgressBar progressBar;

    private TextView editButtonDarken;
    private TextView editButtonLighten;
    private TextView editButtonBadBlur;

    final Handler handler = new Handler();

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
        progressBar.setVisibility(View.INVISIBLE);
        editButtonDarken = (TextView) findViewById(R.id.ie_darken);
        editButtonLighten = (TextView) findViewById(R.id.ie_brighten);
        editButtonBadBlur = (TextView) findViewById(R.id.ie_badblur);
    }

    private void attachListeners() {
        // imageView.setOnTouchListener();

        editButtonDarken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTaskFinished()) {
                    currentTask = new BitmapTasks.Darken(ImageEditActivity.this);
                    ((BitmapTasks.Darken) currentTask).execute();
                }
            }
        });
        editButtonLighten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTaskFinished()) {
                    currentTask = new BitmapTasks.Lighten(ImageEditActivity.this);
                    ((BitmapTasks.Lighten) currentTask).execute();
                }
            }
        });
        editButtonBadBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTaskFinished()) {
                    currentTask = new BitmapTasks.Blur(ImageEditActivity.this);
                    ((BitmapTasks.Blur) currentTask).execute();
                }
            }
        });
    }

    /**
     * Tests to make sure that the current task running is finished
     * @return True The current task is finished (or null)
     * @return False The current task is not finished
     */
    private boolean isTaskFinished() {
        if (currentTask == null || currentTask.getStatus() == AsyncTask.Status.FINISHED) {
            return true;
        } else {
            return false;
        }
    }

    public Bitmap getBitmap() {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public void setBitmap(Bitmap bmp) {
        imageView.setImageBitmap(bmp);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBarProgress(int progress) {
        progressBar.setProgress(progress);
    }

}

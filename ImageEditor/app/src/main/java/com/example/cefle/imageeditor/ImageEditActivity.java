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
import com.example.cefle.util.ToastUtil;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Zach Reznicek on 11/10/17.
 */
public class ImageEditActivity extends AppCompatActivity {

    /**
     * The amount to scale down the selected image by. Improves performance by a LOT.
     */
    private final float SCALE_DOWN_AMOUNT = 0.20f;

    /**
     * Image editing tasks are all subclasses of AsyncTask.
     * When the user starts a task, this variable keeps a
     * reference to the current (most recently started) task.
     */
    private AsyncTask currentTask;

    /**
     * Contains the image Bitmap that is being edited
     */
    private ImageView imageView;

    /**
     * ProgressBar to show the user a visual indication of the editing
     * task's progress
     */
    private ProgressBar progressBar;

    private TextView editButtonDarken;
    private TextView editButtonLighten;
    private TextView editButtonBadBlur;
    private TextView editButtonDesaturate;
    private TextView editButtonSaturate;
    private TextView editButtonUnsaturate;
    private TextView editButtonRotate;

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
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Bitmap scaledImage = Bitmap.createScaledBitmap(selectedImage, Math.round(selectedImage.getWidth() * SCALE_DOWN_AMOUNT), Math.round(selectedImage.getHeight() * SCALE_DOWN_AMOUNT), true);
            imageView.setImageBitmap(scaledImage);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            ToastUtil.createAndShow(this, "Image cannot be found!");
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
        editButtonDesaturate = (TextView) findViewById(R.id.ie_desaturate);
        editButtonSaturate = (TextView) findViewById(R.id.ie_saturate);
        editButtonUnsaturate = (TextView) findViewById(R.id.ie_unsaturate);
        editButtonRotate = (TextView) findViewById(R.id.ie_rotate);
    }

    /**
     * Attach event listeners to the various View components of ImageEditActivity
     */
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
        editButtonDesaturate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTaskFinished()) {
                    currentTask = new BitmapTasks.Desaturate(ImageEditActivity.this);
                    ((BitmapTasks.Desaturate) currentTask).execute();
                }
            }
        });
        editButtonSaturate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTaskFinished()) {
                    currentTask = new BitmapTasks.Saturate(ImageEditActivity.this);
                    ((BitmapTasks.Saturate) currentTask).execute();
                }
            }
        });
        editButtonUnsaturate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTaskFinished()) {
                    currentTask = new BitmapTasks.Unsaturate(ImageEditActivity.this);
                    ((BitmapTasks.Unsaturate) currentTask).execute();
                }
            }
        });
        editButtonRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTaskFinished()) {
                    currentTask = new BitmapTasks.Rotate(ImageEditActivity.this);
                    ((BitmapTasks.Rotate) currentTask).execute();
                }
            }
        });
    }

    /**
     * Overriding the onBackPressed() method to make sure and cancel any
     * running tasks before finishing the activity and returning
     */
    @Override
    public void onBackPressed() {
        // If a task is currently running, stop it before finishing
        if (!isTaskFinished()) {
            currentTask.cancel(true);
            currentTask = null;
        }

        // Finish the activity and go back
        finish();
    }

    /**
     * Tests to make sure that the current task running is finished
     * @return True if the current task is finished (or null)
     * or False if the current task is not finished
     */
    private boolean isTaskFinished() {
        if (currentTask == null || currentTask.getStatus() == AsyncTask.Status.FINISHED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the ImageView's Bitmap object
     * @return The Bitmap associated with the ImageView
     */
    public Bitmap getBitmap() {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    /**
     * Set the ImageView's image with a Bitmap
     * @param bmp The Bitmap to set the ImageView's image to
     */
    public void setBitmap(Bitmap bmp) {
        imageView.setImageBitmap(bmp);
    }

    /**
     * Get progressBar
     * @return progressBar
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * Get the progress value of progressBar
     * @return The progress integer value of progressBar
     */
    public int getTaskProgress() {
        return progressBar.getProgress();
    }

    /**
     * Set the progress value of progressBar
     * @param progress The integer value to set progressBar's progress value to
     */
    public void setTaskProgress(int progress) {
        progressBar.setProgress(progress);
    }

}

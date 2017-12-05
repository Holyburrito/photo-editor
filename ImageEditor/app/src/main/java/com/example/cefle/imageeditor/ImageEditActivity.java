package com.example.cefle.imageeditor;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cefle.tasks.BitmapTasks;
import com.example.cefle.util.ToastUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
    private static AsyncTask currentTask;

    /**
     * Contains the image Bitmap that is being edited
     */
    private ImageView imageView;

    /**
     * An ArrayList of stored images that are able to be undo'd
     */
    private ArrayList<Bitmap> undoImages;

    /**
     * Number of allowed undos
     */
    private final int numberOfAllowedUndos = 3;

    private final float MAXIMUM_SIZE_IMAGE = 800.0f;

    /**
     * ProgressBar to show the user a visual indication of the editing
     * task's progress
     */
    private ProgressBar progressBar;

    private TextView toolbarButtonUndo;
    private TextView toolbarButtonSave;

    private TextView editButtonDarken;
    private TextView editButtonLighten;
    private TextView editButtonBadBlur;
    private TextView editButtonDesaturate;
    private TextView editButtonSaturate;
    private TextView editButtonRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_edit_activity);

        // Instantiate the array list...forgot this!
        undoImages = new ArrayList<>();

        // Get references to the components
        findViews();

        // Attach listeners to the components
        attachListeners();

        Intent intent = getIntent();
        try {
            final Uri imageUri = intent.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            if (selectedImage.getWidth() > MAXIMUM_SIZE_IMAGE || selectedImage.getHeight() > MAXIMUM_SIZE_IMAGE) {
                int largerSide = Math.max(selectedImage.getWidth(), selectedImage.getHeight());
                float ratio = largerSide / MAXIMUM_SIZE_IMAGE;
                int width = Math.round(selectedImage.getWidth() / ratio);
                int height = Math.round(selectedImage.getHeight() / ratio);
                selectedImage = Bitmap.createScaledBitmap(selectedImage, width, height, true);
            }
            imageView.setImageBitmap(selectedImage);
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

        toolbarButtonUndo = (TextView) findViewById(R.id.toolbar_undo);
        toolbarButtonSave = (TextView) findViewById(R.id.toolbar_save);

        editButtonDarken = (TextView) findViewById(R.id.ie_darken);
        editButtonLighten = (TextView) findViewById(R.id.ie_brighten);
        editButtonBadBlur = (TextView) findViewById(R.id.ie_badblur);
        editButtonDesaturate = (TextView) findViewById(R.id.ie_desaturate);
        editButtonSaturate = (TextView) findViewById(R.id.ie_saturate);
        editButtonRotate = (TextView) findViewById(R.id.ie_rotate);
    }

    /**
     * Attach event listeners to the various View components of ImageEditActivity
     */
    private void attachListeners() {
        toolbarButtonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap oldImage = getUndoableImage();
                if (oldImage != null) {
                    imageView.setImageBitmap(oldImage);
                    ToastUtil.createAndShow(ImageEditActivity.this, "Image undo'd");
                }
            }
        });
        toolbarButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String filename = System.nanoTime() + ".png";
                File file = new File(path, filename);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                            addImageGallery(file);
                            ToastUtil.createAndShow(ImageEditActivity.this, "Save Complete!");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
        if (currentTask != null) currentTask.cancel(true);
        currentTask = null;

        // Finish the activity and go back
        finish();
    }

    /**
     * Tests to make sure that the current task running is finished
     * @return True if the current task is finished (or null)
     * or False if the current task is not finished
     */
    private boolean isTaskFinished() {
        if (currentTask == null || currentTask.getStatus() == AsyncTask.Status.FINISHED || currentTask.isCancelled()) {
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

    /**
     * Updates the MediaStore by giving it information about where the new file was saved
     * @param file The file of which to alert the MediaStore of
     */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public void addUndoableImage(Bitmap bmp) {
        if (undoImages.size() < numberOfAllowedUndos) {
            undoImages.add(bmp);
        } else {
            int index = 0;
            while (index < numberOfAllowedUndos - 1) {
                undoImages.set(index, undoImages.get(index + 1));
                index++;
            }
            undoImages.set(numberOfAllowedUndos - 1, bmp);
        }
        System.out.println("Size of stuff: " + undoImages.size());
    }

    private Bitmap getUndoableImage() {
        if (undoImages.size() == 0) {
            return null;
        }
        Bitmap undoImage = undoImages.get(undoImages.size() - 1);
        undoImages.remove(undoImages.size() - 1);
        return undoImage;
    }

}

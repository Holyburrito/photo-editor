package com.example.cefle.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import com.example.cefle.imageeditor.ImageEditActivity;
import com.example.cefle.util.ColorUtil;
import com.example.cefle.util.ToastUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Zach Reznicek on 11/30/2017.
 */
public class BitmapTasks {

    /**
     * The factor at which to scale the ARGB values when darkening a Bitmap
     */
    private static final float DARKEN_AMOUNT = 0.80f;

    /**
     * The factor at which to scale the ARGB values when lightening a Bitmap
     */
    private static final float LIGHTEN_AMOUNT = 1.20f;

    /**
     * The maximum value of color saturation for saturating a Bitmap
     */
    private static final float HIGH_SATURATION = 1.0f;

    /**
     * An arbitrary low value for saturation used for de-saturating a Bitmap
     */
    private static final float LOW_SATURATION = 0.0f;

    private static final float BITMAP_SCALE = 0.4f;

    private static final float BLUR_RADIUS = 3.0f;

    public static class Darken extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Darken(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
            activity.addUndoableImage(activity.getBitmap());
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap bmp = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(bmp);
            Paint p = new Paint(Color.RED);
            ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
            p.setColorFilter(filter);
            canvas.drawBitmap(bmp, new Matrix(), p);

            return bmp;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setTaskProgress(integers[0]);
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setBitmap(result);
            activity.setTaskProgress(0);
            activity.getProgressBar().setVisibility(View.GONE);
        }
    }

    public static class Blur extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Blur(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
            activity.addUndoableImage(activity.getBitmap());
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap copy = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

            int width = Math.round(copy.getWidth() * BITMAP_SCALE);
            int height = Math.round(copy.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(copy, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(activity.getApplicationContext());
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            return outputBitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setTaskProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setBitmap(result);
            activity.setTaskProgress(0);
            activity.getProgressBar().setVisibility(View.GONE);
        }
    }

    public static class Lighten extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Lighten(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
            activity.addUndoableImage(activity.getBitmap());
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap bmp = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(bmp);
            Paint p = new Paint(Color.RED);
            ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
            p.setColorFilter(filter);
            canvas.drawBitmap(bmp, new Matrix(), p);
            return bmp;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setTaskProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setBitmap(result);
            activity.setTaskProgress(0);
            activity.getProgressBar().setVisibility(View.GONE);
        }
    }

    public static class Rotate extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Rotate(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
            activity.addUndoableImage(activity.getBitmap());
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap bmp = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

            Matrix matrix = new Matrix();

            matrix.postRotate(90);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp,bmp.getWidth(),bmp.getHeight(),true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            return rotatedBitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setTaskProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setBitmap(result);
            activity.setTaskProgress(0);
            activity.getProgressBar().setVisibility(View.GONE);
        }
    }

    public static class Desaturate extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Desaturate(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
            activity.addUndoableImage(activity.getBitmap());
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap bmp = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(bmp);
            Paint paint = new Paint();

            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0.5f);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

            paint.setColorFilter(filter);
            canvas.drawBitmap(bmp, 0, 0, paint);

            return bmp;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setTaskProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setBitmap(result);
            activity.setTaskProgress(0);
            activity.getProgressBar().setVisibility(View.GONE);
        }
    }

    public static class Greyscale extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Greyscale(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
            activity.addUndoableImage(activity.getBitmap());
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap bmp = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(bmp);
            Paint paint = new Paint();

            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

            paint.setColorFilter(filter);
            canvas.drawBitmap(bmp, 0, 0, paint);

            return bmp;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setTaskProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setBitmap(result);
            activity.setTaskProgress(0);
            activity.getProgressBar().setVisibility(View.GONE);
        }
    }

    public static class Saturate extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Saturate(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
            activity.addUndoableImage(activity.getBitmap());
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap bmp = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(bmp);
            Paint paint = new Paint();

            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(1.5f);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

            paint.setColorFilter(filter);
            canvas.drawBitmap(bmp, 0, 0, paint);

            return bmp;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setTaskProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.setBitmap(result);
            activity.setTaskProgress(0);
            activity.getProgressBar().setVisibility(View.GONE);
        }
    }

    /**
     * Helper method for the inner static AsyncTasks to make sure that the reference
     * exists before using it. Protects against NullPointerException in the case that
     * the activity is removed before the task completes
     * @param iea The WeakReference to check
     * @throws IllegalStateException if the reference has been broken
     * @return The ImageEditActivity within the reference
     */
    public static ImageEditActivity getReferenceIfExists(WeakReference<ImageEditActivity> iea) {
        if (iea.get() != null) {
            return iea.get();
        } else {
            throw new IllegalStateException("");
        }
    }

}

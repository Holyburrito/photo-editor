package com.example.cefle.tasks;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
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
     * The size of the blur radius neighborhood
     */
    private static final int BLUR_RADIUS = 3;

    /**
     * The factor at which to scale the ARGB values when darkening a Bitmap
     */
    private static final float DARKEN_AMOUNT = 0.80f;

    /**
     * The factor at which to scale the ARGB values when lightening a Bitmap
     */
    private static final float LIGHTEN_AMOUNT = 1.20f;

    public static class Darken extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Darken(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            publishProgress(0);
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap bmp = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            for (int i = 0; i < bmp.getHeight(); i++) {
                for (int j = 0; j < bmp.getWidth(); j++) {
                    int colorInt = bmp.getPixel(j, i);
                    int newColor = ColorUtil.modifyColor(colorInt, DARKEN_AMOUNT);
                    bmp.setPixel(j, i, newColor);
                }
                int progress = (int) (((float) i / (float) bmp.getHeight()) * 100);
                publishProgress(progress);
            }
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
            ToastUtil.createAndShow(activity, "Darken Completed!");
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
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            publishProgress(0);
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap copy = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            int width = copy.getWidth();
            int height = copy.getHeight();

            // Create a blank bitmap the same dimensions as the copy
            final Bitmap blurred = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            // For every pixel
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    // Get the neighbor bounds for the pixel
                    int startY = Math.max(i - BLUR_RADIUS, 0);
                    int endY = Math.min(i + BLUR_RADIUS, height - 1);
                    int startX = Math.max(j - BLUR_RADIUS, 0);
                    int endX = Math.min(j + BLUR_RADIUS, width - 1);

                    // Set up some color variables
                    int totalA, totalR, totalG, totalB, totalPixels;
                    totalA = totalR = totalG = totalB = totalPixels = 0;

                    // Loop through the neighbors and add on their color data
                    for (int currentY = startY; currentY <= endY; currentY++) {
                        for (int currentX = startX; currentX <= endX; currentX++) {
                            int currentPixelColor = copy.getPixel(currentX, currentY);
                            totalA += Color.alpha(currentPixelColor);
                            totalR += Color.red(currentPixelColor);
                            totalG += Color.green(currentPixelColor);
                            totalB += Color.blue(currentPixelColor);
                            totalPixels++;
                        }
                    }

                    // Average the colors
                    int averagedA = totalA / totalPixels;
                    int averagedR = totalR / totalPixels;
                    int averagedG = totalG / totalPixels;
                    int averagedB = totalB / totalPixels;

                    int newColor = ColorUtil.argbToColorInt(averagedA, averagedR, averagedG, averagedB);
                    blurred.setPixel(j, i, newColor);

                }
                int progress = (int) (((float) i / (float) height) * 100);
                publishProgress(progress);
            }

            return blurred;
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
            ToastUtil.createAndShow(activity, "Blur Completed!");
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
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            publishProgress(0);
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            Bitmap bmp = activity.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            for (int i = 0; i < bmp.getHeight(); i++) {
                for (int j = 0; j < bmp.getWidth(); j++) {
                    int colorInt = bmp.getPixel(j, i);
                    int newColor = ColorUtil.modifyColor(colorInt, LIGHTEN_AMOUNT);
                    bmp.setPixel(j, i, newColor);
                }
                int progress = (int) (((float) i / (float) bmp.getHeight()) * 100);
                publishProgress(progress);
            }
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
            ToastUtil.createAndShow(activity, "Lighten Completed!");
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
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            // TODO: 12/4/2017  Saturate image
            return null;
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
            ToastUtil.createAndShow(activity, "Saturation Completed!");
        }
    }

    public static class Unsaturate extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Unsaturate(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            //// TODO: 12/4/2017 De-saturate image
            return null;
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
            ToastUtil.createAndShow(activity, "Desaturation Completed!");
        }
    }

    public static class Decolorize extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Decolorize(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return null;
            // TODO: 12/4/2017 de-colorize image
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
            ToastUtil.createAndShow(activity, "Decolorization Completed!");
        }
    }

    public static class Colorize extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Colorize(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            ImageEditActivity activity = BitmapTasks.getReferenceIfExists(iea);
            activity.getProgressBar().setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            // TODO: 12/4/2017 colorize image
            return null;
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
            ToastUtil.createAndShow(activity, "Colorization Completed!");
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

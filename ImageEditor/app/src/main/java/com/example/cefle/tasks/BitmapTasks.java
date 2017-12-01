package com.example.cefle.tasks;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
    public static final int BLUR_RADIUS = 3;

    /**
     * The factor at which to scale the ARGB values when darkening a Bitmap
     */
    public static final float DARKEN_AMOUNT = 0.80f;

    /**
     * The factor at which to scale the ARGB values when lightening a Bitmap
     */
    public static final float LIGHTEN_AMOUNT = 1.20f;

    public static class Darken extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Darken(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            iea.get().getProgressBar().setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bmp = iea.get().getBitmap().copy(Bitmap.Config.ARGB_8888, true);
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
            iea.get().setProgressBarProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            iea.get().setBitmap(result);
            iea.get().setProgressBarProgress(0);
            iea.get().getProgressBar().setVisibility(View.GONE);
            ToastUtil.createAndShow(iea.get(), "Darken Completed!");
        }
    }

    public static class Blur extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Blur(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            iea.get().getProgressBar().setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            publishProgress(0);

            // Get the copy and its dimensions
            Bitmap copy = iea.get().getBitmap().copy(Bitmap.Config.ARGB_8888, true);
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
            iea.get().setProgressBarProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            iea.get().setBitmap(result);
            iea.get().setProgressBarProgress(0);
            iea.get().getProgressBar().setVisibility(View.GONE);
            ToastUtil.createAndShow(iea.get(), "Blur Completed!");
        }
    }

    public static class Lighten extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageEditActivity> iea;

        public Lighten(ImageEditActivity iea) {
            this.iea = new WeakReference<>(iea);
        }

        @Override
        protected void onPreExecute() {
            iea.get().getProgressBar().setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bmp = iea.get().getBitmap().copy(Bitmap.Config.ARGB_8888, true);
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
            iea.get().setProgressBarProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            iea.get().setBitmap(result);
            iea.get().setProgressBarProgress(0);
            iea.get().getProgressBar().setVisibility(View.GONE);
            ToastUtil.createAndShow(iea.get(), "Lighten Completed!");
        }
    }

}

package com.example.cefle.imageeditor;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Buster on 11/12/2017.
 */

public class BitmapUtil {

    public static final int BLUR_RADIUS = 2;
    public static final float DARKEN_AMOUNT = 0.80f;
    public static final float BRIGHTEN_AMOUNT = 1.20f;

    public static Bitmap badBlur(BitmapDrawable bitmapDrawable) {

        // Get the copy and its dimensions
        Bitmap copy = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        int width = copy.getWidth();
        int height = copy.getHeight();

        // Create a blank bitmap the same dimensions as the copy
        Bitmap blurred = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

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
        }
        return blurred;
    }

    public static Bitmap darken(BitmapDrawable bitmapDrawable) {
        Bitmap bmp = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 0; i < bmp.getHeight(); i++) {
            for (int j = 0; j < bmp.getWidth(); j++) {
                int colorInt = bmp.getPixel(j, i);
                int newColor = ColorUtil.modifyColor(colorInt, DARKEN_AMOUNT);
                bmp.setPixel(j, i, newColor);
            }
        }
        return bmp;
    }

    public static Bitmap lighten(BitmapDrawable bitmapDrawable) {
        Bitmap bmp = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 0; i < bmp.getHeight(); i++) {
            for (int j = 0; j < bmp.getWidth(); j++) {
                int colorInt = bmp.getPixel(j, i);
                int newColor = ColorUtil.modifyColor(colorInt, BRIGHTEN_AMOUNT);
                bmp.setPixel(j, i, newColor);
            }
        }
        return bmp;
    }

}

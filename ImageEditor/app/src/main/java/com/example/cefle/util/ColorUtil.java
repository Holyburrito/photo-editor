package com.example.cefle.util;

import android.graphics.Color;

/**
 * Created by Zach Reznicek on 11/10/2017.
 */
public class ColorUtil {

    /**
     * Converts four 8 bit integer values representing each of the ARGB channels of
     * a color into a single 32 bit integer that represents the color.
     * @param a - The alpha channel of the color
     * @param r - The red channel of the color
     * @param g - The green channel of the color
     * @param b - The blue channel of the color
     * @return - A 32 bit integer representation of a color
     */
    public static int argbToColorInt(int a, int r, int g, int b) {
        a = (a < 255) ? a : 255;
        r = (r < 255) ? r : 255;
        g = (g < 255) ? g : 255;
        b = (b < 255) ? b : 255;
        return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }

    /**
     * Scale the ARGB values of a integer represented color by a single modifier
     * @param colorInt - The color represented as a 32 bit integer
     * @param colorModifier - A float value to scale each color channel by
     * @return A color scaled by a single float modifier
     */
    public static int modifyColor(int colorInt, float colorModifier) {
        return ColorUtil.modifyColor(colorInt, colorModifier, colorModifier, colorModifier, colorModifier);
    }

    /**
     * Scale the ARGB values of an integer represented color by individual modifiers
     * @param colorInt - The color represented as a 32 bit integer
     * @param aModifier - A float value to scale a color's alpha channel by
     * @param rModifier - A float value to scale a color's red channel by
     * @param gModifier - A float value to scale a color's green channel by
     * @param bModifier - A float value to scale a color's blue channel by
     * @return A color scaled by channel independent float modifiers
     */
    public static int modifyColor(int colorInt, float aModifier, float rModifier, float gModifier, float bModifier) {
        int a = Color.alpha(colorInt);
        int r = Color.red(colorInt);
        int g = Color.green(colorInt);
        int b = Color.blue(colorInt);

        a = (a * aModifier > 255) ? 255 : (int) (a * aModifier);
        r = (r * rModifier > 255) ? 255 : (int) (r * rModifier);
        g = (g * gModifier > 255) ? 255 : (int) (g * gModifier);
        b = (b * bModifier > 255) ? 255 : (int) (b * bModifier);

        return ColorUtil.argbToColorInt(a, r, g, b);
    }

}

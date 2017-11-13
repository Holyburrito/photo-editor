package com.example.cefle.imageeditor;

import android.graphics.Color;

/**
 * Created by Buster on 11/10/2017.
 */

public class Util {

    public static int argbToColorInt(int a, int r, int g, int b) {
        return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }

    public static int modifyColor(int colorInt, float colorModifier) {
        int a = Color.alpha(colorInt);
        int r = Color.red(colorInt);
        int g = Color.green(colorInt);
        int b = Color.blue(colorInt);

        r = (r * colorModifier > 255) ? 255 : (int) (r * colorModifier);
        g = (g * colorModifier > 255) ? 255 : (int) (g * colorModifier);
        b = (b * colorModifier > 255) ? 255 : (int) (b * colorModifier);

        return Util.argbToColorInt(a, r, g, b);
    }

    public static int modifyColor(int colorInt, float aModifier, float rModifier, float gModifier, float bModifier) {
        int a = Color.alpha(colorInt);
        int r = Color.red(colorInt);
        int g = Color.green(colorInt);
        int b = Color.blue(colorInt);

        a = (a * aModifier > 255) ? 255 : (int) (a * aModifier);
        r = (r * rModifier > 255) ? 255 : (int) (r * rModifier);
        g = (g * gModifier > 255) ? 255 : (int) (g * gModifier);
        b = (b * bModifier > 255) ? 255 : (int) (b * bModifier);

        return Util.argbToColorInt(a, r, g, b);
    }

}

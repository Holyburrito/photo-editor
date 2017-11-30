package com.example.cefle.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cefle.imageeditor.R;

/**
 * Created by Zach Reznicek on 11/29/2017.
 */
public class ToastUtil {

    /**
     * Holds a reference the last-displayed custom toast
     */
    private static Toast lastToast;

    /**
     * Creates and shows a custom Toast
     * @param activity - The activity creating the Toast
     * @param message - The toast's text
     */
    public static void createAndShow(Activity activity, String message) {

        // If there is already a toast active, cancel it so that it fades out
        if (lastToast != null) {
            lastToast.cancel();
            lastToast = null;
        }

        // Inflate the custom toast layout
        View layout = activity.getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.custom_toast_container));

        // Set the toast message
        ((TextView) layout.findViewById(R.id.custom_toast_text)).setText(message);

        // Create the toast
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.TOP, Gravity.CENTER, Gravity.TOP);
        toast.setDuration(Toast.LENGTH_SHORT);

        // Attach the view to the toast
        toast.setView(layout);

        // Make the toast visible
        toast.show();

        // Assign lastToast so that if another custom toast is created before it is finished showing,
        // a new toast can take its place. This removes the toast queue and makes toasts snappy.
        lastToast = toast;
    }

}

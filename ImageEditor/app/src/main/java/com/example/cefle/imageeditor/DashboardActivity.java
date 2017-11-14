package com.example.cefle.imageeditor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DashboardActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_FROM_GALLERY = 100;

    private ImageButton selectFromGalleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        findViews();
        attachListeners();
    }

    private void findViews() {
        selectFromGalleryButton = (ImageButton) findViewById(R.id.btn_select_from_gallery);
    }

    private void attachListeners() {
        selectFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickerIntent = new Intent(Intent.ACTION_PICK);
                pickerIntent.setType("image/*");
                startActivityForResult(pickerIntent, REQUEST_CODE_SELECT_FROM_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {
                Intent imageEditIntent = new Intent(this, ImageEditActivity.class);
                imageEditIntent.setData(data.getData());
                startActivity(imageEditIntent);
            }
        }

    }
}

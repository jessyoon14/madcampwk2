package com.example.Wk2_project;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class FullImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        ImageView picturesView = (ImageView) findViewById(R.id.full_image_view);
        // get intent data
        Intent i = getIntent();

        // Selected image id

        String path = i.getExtras().getString("path");

        Glide.with(FullImageActivity.this).load(new File(path))
                .placeholder(R.drawable.pic_1)
                .into(picturesView);
    }

}
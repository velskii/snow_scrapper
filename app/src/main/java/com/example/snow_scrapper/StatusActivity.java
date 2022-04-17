package com.example.snow_scrapper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.thankyou);
    }
}
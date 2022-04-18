package com.example.snow_scrapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class StatusActivity extends AppCompatActivity {

    private Button orderHis1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.thankyou);

        orderHis1 = (Button) findViewById(R.id.orderHist1);
        orderHis1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                orderHistory1();
            }
        });
    }

    public void orderHistory1(){
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        startActivity(intent);
    }
}
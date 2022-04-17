package com.example.snow_scrapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderDetailsActivity extends AppCompatActivity {

    private Button paybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        paybtn = (Button) findViewById(R.id.payBtn);
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    payOption();
            }
        });
    }

    public void payOption(){
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }
}
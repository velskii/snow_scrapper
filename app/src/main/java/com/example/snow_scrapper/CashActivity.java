package com.example.snow_scrapper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CashActivity extends AppCompatActivity {

    private Button orderHis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        orderHis = (Button) findViewById(R.id.orderHis);
        orderHis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                orderHistory();
            }
        });
    }

    public void orderHistory(){
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        startActivity(intent);
    }
}
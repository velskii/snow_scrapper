package com.example.snow_scrapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends AppCompatActivity {

    private Button btnCash;
    private Button btnCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        btnCash = (Button) findViewById(R.id.btnCash);
        btnCash.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                payCash();
            }
        });

        btnCard = (Button) findViewById(R.id.btnCard);
        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payCard();
            }
        });
    }

    public void payCash(){
        Intent intent = new Intent(this, CashActivity.class);
        startActivity(intent);
    }

    public void payCard(){
        Intent i = new Intent(this, CardActivity.class);
        startActivity(i);
    }
}
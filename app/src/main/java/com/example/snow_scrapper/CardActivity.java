package com.example.snow_scrapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CardActivity extends AppCompatActivity{

    EditText cardDate;
    private Button btnPay;
    private TextView returnOpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        EditText cardName = findViewById(R.id.cardName);
        EditText cardNo = findViewById(R.id.cardNo);
        EditText cvvNo = findViewById(R.id.cvvNo);
        returnOpt = (TextView) findViewById(R.id.returnOpt);

        returnOpt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) { returnOpt();}
        });

        btnPay = (Button) findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                payNow();
            }
        });
    }

    public void payNow(){
        Intent in = new Intent(this, StatusActivity.class);
        startActivity(in);
    }

    public void returnOpt(){
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void onClick(View view) {
//        if (view == cardDate) {
//            final Calendar calendar = Calendar.getInstance ();
//            mYear = calendar.get ( Calendar.YEAR );
//            mMonth = calendar.get ( Calendar.MONTH );
//
//            //show dialog
//            DatePickerDialog datePickerDialog = new DatePickerDialog ( this, new DatePickerDialog.OnDateSetListener () {
//                @Override
//                public void onDateSet(DatePicker view, int year, int month) {
//                    cardDate.setText ( (month + 1) + "-" + year );
//                }
//            }, mYear, mMonth);
//            datePickerDialog.show ();
//        }
//    }
}

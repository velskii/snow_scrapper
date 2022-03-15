package com.example.snow_scrapper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.snow_scrapper.tradesman_fragments.TradesmanOrdersFragment;
import com.example.snow_scrapper.tradesman_fragments.TradesmanServiceListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ServiceDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ServiceDetailsActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String id = intent.getStringExtra("item_id");

        Log.d("Zhou", id);
        getServiceDetailsById(mAuth.getUid(), id);

        Button btnPurchase = findViewById(R.id.purchase_service);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myIntent = new Intent(ServiceDetailsActivity.this, OrdersActivity.class);
//                myIntent.putExtra("order_id", 1);
//                startActivity(myIntent);

                Fragment fragment;
                fragment = new TradesmanOrdersFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName())
                        .commit();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.tradesman_content_frame, fragment, fragment.getClass().getSimpleName());
                transaction.commit();
            }
        });

    }




    public void getServiceDetailsById(String uid, String item_id) {

        db.collection("service_list")
                .whereEqualTo(FieldPath.documentId(), item_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView item_name = findViewById(R.id.tradesman_item_name);
                                item_name.setText(document.getString("name"));

                                TextView location = findViewById(R.id.tradesman_item_location);
                                location.setText(document.getString("location"));

                                TextView image = findViewById(R.id.tradesman_item_image);
                                image.setText(document.getString("image"));

                                TextView price = findViewById(R.id.tradesman_item_price);
                                price.setText(document.getString("price"));

                                TextView rating = findViewById(R.id.tradesman_item_rating);
                                rating.setText(document.getLong("rating").toString());

                                TextView range = findViewById(R.id.tradesman_item_range);
                                range.setText(document.getString("range"));

//                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


}
package com.example.snow_scrapper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String uid = currentUser.getUid();
            getUserInfo(uid);
            startActivity(new Intent(MainActivity.this, HomeActivity.class));

        } else {
//            Toast.makeText(MainActivity.this, "Please login first.",
//                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    public void getUserInfo(String uid) {
        this.db.collection("users")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

//                                TextView tv_uid = findViewById(R.id.uid);
//                                TextView tv_username = findViewById(R.id.username);
//                                TextView tv_role = findViewById(R.id.user_role);
//                                TextView tv_address = findViewById(R.id.user_address);
//                                TextView tv_email = findViewById(R.id.user_email);
//                                tv_uid.setText(document.getData().get("username").toString());
//                                tv_username.setText(document.getData().get("username").toString());
//                                tv_role.setText(document.getData().get("role").toString());
//                                tv_address.setText(document.getData().get("address").toString());
//                                tv_email.setText(document.getData().get("email").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}